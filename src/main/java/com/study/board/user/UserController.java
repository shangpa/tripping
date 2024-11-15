package com.study.board.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "join";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "join";
        }
        if (userCreateForm.getPassword1().isEmpty() || userCreateForm.getPassword2().isEmpty()) {
            bindingResult.rejectValue("password1", "passwordEmpty", "비밀번호는 빈칸일 수 없습니다.");
            bindingResult.rejectValue("password2", "passwordEmpty", "비밀번호 확인은 빈칸일 수 없습니다.");
            return "join";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "join";
        }
        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(), userCreateForm.getPassword1(),userCreateForm.getNickname(),   // 추가된 필드
                    userCreateForm.getPhone(),      // 추가된 필드
                    userCreateForm.getName(),       // 추가된 필드
                    userCreateForm.getBirthdate()); // 추가된 필드);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "join";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/signup";
        }

        return "redirect:/user/login";
    }
    //PostMapping은 스프링시큐리티가 대신해줌.
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    //이거슨 메인
    @GetMapping("/main")
    public String mainPage(Model model) {
        // 게시글 목록을 DB에서 가져옴
        //List<Board> hotPosts = boardRepository.findAll(); // DB에서 게시글 목록을 가져오기

        // 모델에 게시글 목록 추가
        //model.addAttribute("hotPosts", hotPosts);

        return "/main"; // main 페이지로 이동
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/edit")
    public String edit() {return "edit";}
}