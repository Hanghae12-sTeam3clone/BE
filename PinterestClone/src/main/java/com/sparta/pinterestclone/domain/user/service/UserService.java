package com.sparta.pinterestclone.domain.user.service;

import com.sparta.pinterestclone.auth.refreshtoken.dto.ApiResponseDto;
import com.sparta.pinterestclone.auth.refreshtoken.dto.TokenDto;
import com.sparta.pinterestclone.auth.refreshtoken.entity.RefreshToken;
import com.sparta.pinterestclone.auth.refreshtoken.repository.RefreshTokenRepository;
import com.sparta.pinterestclone.auth.refreshtoken.response.ResponseUtils;
import com.sparta.pinterestclone.auth.refreshtoken.response.SuccessResponse;
import com.sparta.pinterestclone.domain.user.dto.LoginRequestDto;
import com.sparta.pinterestclone.domain.user.dto.LoginResponseDto;
import com.sparta.pinterestclone.domain.user.dto.SignupRequestDto;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.domain.user.entity.UserRoleEnum;
import com.sparta.pinterestclone.domain.user.repository.UserRepository;
import com.sparta.pinterestclone.exception.Exception;
import com.sparta.pinterestclone.utils.dto.MessageDto;
import com.sparta.pinterestclone.exception.ApiException;
import com.sparta.pinterestclone.auth.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.sparta.pinterestclone.exception.Exception.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional(readOnly = true)
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ApiException(NOT_FOUND_USER);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new ApiException(INVALID_PASSWORD);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(email);

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findAllByEmail(email);

        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefresh_Token()));
        }else{
            RefreshToken newToken = new RefreshToken(tokenDto.getRefresh_Token(), email);
            refreshTokenRepository.save(newToken);
        }
        jwtUtil.setHeader(response, tokenDto);

        return ResponseEntity.ok()
                .body(new LoginResponseDto("로그인 성공", HttpStatus.OK.value(), user.get().getNickname()));
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

//         토큰 갱신 확인
    public ApiResponseDto<SuccessResponse> issueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.resolveToken(request,"Refresh");
        if(!jwtUtil.refreshTokenValidation(refreshToken)){
            throw new ApiException(JWT_EXPIRED_TOKEN);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(jwtUtil.getEmail(refreshToken),"Access"));
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "갱신 성공"));
    }
}
