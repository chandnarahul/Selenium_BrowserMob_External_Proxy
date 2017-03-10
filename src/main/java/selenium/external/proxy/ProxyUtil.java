package selenium.external.proxy;

public class ProxyUtil {
    public static boolean isNotBlank(String data) {
        return data != null && !data.trim().isEmpty();
    }
}
