package com.dm4nk.recorder.service;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.constants.Constants;
import com.dm4nk.recorder.mappers.ActionsMapper;
import com.dm4nk.recorder.model.ActionDTO;
import com.dm4nk.recorder.model.ActionRequest;
import com.dm4nk.recorder.model.ActionResponse;
import com.dm4nk.recorder.repository.ActionsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ActionsService {
    private final ActionsRepository actionsRepository;
    private final ActionsMapper actionsMapper;

    public ResponseEntity<Void> addAction(ActionRequest action) throws ClickHouseException {
        actionsRepository.insertActionEvent(action.getUserId(), action.getAction());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> removeAction(String userId) throws ClickHouseException {
        actionsRepository.deleteActionsForUser(userId);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<ActionResponse>> viewActions(String userId) throws ClickHouseException {
        List<ActionDTO> actions = actionsRepository.viewActionsForUser(userId);
        List<ActionResponse> actionResponses = actionsMapper.toActionResponses(actions);
        return ResponseEntity.ok(actionResponses);
    }

    public ResponseEntity<Void> optimize() throws ClickHouseException {
        actionsRepository.optimizeTable(Constants.ACTIONS_TABLE);
        return ResponseEntity.ok().build();
    }
}
