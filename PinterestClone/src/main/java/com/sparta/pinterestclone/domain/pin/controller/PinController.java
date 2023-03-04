package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinService;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @PostMapping("/pins/create")
    public MessageResponseDto create(@ModelAttribute PinRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException{
        return pinService.create(userDetails.getUser(),requestDto);
    }

    @GetMapping("/pins")
    public List<PinResponseDto> getBoards() {
        return pinService.getPins();
    }

}
