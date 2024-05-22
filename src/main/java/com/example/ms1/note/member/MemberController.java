package com.example.ms1.note.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signup (MemberForm memberForm) {
        return "signup_form";
    }
    @PostMapping("/signup")
    public String signup (@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect","비밀번호가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            this.memberService.create(memberForm.getUsername(),memberForm.getPassword1(),memberForm.getNickname(),memberForm.getEmail());
        }catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed","동일한 사용자가 있습니다.");
            return "signup_form";

        }catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";

        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login () {
        return "login_form";
    }
}
