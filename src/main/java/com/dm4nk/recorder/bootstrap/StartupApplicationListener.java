package com.dm4nk.recorder.bootstrap;

import com.clickhouse.client.ClickHouseException;
import com.dm4nk.unidbclickhouse.repository.ClicksRepository;
import com.dm4nk.unidbclickhouse.repository.ViewsRepository;
import com.dm4nk.unidbclickhouse.repository.VisitsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import static com.dm4nk.unidbclickhouse.constants.Constants.VISIT_TABLE;

@Slf4j
@AllArgsConstructor
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final VisitsRepository visitsRepository;
    private final ViewsRepository viewsRepository;
    private final ClicksRepository clicksRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            visitsRepository.dropAndCreateVisitsTable();
            visitsRepository.insertVisitEvent("user1", "/main");
            visitsRepository.insertVisitEvent("user1", "/not_main");
            visitsRepository.insertVisitEvent("user1", "/main");
            visitsRepository.insertVisitEvent("user1", "/main");
            visitsRepository.insertVisitEvent("user1", "/not_main");
            visitsRepository.optimizeTable(VISIT_TABLE);

            log.debug(visitsRepository.viewVisitsForUser("user1").toString());

            viewsRepository.dropAndCreateViewsTable();
            viewsRepository.insertViewEvent("user1", 1, 4);
            viewsRepository.insertViewEvent("user2", 2, 19);
            viewsRepository.insertViewEvent("user1", 1, 6);
            viewsRepository.insertViewEvent("user1", 1, 8);
            viewsRepository.insertViewEvent("user1", 1, 10);
            viewsRepository.insertViewEvent("user3", 3, 100);
            viewsRepository.insertViewEvent("user1", 1, 12);
            viewsRepository.insertViewEvent("user2", 2, 21);
            viewsRepository.optimizeTable(VISIT_TABLE);

            log.debug(viewsRepository.viewViewsForUser("user1").toString());

            clicksRepository.dropAndCreateClicksTable();
        } catch (ClickHouseException e) {
            throw new RuntimeException(e);
        }
    }
}
