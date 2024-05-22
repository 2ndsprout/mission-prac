package com.example.ms1.note.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "ID 를 입력해주세요")
    private String username;

    @NotEmpty(message = "Password 를 입력해주세요")
    private String password1;

    @NotEmpty(message = "Password 를 확인해주세요")
    private String password2;

    @NotEmpty(message = "Nickname 을 입력해주세요")
    private String nickname;

    @NotEmpty(message = "Email 를 입력해주세요")
    @Email
    private String email;
}
