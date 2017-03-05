package selenium.external.proxy;

public class ProxyHandler {

    public void startProxy() throws Exception {
        System.out.println(ProxyHttpAPI.response(GlobalProxyConfig.proxyInit(), "POST", "port=" + GlobalProxyConfig.PROXY_RUNNING_PORT));
    }

    public void startHar() throws Exception {
        System.out.println(ProxyHttpAPI.response(GlobalProxyConfig.harURL(), "PUT", "captureHeaders=true&captureContent=true"));
    }

    public String getAllHAR() throws Exception {
        return ProxyHttpAPI.response(GlobalProxyConfig.harURL(), "GET", "").data;
    }

    public String getSubHar(String type, String... searchParams) throws Exception {
        for (String data : getAllHAR().split(type)) {
            int count = 0;
            for (String search : searchParams) {
                if (data.contains(search)) {
                    count++;
                } else {
                    break;
                }
            }
            if (count == searchParams.length) {
                return data;
            }
        }
        return "";
    }

}
