package com.dm4nk.recorder.model;

import lombok.Data;

@Data
public class ViewRequest {
    private String userId;
    private Integer pageVisitedId;
    private Integer views;
}
