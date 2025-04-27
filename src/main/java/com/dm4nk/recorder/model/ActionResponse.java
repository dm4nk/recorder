package com.dm4nk.recorder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Schema(description = "Ответ с данными о действии пользователя")
public class ActionResponse {

    @Schema(description = "Наименование действия пользователя", example = "login")
    String action;

    @Schema(description = "Дата и время выполнения действия", example = "2024-04-27T14:30:00")
    LocalDateTime eventTime;
}

