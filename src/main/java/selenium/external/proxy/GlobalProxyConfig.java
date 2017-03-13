package selenium.external.proxy;

public class GlobalProxyConfig {
    public static final String PROXY_PATH = "/home/rahulc/Downloads/browsermob-proxy-2.1.4/bin/";
    public static final String LINUX_PROXY = PROXY_PATH + "browsermob-proxy";
    public static final String WIN_PROXY = PROXY_PATH + "browsermob-proxy.bat";
    public static final String LINUX_BROWSERMOB_PROXY_PROCESS = "browsermob-proxy";
    public static final String WIN_BROWSERMOB_PROXY_PROCESS = "browsermob-proxy";
    private static final String PROXY_HOST = "127.0.0.1";
    private static final String PROXY_PORT = "8080";
    private static final String PROXY_INIT = "/proxy";
    public static final String PROXY_RUNNING_PORT = "8888";
    public static final String CHROME_DRIVER_PATH = "/home/rahulc/Downloads/chrome/chromedriver";
    public static final String PROXY_PAGE_REFERENCE = "SeleniumTest";
    public static final String PROXY_STARTED_INDICATOR = "Started SelectChannelConnector";

    public static String proxyInit() {
        return "http://" + PROXY_HOST + ":" + PROXY_PORT + PROXY_INIT;
    }

    public static String proxyURL() {
        return "http://" + PROXY_HOST + ":" + PROXY_RUNNING_PORT;
    }

    public static String harURL() {
        return proxyInit() + "/" + PROXY_RUNNING_PORT + "/har";
    }

}
