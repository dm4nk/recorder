package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ActionRequest;
import com.dm4nk.recorder.model.ActionResponse;
import com.dm4nk.recorder.service.ActionsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/actions")
@AllArgsConstructor
public class ActionsControllerImpl implements ActionsController {
    private final ActionsService actionsService;

    @Override
    public ResponseEntity<Void> addAction(ActionRequest request) throws ClickHouseException {
        return actionsService.addAction(request);
    }

    @Override
    public ResponseEntity<Void> removeAction(String userId) throws ClickHouseException {
        return actionsService.removeAction(userId);
    }

    @Override
    public ResponseEntity<List<ActionResponse>> viewActions(String userId) throws ClickHouseException {
        return actionsService.viewActions(userId);
    }

    @Override
    public ResponseEntity<Void> optimize() throws ClickHouseException {
        return actionsService.optimize();
    }
}
