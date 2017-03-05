package selenium.external.proxy;

import selenium.external.proxy.http.ProxyHttpAPI;
import selenium.external.proxy.process.ProxyProcessHandler;

import java.io.PrintWriter;

public class ProxyHandler {
    private static final ProxyProcessHandler proxyProcessHandler = ProxyProcessHandler.proxy();

    public void start() throws Exception {
        proxyProcessHandler.startProcess();
        System.out.println(ProxyHttpAPI.response(GlobalProxyConfig.proxyInit(), "POST", "port=" + GlobalProxyConfig.PROXY_RUNNING_PORT));
        startHar();
    }

    public void stop() throws Exception {
        proxyProcessHandler.killProcess();
    }

    public void startHar() throws Exception {
        System.out.println(ProxyHttpAPI.response(GlobalProxyConfig.harURL(), "PUT", "captureHeaders=true&captureContent=true"));
    }

    public String getAllHAR() throws Exception {
        return ProxyHttpAPI.response(GlobalProxyConfig.harURL(), "GET", "").data();
    }

    public String getSubHar(String type, String... searchParams) throws Exception {
        for (String data : getAllHAR().split(type)) {
            if (paramMatch(data, searchParams)) {
                return data;
            }
        }
        return "";
    }

    public void harToFile(String fileName) throws Exception {
        writeToFile(fileName, getAllHAR());
    }

    public void subHarToFile(String fileName, String type, String... searchParams) throws Exception {
        writeToFile(fileName, getSubHar(type, searchParams));
    }

    public boolean isSubHarContains(String type, String... searchParams) throws Exception {
        return !getSubHar(type, searchParams).isEmpty();
    }

    private void writeToFile(String fileName, String har) throws Exception {
        PrintWriter out = new PrintWriter(fileName);
        out.write(har);
        out.close();
    }

    private boolean paramMatch(String data, String[] searchParams) {
        for (String search : searchParams) {
            if (!data.contains(search)) {
                return false;
            }
        }
        return true;
    }

}
