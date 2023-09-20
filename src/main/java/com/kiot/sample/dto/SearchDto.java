package com.kiot.sample.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchDto {
    private String searchType;
    private String keyword;
}
