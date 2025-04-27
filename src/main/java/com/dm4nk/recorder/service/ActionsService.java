package com.dm4nk.recorder.service;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ActionRequest;
import com.dm4nk.recorder.model.ActionResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ActionsService {
    ResponseEntity<Void> addAction(ActionRequest action) throws ClickHouseException;

    ResponseEntity<Void> removeAction(String userId) throws ClickHouseException;

    ResponseEntity<List<ActionResponse>> viewActions(String userId) throws ClickHouseException;

    ResponseEntity<Void> optimize() throws ClickHouseException;
}
