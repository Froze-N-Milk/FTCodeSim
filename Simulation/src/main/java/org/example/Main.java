package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws IOException, NativeHookException, InterruptedException {
        System.out.println("Keyboard process started");

        Socket socket = new Socket("127.0.0.1", 8080);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Disable JNativeHook logging (optional)
//        java.util.logging.Logger logger =
//                java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
//        logger.setLevel(java.util.logging.Level.OFF);
//        logger.setUseParentHandlers(false);

        GlobalScreen.registerNativeHook();

        System.out.println("hosting socket");

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                try {
                    out.writeInt(e.getKeyCode());
                    out.writeBoolean(true); // pressed
                    out.flush();
                } catch (IOException ex) {
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ignored) {}

                    System.exit(0);
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                try {
                    out.writeInt(e.getKeyCode());
                    out.writeBoolean(false); // released
                    out.flush();
                } catch (IOException ex) {
                    try {
                        GlobalScreen.unregisterNativeHook();
                    } catch (NativeHookException ignored) {}

                    System.exit(0);
                }
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ignored) {
            }
        }));

        // Keep the program alive forever.
        new CountDownLatch(1).await();
    }
}