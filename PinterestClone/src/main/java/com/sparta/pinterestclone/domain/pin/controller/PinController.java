package com.sparta.pinterestclone.domain.pin.controller;

import com.sparta.pinterestclone.domain.pin.dto.PinRequestDto;
import com.sparta.pinterestclone.domain.pin.dto.PinResponseDto;
import com.sparta.pinterestclone.domain.pin.service.PinService;
import com.sparta.pinterestclone.utils.dto.MessageDto;
import com.sparta.pinterestclone.config.ApiDocumentResponse;
import com.sparta.pinterestclone.auth.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@ApiDocumentResponse
@Slf4j
public class PinController {
    private final PinService pinService;

    @Operation(summary = "Pin 제목 검색 요청", description = "제목을 검색합니다.", tags = {"Pin"})
    @GetMapping("/pins/search")
    public List<PinResponseDto> search(@RequestParam(value = "keyword") String keyword
            , @RequestParam("page") int page
            , @RequestParam("size") int size
            , Model model) {
        List<PinResponseDto> pinResponseDtoList = pinService.searchPosts(keyword ,page ,size);
        model.addAttribute("pinList", pinResponseDtoList);

        return pinService.searchPosts(keyword, page , size);
    }

    @Operation(summary = "Pin 생성 요청", description = "Pin 추가됩니다.", tags = {"Pin"})
    @PostMapping("/pins/create")
    public MessageDto create(@ModelAttribute PinRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return pinService.create(userDetails.getUser(), requestDto);
    }

    @Operation(summary = "Pin 전체 조회 요청", description = "Pin을 모두 조회합니다.", tags = {"Pin"})
    @GetMapping("/pins")
    public List<PinResponseDto> getPostList(@RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size){
        List<PinResponseDto> resultList = pinService.getPintList(page, size);
        return resultList;
    }

    @Operation(summary = "Pin 상세 페이지 요청", description = "Pin Id를 통해 상세 페이지를 조회 합니다.", tags = {"Pin"})
    @GetMapping("pins/{pinId}")
    public PinResponseDto getIdPin(@PathVariable Long pinId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return pinService.getIdPin(pinId, userDetails.getUser());

    }


    @Operation(summary = "Pin 수정 요청", description = "Pin Id를 통해 Pin을 수정 합니다.", tags = {"Pin"})
    @PatchMapping("/pins/{pinId}")
    public ResponseEntity<MessageDto> updatepins(@PathVariable Long pinId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @ModelAttribute PinRequestDto pinRequestDto) throws Exception {

        return pinService.update(userDetails.getUser(), pinId, pinRequestDto);
    }

    @Operation(summary = "Pin 삭제 요청", description = "Pin Id를 통해 Pin을 삭제합니다.", tags = {"Pin"})
    @DeleteMapping("/pins/{pinId}")
    public MessageDto delete(@PathVariable Long pinId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pinService.delete(userDetails.getUser(), pinId);
        return new MessageDto("핀 삭제 성공", HttpStatus.OK);
    }


}
