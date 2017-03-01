package selenium.external.proxy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.assertTrue;

public class ExternalProxyHandlerTest {
    private ExternalProxyHandler externalProxyHandler = null;
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
        this.externalProxyHandler = new ExternalProxyHandler();
        this.externalProxyHandler.start();
    }

    @Test
    public void should_load_proxy_bat() throws Exception {

        System.setProperty("webdriver.chrome.driver", GlobalProxyConfig.CHROME_DRIVER_PATH);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setSslProxy(GlobalProxyConfig.proxyURL());
        proxy.setHttpProxy(GlobalProxyConfig.proxyURL());
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        driver = new ChromeDriver(capabilities);
        externalProxyHandler.resetHar();
        driver.navigate().to("http://www.trainman.in");
        String har = externalProxyHandler.getSubHar("request", "http://googleads.g.doubleclick.net/pagead/viewthroughconversion/959180864/?value=0&guid=ON&script=0", "response", "http://www.google.com/ads/user-lists/959180864");
        System.out.println(har);
        assertTrue(har.contains("959180864"));
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