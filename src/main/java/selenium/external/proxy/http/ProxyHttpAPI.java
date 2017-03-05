package selenium.external.proxy.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProxyHttpAPI {
    private ProxyHttpAPI() {
    }

    public static ProxyHttpAPIResponse response(String url, String methodType, String data) throws IOException {
        return new ProxyHttpAPI().apiResponse(url, methodType, data);
    }

    private ProxyHttpAPIResponse apiResponse(String proxyURL, String methodType, String data) throws IOException {
        URL url = new URL(proxyURL);
        System.out.println("\nSending '" + methodType + "' request to URL : " + proxyURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(methodType);
        sendData(data, httpURLConnection);
        return apiHttpConnectionResponse(httpURLConnection);
    }

    private void sendData(String data, HttpURLConnection httpURLConnection) throws IOException {
        final String CONTENT_TYPE = "application/x-www-form-urlencoded";
        if (data != null && !data.trim().isEmpty()) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", CONTENT_TYPE);
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(data);
            dataOutputStream.flush();
            dataOutputStream.close();
        }
    }

    private ProxyHttpAPIResponse apiHttpConnectionResponse(HttpURLConnection con) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder data = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            data.append(inputLine);
        }
        bufferedReader.close();
        con.disconnect();
        return new ProxyHttpAPIResponse(con.getResponseCode(), data.toString());
    }

}
