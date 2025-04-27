package com.dm4nk.recorder.bootstrap;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.recorder.repository.ActionsRepository;
import com.dm4nk.recorder.repository.ViewsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ViewsRepository viewsRepository;
    private final ActionsRepository actionsRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            viewsRepository.createViewsTable();
            actionsRepository.createActionsTable();
        } catch (ClickHouseException e) {
            throw new RuntimeException(e);
        }
    }
}
