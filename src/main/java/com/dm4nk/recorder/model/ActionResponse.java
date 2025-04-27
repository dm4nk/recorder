package com.dm4nk.recorder.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ActionResponse {
    String action;
    LocalDateTime eventTime;
}
