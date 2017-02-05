package selenium.external.proxy;

import selenium.external.proxy.process.ProxyProcessHandler;

public class ExternalProxyHandler {
    private ProxyProcessHandler proxyProcessHandler = new ProxyProcessHandler();
    private ProxyHandler proxyHandler = new ProxyHandler();

    public void start() throws Exception {
        System.out.println("starting external proxyURL");
        proxyProcessHandler.startProcess();
        proxyHandler.startProxy();
    }

    public void stop() throws Exception {
        System.out.println("destroying external proxyURL");
        proxyProcessHandler.killProcess();
    }

    public String getHar() throws Exception {
        return proxyHandler.getAllHAR();
    }


}
