package com.sparta.pinterestclone.domain.mysave.service;

import com.sparta.pinterestclone.auth.refreshtoken.response.SuccessResponse;
import com.sparta.pinterestclone.domain.mysave.entity.MySave;
import com.sparta.pinterestclone.domain.mysave.repository.MySaveRepository;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.entity.Pin;
import com.sparta.pinterestclone.domain.pin.repository.PinRepository;
import com.sparta.pinterestclone.domain.user.entity.User;
import com.sparta.pinterestclone.exception.ApiException;
import com.sparta.pinterestclone.exception.Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class MySaveService {

    private final MySaveRepository mySaveRepository;
    private final PinRepository pinRepository;


    //로그인한 회원 마이페이지에 게시물 저장
    @Transactional
    public SuccessResponse getSave(Long pinId, User user) {

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new ApiException(Exception.NOT_FOUND_PIN)
        );

        // 매개변수 pin과 getUser로 MySave에 있는지 확인 후 없다면 repository에 저장
        Optional<MySave> found = mySaveRepository.findByPinAndUser(pin, user);
        if (found.isEmpty()) {
            MySave mySave = MySave.of(user, pin);
            mySaveRepository.save(mySave);
        } else {
            mySaveRepository.delete(found.get()); //null값이 아니라는게 위에서 보증되기 때문에 get() 사용
            mySaveRepository.flush();
        }

        return SuccessResponse.of(HttpStatus.OK,"완료");
        }


    //리스트로 보여주기
    @Transactional
    public List<PinResponseDto> getView(User user){

        List<MySave> mySaveList = mySaveRepository.findByUserId(user.getId());
        List<PinResponseDto> responseDtoList = new ArrayList<>();

        for (MySave mySave : mySaveList) {
            responseDtoList.add(PinResponseDto.of(mySave.getPin()));
        }
        return responseDtoList;
    }
}
