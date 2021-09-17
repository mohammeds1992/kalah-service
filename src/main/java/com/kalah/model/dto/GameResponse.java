package com.kalah.model.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameResponse {

    private final String id;
    
    private final String url;
    
    private final Map<Integer, String> status;
}