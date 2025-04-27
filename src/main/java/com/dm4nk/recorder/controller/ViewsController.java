package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ViewRequest;
import com.dm4nk.recorder.model.ViewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ViewsController {
    @Operation(summary = "Добавить просмотр", description = "Добавляет новый просмотр для пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Просмотр успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @PostMapping
    ResponseEntity<Void> addView(
            @RequestBody @Parameter(description = "Запрос на добавление просмотра",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ViewRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "userId": "12345",
                                      "pageVisitedId": 678,
                                      "views": 3
                                    }
                                    """)
                    )
            ) ViewRequest viewRequest) throws ClickHouseException;

    @Operation(summary = "Удалить просмотры пользователя", description = "Удаляет все просмотры по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Просмотры успешно удалены"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @DeleteMapping
    ResponseEntity<Void> removeView(
            @RequestParam @Parameter(description = "ID пользователя")
            String userId
    ) throws ClickHouseException;

    @Operation(summary = "Посмотреть просмотры пользователя", description = "Возвращает список просмотров по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Просмотры успешно получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ViewResponse.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "pageVisitedId": 678,
                                        "views": 3,
                                        "eventTime": "2024-04-27T14:30:00"
                                      }
                                    ]
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ViewResponse>> viewViews(
            @RequestParam @Parameter(description = "ID пользователя")
            String userId) throws ClickHouseException;

    @Operation(summary = "Оптимизировать таблицу просмотров", description = "Выполняет операцию оптимизации хранения просмотров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оптимизация успешно выполнена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @PostMapping("/optimize")
    ResponseEntity<Void> optimize() throws ClickHouseException;
}
