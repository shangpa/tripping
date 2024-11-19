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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (userDetails == null) {
            return "redirect:/user/login";
        }

        // 현재 로그인된 사용자 정보 가져오기
        SiteUser user = userService.getCurrentUser(userDetails.getUsername()); // userService를 통해 사용자 정보 조회
        if (user == null) {
            return "redirect:/user/login"; // 유효하지 않은 사용자일 경우
        }

        // 사용자 정보를 모델에 추가
        model.addAttribute("user", user);

        // 사용자 소개 추가 (null 체크)
        String intro = user.getIntro() != null ? user.getIntro() : "자기소개를 입력하세요."; // 기본값 설정
        model.addAttribute("intro", intro);

        return "edit"; // edit.html 페이지로 리턴
    }



    @PostMapping("/edit")
    public String edit(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }

        try {
            // 로그인한 사용자의 정보를 업데이트
            String username = userDetails.getUsername();
            SiteUser siteUser = userService.getUser(username);

            // userCreateForm을 사용하여 정보를 업데이트
            siteUser.setNickname(userCreateForm.getNickname());
            siteUser.setPhone(userCreateForm.getPhone());
            siteUser.setName(userCreateForm.getName());
            siteUser.setBirthdate(userCreateForm.getBirthdate());
            siteUser.setAddress(userCreateForm.getAddress()); // address 필드 추가

            // 수정된 정보를 DB에 저장
            userService.updateUser(siteUser);

            model.addAttribute("message", "회원정보가 업데이트되었습니다.");
        } catch (Exception e) {
            bindingResult.reject("updateFailed", "회원정보 업데이트 중 오류가 발생했습니다.");
            return "edit";
        }

        return "redirect:/user/main"; // 성공 시 메인 페이지로 리다이렉트
    }

}
