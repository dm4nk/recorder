package com.dm4nk.recorder.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewResponse {
    private String pageVisitedId;
    private Integer views;
}
