package selenium.external.proxy;

import selenium.external.proxy.process.ProxyProcessHandler;

public class ExternalProxyHandler {
    private ProxyProcessHandler proxyProcessHandler = new ProxyProcessHandler();
    private ProxyHandler proxyHandler = new ProxyHandler();

    public void start() throws Exception {
        System.out.println("starting external proxyURL");
        proxyProcessHandler.startProcess();
        proxyHandler.startProxy();
        proxyHandler.startHar();
    }

    public void stop() throws Exception {
        System.out.println("destroying external proxyURL");
        proxyProcessHandler.killProcess();
    }

    public void resetHar() throws Exception {
        proxyHandler.startHar();
    }

    public String getHar() throws Exception {
        return proxyHandler.getAllHAR();
    }

    public String getSubHar(String type, String... searchParams) throws Exception {
        return proxyHandler.getSubHar(type, searchParams);
    }

}
