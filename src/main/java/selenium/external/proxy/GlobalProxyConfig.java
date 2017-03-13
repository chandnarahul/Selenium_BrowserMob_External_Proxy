package selenium.external.proxy;

import java.util.ArrayList;
import java.util.List;

public class GlobalProxyConfig {
    public static final String CHROME_DRIVER_PATH = "/home/rahulc/Downloads/chrome/chromedriver";
    public static final String PROXY_PATH = "/home/rahulc/Downloads/browsermob-proxy-2.1.4/bin/";

    public static final String LINUX_PROXY = PROXY_PATH + "browsermob-proxy";
    public static final String WIN_PROXY = LINUX_PROXY + ".bat";
    public static final String LINUX_BROWSERMOB_PROXY_PROCESS = "browsermob-proxy";
    public static final String WIN_BROWSERMOB_PROXY_PROCESS = LINUX_BROWSERMOB_PROXY_PROCESS;

    public static final String PROXY_STARTED_INDICATOR = "Started SelectChannelConnector";

    private static final String PROXY_HOST_IP = "127.0.0.1";
    private static final String PROXY_PORT = "8080";
    private static final String PROXY_INIT_URL = "/proxy";
    private static final String PROXY_HOST = "http://" + PROXY_HOST_IP + ":";
    public static final String PROXY_RUNNING_PORT = "8888";

    public static final String PROXY_INIT = PROXY_HOST + PROXY_PORT + PROXY_INIT_URL;
    public static final String PROXY_URL = PROXY_HOST + PROXY_RUNNING_PORT;
    public static final String PROXY_HAR = PROXY_INIT + "/" + PROXY_RUNNING_PORT + "/har";


    public static List<String> chromeOptions() {
        List<String> list = new ArrayList<>();
        list.add("--proxy-server=" + PROXY_URL);
        list.add("--disable-extensions");

        return list;
    }
}
