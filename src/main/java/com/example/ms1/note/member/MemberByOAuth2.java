package com.example.ms1.note.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberByOAuth2 {

    private String username;

    private String password;

    private String nickname;

    private String email;

}
