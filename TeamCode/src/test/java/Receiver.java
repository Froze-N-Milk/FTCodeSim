import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Receiver {
    private static final int PORT = 8080;
    private static final int SOCKET_TIMEOUT_MS = 30_000;

    @Test
    public void go() throws IOException {
        log("Starting receiver on port " + PORT);

        Process process = null;

        try (
                ServerSocket serverSocket = new ServerSocket(PORT)
        ) {
            serverSocket.setSoTimeout(SOCKET_TIMEOUT_MS);

            log("Receiver ready");
            process = startInputProcess();

            log("Waiting for input connection...");

            try (
                    Socket socket = serverSocket.accept();
                    DataInputStream in = new DataInputStream(socket.getInputStream())
            ) {
                log("input connected");

                while (!socket.isClosed()) {
                    int keyCode = in.readInt();
                    boolean pressed = in.readBoolean();

                    log("keyCode=" + keyCode + ", " + (pressed ? "pressed" : "released"));
                }
            } catch (SocketTimeoutException e) {
                throw new IOException("Timed out waiting for input process to connect", e);
            }
        } finally {
            if (process != null) {
                process.destroy();
                log("Input process stopped");
            }
        }
    }

    private static Process startInputProcess() throws IOException {
        File projectRoot = findProjectRoot();
        File gradlew = new File(projectRoot, isWindows() ? "gradlew.bat" : "gradlew");

        log("Starting input process");
        log("Project root: " + projectRoot.getAbsolutePath());

        return new ProcessBuilder(
                gradlew.getAbsolutePath(),
                ":Simulation:run"
        )
                .directory(projectRoot)
                .redirectErrorStream(true)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .start();
    }

    private static File findProjectRoot() throws IOException {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();

        while (dir != null) {
            File gradlew = new File(dir, isWindows() ? "gradlew.bat" : "gradlew");
            File settingsGradle = new File(dir, "settings.gradle");

            if (gradlew.isFile() && settingsGradle.isFile()) {
                return dir;
            }

            dir = dir.getParentFile();
        }

        throw new IOException("Could not find project root from user.dir=" + System.getProperty("user.dir"));
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static void log(String message) {
        System.out.println("[Receiver] " + message);
    }
}