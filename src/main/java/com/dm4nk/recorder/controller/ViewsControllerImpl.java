package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.model.ViewRequest;
import com.dm4nk.recorder.model.ViewResponse;
import com.dm4nk.recorder.service.ViewsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/views")
@AllArgsConstructor
public class ViewsControllerImpl implements ViewsController {
    private final ViewsServiceImpl viewsService;

    @Override
    public ResponseEntity<Void> addView(ViewRequest viewRequest
    ) throws ClickHouseException {
        return viewsService.addView(viewRequest);
    }

    @Override
    public ResponseEntity<Void> removeView(String userId
    ) throws ClickHouseException {
        return viewsService.removeView(userId);
    }

    @Override
    public ResponseEntity<List<ViewResponse>> viewViews(String userId) throws ClickHouseException {
        return viewsService.viewViews(userId);
    }

    @Override
    public ResponseEntity<Void> optimize() throws ClickHouseException {
        return viewsService.optimize();
    }
}
