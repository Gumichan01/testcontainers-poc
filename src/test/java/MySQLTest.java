import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

import static junit.framework.TestCase.assertTrue;

public class MySQLTest {

    private static Logger logger = LoggerFactory.getLogger(MySQLTest.class);
    private static DataSource dataSource;
    @ClassRule
    public static MySQLContainer mysql = new MySQLContainer("mysql:8.0.18");

    @BeforeClass
    public static void init() throws InterruptedException {
        //You can also use the GenericContainer for arbitrary containers
        //But there are convenient classes for common databases.
        mysql.start();
        logger.info("address - {}", mysql.getContainerIpAddress());
        logger.info("user - {}", mysql.getUsername());
        logger.info("password - {}", mysql.getPassword());
        dataSource = DataSourceBuilder.create()
                .url(mysql.getContainerIpAddress())
                .username(mysql.getUsername())
                .password(mysql.getPassword())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @AfterClass
    public static void destroy() {
        mysql.close();
    }

    @Test
    public void testTableCreation() {
        //use the database
        logger.info("OK");
        assertTrue(true);
    }
}