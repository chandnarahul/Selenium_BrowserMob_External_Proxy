package selenium.external.proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ExternalProxyHandlerTest {
    private ExternalProxyHandler externalProxyHandler = null;
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        this.externalProxyHandler = new ExternalProxyHandler();
        this.externalProxyHandler.start();
        setUpDriver();
    }

    @Test
    public void should_load_proxy_bat() throws Exception {
        externalProxyHandler.resetHar();

        driver.navigate().to("https://www.twitter.com");

        externalProxyHandler.writeHarToFIle("op_1.txt");
        externalProxyHandler.writeSubHarToFIle("op_2.txt", "request", "https://analytics.twitter.com", "response", "partner_id");

        assertTrue(new File("op_2.txt").exists());
    }

    private void setUpDriver() throws Exception {
        System.setProperty("webdriver.chrome.driver", GlobalProxyConfig.CHROME_DRIVER_PATH);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setSslProxy(GlobalProxyConfig.proxyURL());
        proxy.setHttpProxy(GlobalProxyConfig.proxyURL());
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        driver = new ChromeDriver(capabilities);
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