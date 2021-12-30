package com.jpr.maintenance.graphql;

import com.jpr.maintenance.AbstractIntegrationTest;
import com.jpr.maintenance.database.model.PartEntity;
import graphql.execution.DataFetcherResult;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.jpr.maintenance.model.Brand.DUCATI;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PartDataFetchersIT extends AbstractIntegrationTest {
    @Autowired
    private DataFetcherWrapper<CompletableFuture<DataFetcherResult<PartEntity>>> partDataFetcher;

    @Test
    void shouldSavePart() throws Exception {
        Map<String, Object> map = Map.of(
            "brand", "DID",
            "partNr", "525VXGB-120",
            "description", "Gold X-Ring Chain with Connecting Link"
        );
        var environment = new DataFetchingEnvironmentImpl.Builder()
            .arguments(Map.of("partInput", map))
            .build();

        var dataFetcher = partDataFetcher.getDataFetcher();
        var dataFetcherResult = dataFetcher.get(environment).get(1L, TimeUnit.SECONDS);
        var resultData = dataFetcherResult.getData();

        assertEquals(1L, resultData.getId());
        assertEquals("DID", resultData.getBrand());
        assertEquals("525VXGB-120", resultData.getPartNr());
        assertEquals("Gold X-Ring Chain with Connecting Link", resultData.getDescription());
    }
}
