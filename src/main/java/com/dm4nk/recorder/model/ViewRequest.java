package com.dm4nk.recorder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление информации о просмотре страницы пользователем")
public class ViewRequest {

    @Schema(description = "Идентификатор пользователя", example = "12345")
    private String userId;

    @Schema(description = "Идентификатор посещённой страницы", example = "678")
    private String pageVisitedId;

    @Schema(description = "Количество просмотров страницы", example = "3")
    private Integer views;
}

