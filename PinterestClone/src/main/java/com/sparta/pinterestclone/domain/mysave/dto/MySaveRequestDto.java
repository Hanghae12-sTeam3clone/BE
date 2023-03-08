package com.sparta.pinterestclone.domain.mysave.dto;

import com.sparta.pinterestclone.domain.mysave.entity.MySave;
import lombok.Getter;

import java.util.List;

@Getter
public class MySaveRequestDto {
    private List<MySave> mySaveList;
}
