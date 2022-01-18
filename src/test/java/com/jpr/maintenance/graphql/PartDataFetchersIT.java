package com.jpr.maintenance.graphql;

import com.jpr.maintenance.AbstractIntegrationTest;
import com.jpr.maintenance.database.model.PartEntity;
import com.jpr.maintenance.database.repository.PartRepository;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class PartDataFetchersIT extends AbstractIntegrationTest {
    @Autowired
    private DataFetcherWrapper<PartEntity> partDataFetcher;

    @Autowired
    private PartRepository partRepository;

    @BeforeAll
    void init() {
        partRepository.deleteAll().block();
    }

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
