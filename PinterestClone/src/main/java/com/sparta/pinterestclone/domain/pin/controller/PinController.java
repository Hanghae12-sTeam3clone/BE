package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.ResponseDto.MessageResponseDto;
import com.sparta.pinterestclone.domain.pin.RequestDto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.ResponseDto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinService;
import com.sparta.pinterestclone.respoinse.ApiDocumentResponse;
import com.sparta.pinterestclone.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@ApiDocumentResponse
public class PinController {
    private final PinService pinService;

    @Operation(summary = "Pin 제목 검색 요청", description = "제목을 검색합니다.", tags = { "Pin" })
    @GetMapping("/pins/search")
    public List<PinResponseDto> search(@RequestParam(value = "keyword")String keyword, Model model){
        List<PinResponseDto> pinResponseDtoList = pinService.searchPosts(keyword);
        model.addAttribute("pinList",pinResponseDtoList);

        return pinService.searchPosts(keyword);
    }

    @Operation(summary = "Pin 생성 요청", description = "Pin 추가됩니다.", tags = { "Pin" })
    @PostMapping("/pins/create")
    public MessageResponseDto create(@ModelAttribute PinRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails)throws IOException{
        return pinService.create(userDetails.getUser(),requestDto);
    }
    @Operation(summary = "Pin 전체 조회 요청", description = "Pin을 모두 조회합니다.", tags = { "Pin" })
    @GetMapping("/pins")
    public List<PinResponseDto> getBoards() {
        return pinService.getPins();
    }
    @Operation(summary = "Pin 상세 페이지 요청", description = "Pin Id를 통해 상세 페이지를 조회 합니다.", tags = { "Pin" })
    @GetMapping("pins/{pinId}")
    public PinResponseDto getIdPin(@PathVariable Long pinId) {
        return pinService.getIdPin(pinId);
    }

    @Operation(summary = "Pin 수정 요청", description = "Pin Id를 통해 Pin을 수정 합니다.", tags = { "Pin" })
    @PatchMapping("/pins/{pinId}")
    public MessageResponseDto updatepins(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @ModelAttribute PinRequestDto pinRequestDto)throws IOException{
        return pinService.update(userDetails.getUser(),id,pinRequestDto);
    }
    @Operation(summary = "Pin 삭제 요청", description = "Pin Id를 통해 Pin을 삭제합니다.", tags = { "Pin" })
    @DeleteMapping("/pins/{pinId}")
    public MessageResponseDto delete(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        return pinService.delete(userDetails.getUser(),id);
    }


}
