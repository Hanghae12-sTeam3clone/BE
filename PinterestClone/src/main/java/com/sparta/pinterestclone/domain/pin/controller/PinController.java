package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinService;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PinController {
    private final PinService pinService;

    @GetMapping("/pins/search")
    public List<PinResponseDto> search(@RequestParam(value = "keyword")String keyword, Model model){
        List<PinResponseDto> pinResponseDtoList = pinService.searchPosts(keyword);
        model.addAttribute("pinList",pinResponseDtoList);

        return pinService.searchPosts(keyword);
    }


    @PostMapping("/pins/create")
    public MessageResponseDto create(@ModelAttribute PinRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException{
        return pinService.create(userDetails.getUser(),requestDto);
    }

    @GetMapping("/pins")
    public List<PinResponseDto> getBoards() {
        return pinService.getPins();
    }

    @PatchMapping("/pins/{id}")
    public MessageResponseDto updatepins(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @ModelAttribute PinRequestDto pinRequestDto)throws IOException{
        return pinService.update(userDetails.getUser(),id,pinRequestDto);
    }
    @DeleteMapping("/pins/{id}")
    public MessageResponseDto delete(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinService.delete(userDetails.getUser(),id);
    }


}
