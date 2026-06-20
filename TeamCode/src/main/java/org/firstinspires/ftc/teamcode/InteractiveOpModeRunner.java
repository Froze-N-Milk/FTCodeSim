package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.fake.FakeHardwareMap;
import org.firstinspires.ftc.teamcode.fake.FakeTelemetry;
import org.firstinspires.ftc.teamcode.opmode.base.TeleOpMode;
import org.psilynx.psikit.ftc.wrappers.GamepadWrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InteractiveOpModeRunner {
    public static void main(String[] args) throws Exception {
        OpMode opMode = new TeleOpMode() {
            @Override
            protected void onFirstDriverInput() {
                System.out.println("First driver input detected.");
            }
        };

        opMode.hardwareMap = new FakeHardwareMap(null, null);
        opMode.gamepad1 = new GamepadWrapper(opMode.gamepad1);
        opMode.telemetry = new FakeTelemetry();

        System.out.println("Initializing OpMode...");
        opMode.init();

        for (int i = 0; i < 10; i++) {
            opMode.init_loop();
            Thread.sleep(20);
        }

        System.out.println("Starting OpMode...");
        opMode.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Interactive OpMode runner started.");
        System.out.println("Commands:");
        System.out.println("  w       = left_stick_y forward");
        System.out.println("  s       = left_stick_y backward");
        System.out.println("  a       = left_stick_x left");
        System.out.println("  d       = left_stick_x right");
        System.out.println("  rx 0.5  = right_stick_x = 0.5");
        System.out.println("  ly -1   = left_stick_y = -1");
        System.out.println("  zero    = reset sticks");
        System.out.println("  exit    = stop");

        while (true) {
            System.out.print("> ");
            System.out.flush();

            String input = reader.readLine();

            if (input == null || input.equalsIgnoreCase("exit")) {
                break;
            }

            applyInput(opMode, input.trim());

            opMode.loop();

            Thread.sleep(20);
        }

        opMode.stop();
        System.out.println("Interactive OpMode runner finished.");
    }

    private static void applyInput(OpMode opMode, String input) {
        if (input.equalsIgnoreCase("zero")) {
            opMode.gamepad1.left_stick_x = 0.0f;
            opMode.gamepad1.left_stick_y = 0.0f;
            opMode.gamepad1.right_stick_x = 0.0f;
            opMode.gamepad1.right_stick_y = 0.0f;
            return;
        }

        if (input.equalsIgnoreCase("w")) {
            opMode.gamepad1.left_stick_y = -1.0f;
            return;
        }

        if (input.equalsIgnoreCase("s")) {
            opMode.gamepad1.left_stick_y = 1.0f;
            return;
        }

        if (input.equalsIgnoreCase("a")) {
            opMode.gamepad1.left_stick_x = -1.0f;
            return;
        }

        if (input.equalsIgnoreCase("d")) {
            opMode.gamepad1.left_stick_x = 1.0f;
            return;
        }

        String[] parts = input.split("\\s+");
        if (parts.length == 2) {
            float value = Float.parseFloat(parts[1]);

            if (parts[0].equalsIgnoreCase("lx")) {
                opMode.gamepad1.left_stick_x = value;
            } else if (parts[0].equalsIgnoreCase("ly")) {
                opMode.gamepad1.left_stick_y = value;
            } else if (parts[0].equalsIgnoreCase("rx")) {
                opMode.gamepad1.right_stick_x = value;
            } else if (parts[0].equalsIgnoreCase("ry")) {
                opMode.gamepad1.right_stick_y = value;
            } else {
                System.out.println("Unknown axis: " + parts[0]);
            }

            return;
        }

        System.out.println("Unknown command: " + input);
    }
}