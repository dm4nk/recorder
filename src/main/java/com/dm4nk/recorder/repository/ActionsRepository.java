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
import com.clickhouse.data.ClickHouseValues;
import com.clickhouse.data.format.BinaryStreamUtils;
import com.dm4nk.recorder.constants.Constants;
import com.dm4nk.recorder.model.ActionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ActionsRepository {

    private final ClickHouseNode server;

    public void dropAndCreateActionsTable() throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest<?> request = client.read(server);
            request.query(Constants.DROP_TABLE_SQL)
                    .params(Map.of("tableName", Constants.DROP_TABLE_SQL))
                    .execute().get();
            request.query(Constants.CREATE_ACTIONS_TABLE)
                    .execute().get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public void insertActionEvent(String userId, String action) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest.Mutation request = client.read(server).write().table(Constants.ACTIONS_TABLE).format(ClickHouseFormat.RowBinary);
            ClickHouseConfig config = request.getConfig();
            CompletableFuture<ClickHouseResponse> future;
            try (ClickHousePipedOutputStream stream = ClickHouseDataStreamFactory.getInstance().createPipedOutputStream(config, (Runnable) null)) {
                future = request.data(stream.getInputStream()).execute();
                BinaryStreamUtils.writeString(stream, userId);
                BinaryStreamUtils.writeString(stream, action);
                BinaryStreamUtils.writeDateTime(stream, LocalDateTime.now(), TimeZone.getDefault());
            }

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

    public List<ActionDTO> viewActionsForUser(String userId) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol());
             ClickHouseResponse response = client.read(server)
                     .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                     .query(Constants.VIEW_ACTIONS_SQL)
                     .params(Map.of("tableName", Constants.ACTIONS_TABLE, "userId", ClickHouseValues.convertToSqlExpression(userId)))
                     .execute()
                     .get()) {
            return StreamSupport.stream(response.records().spliterator(), false)
                    .map(r -> new ActionDTO(r.getValue("action").asString(), r.getValue("event_time").asDateTime()))
                    .toList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public void deleteActionsForUser(String userId) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol())) {
            ClickHouseRequest<?> request = client.read(server);
            request.query(Constants.DELETE_BY_USER_NAME_SQL)
                    .params(Map.of(
                            "tableName", Constants.ACTIONS_TABLE,
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
            ClickHouseRequest.Mutation request = client.read(server).write().table(Constants.ACTIONS_TABLE).format(ClickHouseFormat.RowBinary);
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
