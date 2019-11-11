import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MySQLTest {

    private static Logger logger = LoggerFactory.getLogger(MySQLTest.class);
    private static DataSource dataSource;
    private static MySQLContainer mysql;

    @BeforeClass
    public static void init() throws InterruptedException {
        //You can also use the GenericContainer for arbitrary containers
        //But there are convenient classes for common databases.
        mysql = new MySQLContainer("mysql:8.0.18");
        mysql.start();
        final String jdbcUrl = mysql.getJdbcUrl() + "?useSSL=false";
        logger.info("address - {}", jdbcUrl);
        logger.info("user - {}", mysql.getUsername());
        logger.info("password - {}", mysql.getPassword());
        dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
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
        final String expectedName = "hello";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE TABLE BAR (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name varchar(200));");
        jdbcTemplate.execute("INSERT INTO `BAR`(`name`) VALUES (\"" + expectedName + "\");");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT name FROM BAR");
        Optional<String> actualName = maps.stream().map(Map::values)
                .flatMap(Collection::stream)
                .map(o -> (String) o)
                .filter(s -> s.equals(expectedName))
                .findFirst();

        assertTrue(actualName.isPresent());
        assertEquals(actualName.get(), expectedName);
    }
}