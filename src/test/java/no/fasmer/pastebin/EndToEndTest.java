package no.fasmer.pastebin;

import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {

    static WebDriver webDriver;

    @LocalServerPort
    int port;

    @BeforeClass
    public static void setUp() throws IOException {
        webDriver = new HtmlUnitDriver();
    }

    @AfterClass
    public static void tearDown() {
        webDriver.close();
    }

    @Test
    public void homePageShouldWork() throws IOException {
        webDriver.get("http://localhost:" + port);

        assertThat(webDriver.getTitle()).isEqualTo("Pastebin");

        final String pageContent = webDriver.getPageSource();

        assertThat(pageContent).contains("Expiration");
    }
}
