package com.dm4nk.recorder.service;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.constants.Constants;
import com.dm4nk.recorder.model.ViewDTO;
import com.dm4nk.recorder.model.ViewRequest;
import com.dm4nk.recorder.model.ViewResponse;
import com.dm4nk.recorder.repository.ViewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Service
@AllArgsConstructor
public class ViewsService {
    private final ViewsRepository viewsRepository;

    public ResponseEntity<Void> addView(ViewRequest viewRequest) throws ClickHouseException {
        viewsRepository.insertViewEvent(viewRequest.getUserId(), viewRequest.getPageVisitedId(), viewRequest.getViews());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> removeView(String userId) throws ClickHouseException {
        viewsRepository.deleteViewsForUser(userName);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<ViewResponse>> viewViews(String userId) throws ClickHouseException {
        List<ViewDTO> views = viewsRepository.viewViewsForUser(userId);

        return ResponseEntity.ok();
    }

    @PostMapping("/optimize")
    public ResponseEntity<Void> optimize() throws ClickHouseException {
        viewsRepository.optimizeTable(Constants.VIEWS_TABLE);
        return ResponseEntity.ok().build();
    }
}
