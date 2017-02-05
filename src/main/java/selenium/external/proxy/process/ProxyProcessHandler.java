package selenium.external.proxy.process;

import selenium.external.proxy.GlobalProxyConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProxyProcessHandler {
    private Process process = null;

    public void killProcess() throws Exception {
        destroy();
        if (isLinux()) {
            stopLinuxProcess();
        } else {
            stopWindowProcess();
        }
    }

    public void startProcess() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(getProxy());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        process = pb.start();
        Thread.sleep(10000);
    }

    private void stopLinuxProcess() throws IOException, InterruptedException {
        String killCommand = "pkill -f java.*" + GlobalProxyConfig.LINUX_BROWSERMOB_PROXY_PROCESS;
        System.out.println(killCommand);
        Runtime.getRuntime().exec(new String[]{"bash", "-c", killCommand}).waitFor();
    }

    private void destroy() {
        if (process != null) {
            process.destroy();
        }
    }

    private void stopWindowProcess() throws Exception {
        if (isWinProcessRunning(GlobalProxyConfig.WIN_BROWSERMOB_PROXY_PROCESS)) {
            killWinProcess(GlobalProxyConfig.WIN_BROWSERMOB_PROXY_PROCESS);
        }
    }

    private boolean isWinProcessRunning(String serviceName) throws Exception {
        Process p = Runtime.getRuntime().exec("tasklist");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    private static void killWinProcess(String serviceName) throws Exception {
        final String KILL = "taskkill /F /IM ";
        Runtime.getRuntime().exec(KILL + serviceName);
    }

    private String getProxy() {
        return isLinux() ? GlobalProxyConfig.LINUX_PROXY : GlobalProxyConfig.WIN_PROXY;
    }

    private boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
