package selenium.external.proxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyHandler {

    public void startProxy() throws Exception {
        System.out.println(sendPost(GlobalProxyConfig.proxyInit()));
    }

    public void startHar() throws Exception {
        System.out.println(sendPut(GlobalProxyConfig.harURL()));
    }

    public String getAllHAR() throws Exception {
        return sendGet(GlobalProxyConfig.harURL());
    }

    private String sendPost(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("port=" + GlobalProxyConfig.PROXY_RUNNING_PORT);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        return getResponse(con).toString();
    }

    private String sendPut(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes("captureHeaders=true&captureContent=true&initialPageRef=foo");
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'PUT' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        return getResponse(con).toString();
    }

    private String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        return getResponse(con).toString();
    }

    private StringBuffer getResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }
}
