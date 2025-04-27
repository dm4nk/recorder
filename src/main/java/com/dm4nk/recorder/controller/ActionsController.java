package com.dm4nk.recorder.controller;

import com.clickhouse.client.ClickHouseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/visits")
@AllArgsConstructor
public class ActionsController {
    private final VisitsRepository visitsRepository;

    @PostMapping
    public void addClick(@RequestBody VisitRequest visit) throws ClickHouseException {
        visitsRepository.insertVisitEvent(visit.getUserName(), visit.getPageVisited());
    }

    @DeleteMapping
    public void removeVisit(@RequestParam String userName) throws ClickHouseException {
        visitsRepository.deleteVisitsForUser(userName);
    }

    @GetMapping
    public ResponseEntity<List<VisitResponse>> viewVisits(@RequestParam String userName) throws ClickHouseException {
        return ResponseEntity.ok(visitsRepository.viewVisitsForUser(userName));
    }

    @PostMapping("/optimize")
    public void optimize() throws ClickHouseException {
        visitsRepository.optimizeTable(VISIT_TABLE);
    }
}
