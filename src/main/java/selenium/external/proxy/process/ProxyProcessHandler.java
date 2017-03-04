package selenium.external.proxy.process;

import selenium.external.proxy.GlobalProxyConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProxyProcessHandler {

    public void killProcess() throws Exception {
        if (isLinux()) {
            System.out.println("stopping linux proxy");
            stopLinuxProcess();
        } else {
            System.out.println("stopping windows proxy");
            stopWindowProcess();
        }
    }

    public void startProcess() throws Exception {
        startProxyServer();
        addProxyServerShutDownHook();
        Thread.sleep(10000);
    }

    private void startProxyServer() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(getProxy());
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.start();
    }

    private void addProxyServerShutDownHook() {
        Thread stopProxyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    killProcess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(stopProxyThread);
    }

    private void stopLinuxProcess() throws IOException, InterruptedException {
        String killCommand = "pkill -f java.*" + GlobalProxyConfig.LINUX_BROWSERMOB_PROXY_PROCESS;
        System.out.println(killCommand);
        Runtime.getRuntime().exec(new String[]{"bash", "-c", killCommand}).waitFor();
    }

    private void stopWindowProcess() throws Exception {
        String winProcessPID = getWinProcess(GlobalProxyConfig.WIN_BROWSERMOB_PROXY_PROCESS);
        if (!winProcessPID.isEmpty()) {
            killWinProcess(winProcessPID);
        } else {
            System.out.println("windows [" + GlobalProxyConfig.WIN_BROWSERMOB_PROXY_PROCESS + "] process not found !");
        }
    }

    private String getWinProcess(String serviceName) throws Exception {
        Process p = Runtime.getRuntime().exec("jps -lv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.contains(serviceName)) {
                System.out.println("windows process found [" + line + "]");
                System.out.println("process id is [" + line.split(" ")[0] + "]");
                return line.split(" ")[0];
            }
        }
        return "";
    }

    private static void killWinProcess(String winProcessPID) throws Exception {
        final String KILL = "taskkill /F /PID ";
        Runtime.getRuntime().exec(KILL + winProcessPID);
    }

    private String getProxy() {
        return isLinux() ? GlobalProxyConfig.LINUX_PROXY : GlobalProxyConfig.WIN_PROXY;
    }

    private boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
