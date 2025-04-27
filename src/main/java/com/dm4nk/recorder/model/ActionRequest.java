package com.dm4nk.recorder.model;

import lombok.Value;

@Value
public class ActionRequest {
    String userId;
    String action;
}
