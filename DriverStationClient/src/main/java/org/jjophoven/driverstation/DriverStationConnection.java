package org.jjophoven.driverstation;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DriverStationConnection {
    private final Consumer<String> telemetryConsumer;
    private final Runnable connectedCallback;
    private final Runnable disconnectedCallback;
    private final Consumer<OpModeList> opModeListCallback;

    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;
    private final AtomicBoolean running = new AtomicBoolean(true);

    protected OpModeList opmodes;

    public DriverStationConnection(
            int port,
            Consumer<String> telemetryConsumer,
            Runnable connectedCallback,
            Runnable disconnectedCallback,
            Consumer<OpModeList> opModeListCallback
    ) {
        this.telemetryConsumer = telemetryConsumer;
        this.connectedCallback = connectedCallback;
        this.disconnectedCallback = disconnectedCallback;
        this.opModeListCallback = opModeListCallback;

        new Thread(() -> connect(port)).start();
    }

    private void connect(int port) {
        try {
            socket = new Socket("127.0.0.1", port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            SwingUtilities.invokeLater(connectedCallback);

            readLoop();
        } catch (IOException e) {
            close();
        }
    }

    private void readLoop() {
        try {
            while (running.get()) {
                byte type = input.readByte();
                switch (type) {
                    case PacketType.TELEMETRY:
                        String telemetry = input.readUTF();
                        SwingUtilities.invokeLater(() -> telemetryConsumer.accept(telemetry));
                        break;
                    case PacketType.OPMODE:
                        opmodes = OpModeList.read(input);
                        for (OpModeInfo info : opmodes.opmodes) {
                            System.out.println(info.name + " (" + info.group + ")");
                        }
                        SwingUtilities.invokeLater(() -> opModeListCallback.accept(opmodes));
                        break;
                }
            }
        } catch (IOException ignored) {
            close();
        }
    }

    public void send(Packet packet) {
        safe(() -> {
            output.writeByte(packet.getPacketType());
            packet.write(output);
            output.flush();
        });
    }

    @FunctionalInterface
    public interface IORunnable {
        void run() throws IOException;
    }

    public void safe(IORunnable run) {
        if (output == null || input == null) {
            System.out.println("ERR Uninitialized client");
            return;
        }

        try {
            run.run();
        } catch (IOException e) {
            System.out.println("Send error: " + e.getMessage());
            close();
        }
    }


    public void close() {
        running.set(false);

        try { if (input != null) input.close(); } catch (IOException ignored) {}
        try { if (output != null) output.close(); } catch (IOException ignored) {}
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}

        SwingUtilities.invokeLater(disconnectedCallback);
    }
}