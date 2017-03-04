package selenium.external.proxy;

import selenium.external.proxy.process.ProxyProcessHandler;

import java.io.PrintWriter;

public class ExternalProxyHandler {
    private static final ProxyProcessHandler proxyProcessHandler = ProxyProcessHandler.proxy();
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

    public void writeHarToFIle(String fileName) throws Exception {
        PrintWriter out = new PrintWriter(fileName);
        out.write(getHar());
        out.close();
    }

    public void writeSubHarToFIle(String fileName, String type, String... searchParams) throws Exception {
        PrintWriter out = new PrintWriter(fileName);
        out.write(getSubHar(type, searchParams));
        out.close();
    }

    public String getSubHar(String type, String... searchParams) throws Exception {
        return proxyHandler.getSubHar(type, searchParams);
    }

}
