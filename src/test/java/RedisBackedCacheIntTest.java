import io.gumichan01.containerdb.RedisBackedCache;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedisBackedCacheIntTest {
    private Logger logger = LoggerFactory.getLogger(RedisBackedCacheIntTest.class);
    private RedisBackedCache db;
    @Rule
    public GenericContainer redis = new GenericContainer("redis:5.0.3-alpine")
            .withExposedPorts(6379);


    @Before
    public void setUp() {
        String ipAddress = redis.getContainerIpAddress();
        Integer port = redis.getFirstMappedPort();
        logger.info("get database: @" + ipAddress + "/" + port);
        db = new RedisBackedCache(ipAddress, port);
    }

    @Test
    public void testSimplePutAndGet() {
        db.put("test", "example");

        Optional<String> retrieved = db.get("test", String.class);
        logger.info("get value: " + retrieved);
        assertTrue(retrieved.isPresent());
        assertEquals("example", retrieved.get());
    }
}
