package com.dm4nk.recorder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Запрос на добавление действия пользователя")
public class ActionRequest {

    @Schema(description = "Идентификатор пользователя", example = "12345")
    String userId;

    @Schema(description = "Наименование действия пользователя", example = "login")
    String action;
}

