package com.sparta.pinterestclone.auth.jwt;

import com.sparta.pinterestclone.auth.refreshtoken.entity.RefreshToken;
import com.sparta.pinterestclone.auth.refreshtoken.repository.RefreshTokenRepository;
import com.sparta.pinterestclone.auth.refreshtoken.dto.TokenDto;
import com.sparta.pinterestclone.auth.security.UserDetailsServiceImpl;
import com.sparta.pinterestclone.exception.ApiException;
import com.sparta.pinterestclone.exception.Exception;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.security.SecurityException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailsServiceImpl userDetailsService;
    public static final String REFRESH_TOKEN = "Refresh_Token";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_TIME = 6000000 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request,String type) {
        String bearerToken = type.equals("Access")?request.getHeader(AUTHORIZATION_HEADER) : request.getHeader(REFRESH_TOKEN);
        if(bearerToken == null) {
            return null;
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public TokenDto createAllToken(String email){
        return new TokenDto(createToken(email, "Access"), createToken(email, "Refresh"));
    }
    public String createToken(String username, String type) {
        Date date = new Date();
        long time = type.equals("Access") ? TOKEN_TIME : REFRESH_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + time))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

//      refreshToken 검증 비교
    public Boolean refreshTokenValidation(String token){
//        1차 토큰 검즘
        if(!validateToken(token)) return false;
//        DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findAllByEmail(token);
        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken().substring(7));
    }
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    public String getEmail(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
    public void setHeader(HttpServletResponse response,TokenDto tokenDto){
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenDto.getAuthorization());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefresh_Token());
    }
}
