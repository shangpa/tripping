package com.study.board.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("userCreateForm", new UserCreateForm());
        return "join"; // 회원가입 화면
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "join";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordMismatch", "비밀번호가 일치하지 않습니다.");
            return "join";
        }

        try {
            userService.create(userCreateForm);
            model.addAttribute("message", "회원가입이 완료되었습니다. 로그인하세요.");
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupFailed", "이미 사용 중인 사용자명입니다.");
            return "join";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", "회원가입 중 오류가 발생했습니다.");
            return "join";
        }

        return "redirect:/user/login"; // 성공 시 로그인 화면으로 리다이렉트
    }

    @GetMapping("/login")
    public String login() {
        return "login_form"; // 로그인 화면
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        SiteUser user = userService.validateUser(username, password);

        if (user != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("loggedInUser", user);
            return "redirect:/user/main"; // 메인 페이지로 리다이렉트
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login_form";
        }
    }


    @GetMapping("/main")
    public String mainPage(Model model) {
        // 게시글 목록 추가 시 여기에 로직 추가 가능
        return "/main";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/edit")
    public String editUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/user/login";
        }

        SiteUser user = userService.getCurrentUser(userDetails.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }

        model.addAttribute("user", user);
        model.addAttribute("intro", Optional.ofNullable(user.getIntro()).orElse("자기소개를 입력하세요."));

        // 생년월일을 LocalDate로 처리하려면 birthdate 필드를 LocalDate로 변경
        LocalDate birthdate = user.getBirthdate(); // 이미 LocalDate이면 그대로 사용
        model.addAttribute("birthdate", birthdate);

        return "edit"; // edit.html 페이지로 리턴
    }

    @PostMapping("/edit")
    public String edit(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        try {
            // 로그인한 사용자의 정보를 가져옴
            String username = userDetails.getUsername();
            SiteUser siteUser = userService.getUser(username);

            // 생년월일 조합
            try {
                // getYear(), getMonth(), getDay()가 int 형일 경우 String으로 변환
                String year = String.valueOf(userCreateForm.getYear());
                String month = String.format("%02d", userCreateForm.getMonth()); // 두 자리로 포맷
                String day = String.format("%02d", userCreateForm.getDay()); // 두 자리로 포맷

                String birthdateString = year + "-" + month + "-" + day;
                LocalDate birthdate = LocalDate.parse(birthdateString);  // 문자열을 LocalDate로 변환
                siteUser.setBirthdate(birthdate); // birthdate는 LocalDate 타입이어야 하므로, birthdate를 set
            } catch (DateTimeParseException e) {
                bindingResult.rejectValue("birthdate", "invalidBirthdate", "올바른 생년월일 형식을 입력하세요.");
                return "edit";
            }



            // 기타 정보 업데이트
            siteUser.setNickname(userCreateForm.getNickname());
            siteUser.setPhone(userCreateForm.getPhone());
            siteUser.setName(userCreateForm.getName());
            siteUser.setAddress(userCreateForm.getAddress()); // address 필드 추가

            // 수정된 정보를 저장
            userService.updateUser(siteUser);

            model.addAttribute("message", "회원정보가 업데이트되었습니다.");
        } catch (Exception e) {
            bindingResult.reject("updateFailed", "회원정보 업데이트 중 오류가 발생했습니다.");
            return "edit";
        }

        return "redirect:/user/main"; // 성공 시 메인 페이지로 리다이렉트
    }

    @PostMapping("/uploadProfile")
    public String uploadProfileImage(@RequestParam("profileImage") MultipartFile file,
                                     @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (!file.isEmpty()) {
            String username = userDetails.getUsername();
            try {
                byte[] imageBytes = file.getBytes(); // 파일을 byte[]로 변환
                userService.updateProfileImage(username, imageBytes); // DB에 이미지 저장
                model.addAttribute("message", "프로필 이미지가 성공적으로 업로드되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
                return "edit";
            }
        }
        return "redirect:/user/edit"; // 업로드 후 사용자 편집 페이지로 리다이렉트
    }

    //이거슨 탈퇴
    @DeleteMapping("/delete")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        try {
            String username = userDetails.getUsername();
            //userService.deleteUser(username);  // 삭제 처리
            session.invalidate();  // 세션 종료
            return "redirect:/user/signup";  // 회원가입 페이지로 리다이렉트
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/edit";  // 삭제 실패 시 편집 페이지로 리다이렉트
        }
    }

    @GetMapping("/profileImage/{username}")
    @ResponseBody
    public byte[] getProfileImage(@PathVariable String username) {
        SiteUser user = userService.getUser(username);

        if (user.getProfileImage() == null) {
            throw new IllegalArgumentException("프로필 이미지가 없습니다.");
        }

        return user.getProfileImage(); // 이미지를 반환
    }
}
