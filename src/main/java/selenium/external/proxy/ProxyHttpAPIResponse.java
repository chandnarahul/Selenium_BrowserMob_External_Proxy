package selenium.external.proxy;

public class ProxyHttpAPIResponse {
    int code;
    String data;

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
