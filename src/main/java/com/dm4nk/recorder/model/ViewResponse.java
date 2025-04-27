package com.dm4nk.recorder.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Ответ с информацией о просмотрах страницы пользователем")
public class ViewResponse {

    @Schema(description = "Идентификатор посещённой страницы", example = "678")
    private String pageVisitedId;

    @Schema(description = "Количество просмотров страницы", example = "3")
    private Integer views;
}

