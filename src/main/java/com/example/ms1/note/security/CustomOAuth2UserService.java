package com.example.ms1.note.security;

import com.example.ms1.note.member.Member;
import com.example.ms1.note.member.MemberByOAuth2;
import com.example.ms1.note.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User user = super.loadUser(userRequest);
        MemberByOAuth2 memberByOAuth2;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        switch (registrationId) {
            case "google" -> memberByOAuth2 = this.googleService(user, registrationId);
            case "naver" -> memberByOAuth2 = this.naverService(user, registrationId);
            case "kakao" -> memberByOAuth2 = this.kakaoService(user,registrationId);
            default -> throw new IllegalStateException("Unexpected value: " + registrationId);
        }
        Optional<Member> _member = this.memberRepository.findByUsername(memberByOAuth2.getUsername());
        Member member = new Member();

        if (_member.isPresent()) {
            member = _member.get();
            System.out.println("기존 회원 로그인");
        }
        else {
            member.setUsername(memberByOAuth2.getUsername());
            member.setPassword(memberByOAuth2.getPassword());
            member.setNickname(memberByOAuth2.getNickname());
            member.setEmail(memberByOAuth2.getEmail());
            member.setCreateDate(LocalDateTime.now());
            this.memberRepository.save(member);
            System.out.println("신규회원 DB 저장 및 로그인");
        }

        return super.loadUser(userRequest);
    }

    public MemberByOAuth2 googleService (OAuth2User user, String registrationId) {

        String username = user.getAttribute("sub");
        String password = "";
        String nickname = registrationId + "_" + user.getAttribute("name");
        String email = user.getAttribute("email");

        return new MemberByOAuth2(username, password, nickname, email);
    }

    public MemberByOAuth2 naverService (OAuth2User user, String registrationId) {

        Map<String, Object> response = user.getAttribute("response");
        assert response != null;
        String username = (String) response.get("id");
        String password = "";
        String nickname = registrationId + "_" + (String) response.get("nickname");
        String email = (String) response.get("email");

        return new MemberByOAuth2(username, password, nickname, email);
    }

    public MemberByOAuth2 kakaoService (OAuth2User user, String registrationId) {
        Map<String, Object> kakaoAccount = user.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String username = user.getAttribute("id").toString();
        String password = "";
        String nickname = registrationId + "_" + (String) profile.get("nickname");
        String email = (String) kakaoAccount.get("email");

        return new MemberByOAuth2(username, password, nickname, email);
    }
}
