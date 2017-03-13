package selenium.external.proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ExternalProxyHandlerTest {
    private ExternalProxyHandler externalProxyHandler = new ExternalProxyHandler();
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        this.externalProxyHandler.start();
        setUpDriver();
    }

    @Test
    public void should_load_proxy_bat() throws Exception {
        externalProxyHandler.resetHar();

        String webSite = "https://www.twitter.com";

        driver.navigate().to(webSite);

        externalProxyHandler.harToFile("op_1.txt");
        String searchParam = "partner_id";
        externalProxyHandler.subHarToFile("op_2.txt", "request", "analytics.twitter.com/tpm/p?_=", "response", searchParam);

        assertTrue(externalProxyHandler.isSubHarContains("request", "analytics.twitter.com/tpm/p?_=", "response", searchParam));

        assertTrue(isFileContains("op_2.txt", searchParam));
    }

    private boolean isFileContains(String fileName, String check) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String sCurrentLine;

        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            if (sCurrentLine.contains(check)) {
                return true;
            }
        }
        return false;
    }

    private void setUpDriver() throws Exception {
        System.setProperty("webdriver.chrome.driver", GlobalProxyConfig.CHROME_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--proxy-server=" + GlobalProxyConfig.proxyURL());
        driver = new ChromeDriver(chromeOptions);
    }

    @After
    public void destroy() throws Exception {
        this.externalProxyHandler.stop();
        if (driver != null) {
            System.out.println("Closing chrome browser");
            driver.quit();
        }
    }
}