package selenium.external.proxy;

import selenium.external.proxy.process.ProxyProcessHandler;

import java.io.PrintWriter;

public class ExternalProxyHandler {
    private static final ProxyProcessHandler proxyProcessHandler = ProxyProcessHandler.proxy();
    private ProxyHandler proxyHandler = new ProxyHandler();

    public void start() throws Exception {
        proxyProcessHandler.startProcess();
        proxyHandler.startProxy();
        proxyHandler.startHar();
    }

    public void stop() throws Exception {
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

    public void writeHarToFIle(String fileName) throws Exception {
        writeToFile(fileName, getHar());
    }

    public void writeSubHarToFIle(String fileName, String type, String... searchParams) throws Exception {
        writeToFile(fileName, getSubHar(type, searchParams));
    }

    private void writeToFile(String fileName, String har) throws Exception {
        PrintWriter out = new PrintWriter(fileName);
        out.write(har);
        out.close();
    }

}
