package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/views")
@AllArgsConstructor
public class ViewsController {
    private final ViewsRepository viewsRepository;

    @PostMapping
    public void addView(@RequestBody ViewRequest viewRequest) throws ClickHouseException {
        viewsRepository.insertViewEvent(viewRequest.getUserName(), viewRequest.getPageVisitedId(), viewRequest.getViews());
    }

    @PatchMapping
    public void updateView(@RequestParam String userName, @RequestParam Integer views) throws ClickHouseException {
        viewsRepository.updateViewsForUserName(userName, views);
    }

    @DeleteMapping
    public void removeView(@RequestParam String userName) throws ClickHouseException {
        viewsRepository.deleteViewsForUser(userName);
    }

    @GetMapping
    public ResponseEntity<List<ViewResponse>> viewViews(@RequestParam String userName) throws ClickHouseException {
        return ResponseEntity.ok(viewsRepository.viewViewsForUser(userName));
    }

    @PostMapping("/optimize")
    public void optimize() throws ClickHouseException {
        viewsRepository.optimizeTable(VIEWS_TABLE);
    }
}
