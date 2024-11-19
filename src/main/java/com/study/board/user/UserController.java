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
    public String edit() {
        return "edit";
    }
}
