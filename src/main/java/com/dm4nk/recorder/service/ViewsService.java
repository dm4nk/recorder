package com.dm4nk.recorder.service;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ViewRequest;
import com.dm4nk.recorder.model.ViewResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ViewsService {
    ResponseEntity<Void> addView(ViewRequest viewRequest) throws ClickHouseException;

    ResponseEntity<Void> removeView(String userId) throws ClickHouseException;

    ResponseEntity<List<ViewResponse>> viewViews(String userId) throws ClickHouseException;

    ResponseEntity<Void> optimize() throws ClickHouseException;
}
