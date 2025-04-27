package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ActionRequest;
import com.dm4nk.recorder.model.ActionResponse;
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

public interface ActionsController {
    @Operation(summary = "Добавить действие", description = "Добавляет новое действие для пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Действие успешно добавлено"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @PostMapping
    ResponseEntity<Void> addAction(@RequestBody @Parameter(description = "Запрос на добавление действия", required = true,
            content = @Content(schema = @Schema(implementation = ActionRequest.class),
                    examples = @ExampleObject(value = """
                            {
                              "userId": "12345",
                              "action": "login"
                            }
                            """)
            )
    ) ActionRequest request) throws ClickHouseException;

    @Operation(summary = "Удалить действия пользователя", description = "Удаляет все действия по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Действия успешно удалены"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @DeleteMapping
    ResponseEntity<Void> removeAction(@RequestParam @Parameter(description = "ID пользователя")
                                      String userId) throws ClickHouseException;

    @Operation(summary = "Посмотреть действия пользователя", description = "Возвращает список действий по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Действия успешно получены",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActionResponse.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "action": "login",
                                        "eventTime": "2024-04-27T14:30:00"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ActionResponse>> viewActions(@RequestParam @Parameter(description = "ID пользователя")
                                                     String userId) throws ClickHouseException;

    @Operation(summary = "Оптимизировать таблицу действий", description = "Выполняет операцию оптимизации хранения действий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Оптимизация успешно выполнена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера", content = @Content)
    })
    @PostMapping("/optimize")
    ResponseEntity<Void> optimize() throws ClickHouseException;
}
