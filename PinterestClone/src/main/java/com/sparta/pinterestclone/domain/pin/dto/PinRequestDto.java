package com.sparta.pinterestclone.domain.pin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PinRequestDto {
    MultipartFile image;
    private String title;
    private String content;
}
