package com.sparta.pinterestclone.domain.user.service;

import com.sparta.pinterestclone.domain.user.dto.LoginRequestDto;
import com.sparta.pinterestclone.domain.user.dto.SignupRequestDto;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.domain.user.repository.UserRepository;
import com.sparta.pinterestclone.dto.MessageDto;
import com.sparta.pinterestclone.exception.ApiException;
import com.sparta.pinterestclone.exception.Exception;
import com.sparta.pinterestclone.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.pinterestclone.exception.Exception.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional(readOnly = true)
    public ResponseEntity<MessageDto> login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ApiException(NOT_FOUND_USER);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new ApiException(INVALID_PASSWORD);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getEmail(), user.get().getRole()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(new MessageDto("로그인 성공", HttpStatus.OK));
    }

    public ResponseEntity<MessageDto> signup(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();

        Optional<User> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            throw new ApiException(DUPLICATED_EMAIL);
        }

        Optional<User> foundNickname = userRepository.findByNickname(nickname);
        if (foundNickname.isPresent()) {
            throw new ApiException(DUPLICATED_NICKNAME);
        }

        UserRoleEnum role = UserRoleEnum.USER;

        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 토큰이 존재하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(email, password, nickname, role);
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(new MessageDto("회원가입 완료", HttpStatus.OK));
    }

}
