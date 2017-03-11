package selenium.external.proxy.process;

import selenium.external.proxy.GlobalProxyConfig;
import selenium.external.proxy.ProxyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProxyProcessHandler implements Cloneable {
    private static volatile boolean isProxyStarted = false;
    private static final ProxyProcessHandler proxyProcessHandler = new ProxyProcessHandler();
    private final Object LOCK_OBJECT = new Object();

    private ProxyProcessHandler() {

    }

    public static ProxyProcessHandler proxy() {
        return proxyProcessHandler;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("only one proxy can run at a time.");
    }

    public void killProcess() throws Exception {
        synchronized (LOCK_OBJECT) {
            if (isProxyStarted) {
                if (isLinux()) {
                    stopLinuxProcess();
                } else {
                    stopWindowProcess();
                }
                isProxyStarted = false;
            }
        }
    }

    public void startProcess() throws Exception {
        synchronized (LOCK_OBJECT) {
            if (!isProxyStarted) {
                isProxyStarted = true;
                addProxyServerShutDownHook();
                startProxyServer();
            }
        }
    }

    private void startProxyServer() throws IOException {
        System.out.println("starting external proxyURL");
        ProcessBuilder processBuilder = new ProcessBuilder(getProxy());
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = processBuilder.start();
        holdTillProxyStarts(process);
    }

    private void holdTillProxyStarts(Process process) throws IOException {
        String output;
        BufferedReader bufferedReader = getOutput(process);
        while (ProxyUtil.isNotBlank(output = bufferedReader.readLine())) {
            System.out.println(output);
            if (output.contains(GlobalProxyConfig.PROXY_STARTED_INDICATOR)) {
                break;
            }
        }
    }

    private BufferedReader getOutput(Process process) {
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
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
        System.out.println("stopping linux proxy");
        String killCommand = "pkill -f java.*" + GlobalProxyConfig.LINUX_BROWSERMOB_PROXY_PROCESS;
        System.out.println(killCommand);
        Runtime.getRuntime().exec(new String[]{"bash", "-c", killCommand}).waitFor();
    }

    private void stopWindowProcess() throws Exception {
        System.out.println("stopping windows proxy");
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
        while (ProxyUtil.isNotBlank(line = reader.readLine())) {
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
