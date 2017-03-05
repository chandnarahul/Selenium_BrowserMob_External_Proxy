package selenium.external.proxy.http;

public class ProxyHttpAPIResponse {
    private int code;
    private String data;

    public String data() {
        return data;
    }

    public int code() {
        return code;
    }

    public ProxyHttpAPIResponse(int code, String data) {
        this.code = code;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProxyHttpAPIResponse{" +
                "code=" + code +
                ", data='" + data + '\'' +
                '}';
    }
}
