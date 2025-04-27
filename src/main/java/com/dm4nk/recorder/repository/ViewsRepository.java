package com.dm4nk.recorder.repository;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseConfig;
import com.clickhouse.client.ClickHouseException;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseRequest;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.client.ClickHouseResponseSummary;
import com.clickhouse.data.ClickHouseDataStreamFactory;
import com.clickhouse.data.ClickHouseFormat;
import com.clickhouse.data.ClickHousePipedOutputStream;
import com.clickhouse.data.ClickHouseRecord;
import com.clickhouse.data.ClickHouseValues;
import com.clickhouse.data.format.BinaryStreamUtils;
import com.dm4nk.recorder.constants.Constants;
import com.dm4nk.recorder.model.ViewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

import static com.dm4nk.recorder.constants.Constants.VIEWS_TABLE;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ViewsRepository {

    private final ClickHouseNode server;

    public void dropAndCreateViewsTable() throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest<?> request = client.read(server);
            request.query(Constants.DROP_TABLE_SQL)
                    .params(Map.of("tableName", VIEWS_TABLE))
                    .execute().get();
            request.query(Constants.CREATE_VIEWS_TABLE)
                    .execute().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public void insertViewEvent(String userName, int pageVisitedId, int views) throws ClickHouseException {
        Integer previousRecordViews = viewViewsForUserAndPageId(userName, pageVisitedId);
        log.debug("insertViewEvent for user: {}, pageVisitedId: {} = {}", userName, pageVisitedId, previousRecordViews);
        if (Objects.nonNull(previousRecordViews)) {
            insertViewEvent(userName, pageVisitedId, previousRecordViews, -1);
        }
        insertViewEvent(userName, pageVisitedId, views, 1);
    }

    private void insertViewEvent(String userName, int pageVisitedId, int views, int sign) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest.Mutation request = client.read(server).write().table(VIEWS_TABLE).format(ClickHouseFormat.RowBinary);
            ClickHouseConfig config = request.getConfig();
            CompletableFuture<ClickHouseResponse> future;
            // back-pressuring is not supported, you can adjust the first two arguments
            try (ClickHousePipedOutputStream stream = ClickHouseDataStreamFactory.getInstance().createPipedOutputStream(config, (Runnable) null)) {
                // in async mode, which is default, execution happens in a worker thread
                future = request.data(stream.getInputStream()).execute();

                BinaryStreamUtils.writeString(stream, userName);
                BinaryStreamUtils.writeInt8(stream, pageVisitedId);
                BinaryStreamUtils.writeInt8(stream, views);
                BinaryStreamUtils.writeInt8(stream, sign);
            }

            // response should be always closed
            try (ClickHouseResponse response = future.get()) {
                ClickHouseResponseSummary summary = response.getSummary();
                summary.getWrittenRows();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException | IOException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    private Integer viewViewsForUserAndPageId(String userId, int pageVisitedId) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol());
             ClickHouseResponse response = client.read(server)
                     .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                     .query(Constants.VIEW_VIEWS_BY_PAGE_ID_SQL)
                     .params(Map.of(
                             "tableName", VIEWS_TABLE,
                             "userId", ClickHouseValues.convertToSqlExpression(userId),
                             "pageVisitedId", ClickHouseValues.convertToSqlExpression(pageVisitedId)))
                     .execute()
                     .get()) {

            ClickHouseRecord views = StreamSupport.stream(response.records().spliterator(), false)
                    .findAny()
                    .orElse(null);
            return views == null ? null : views.getValue("views").asInteger();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public List<ViewDTO> viewViewsForUser(String userId) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol());
             ClickHouseResponse response = client.read(server)
                     .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                     .query(Constants.VIEW_VIEWS_SQL)
                     .params(Map.of("tableName", VIEWS_TABLE, "userId", ClickHouseValues.convertToSqlExpression(userId)))
                     .execute()
                     .get()) {

            return StreamSupport.stream(response.records().spliterator(), false)
                    .map(r -> new ViewDTO(r.getValue("page_visited_id").asString(), r.getValue("views").asInteger()))
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public void deleteViewsForUser(String userId) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest<?> request = client.read(server);
            request.query(Constants.DELETE_BY_USER_NAME_SQL)
                    .params(Map.of(
                            "tableName", VIEWS_TABLE,
                            "userId", ClickHouseValues.convertToSqlExpression(userId)))
                    .execute()
                    .get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public void optimizeTable(String tableName) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest.Mutation request = client.read(server).write().table(VIEWS_TABLE).format(ClickHouseFormat.RowBinary);
            request.query(Constants.OPTIMIZE_TABLE)
                    .params(Map.of("tableName", tableName))
                    .execute().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }
}
