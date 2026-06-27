package org.jjophoven.simulator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.jjophoven.driverstation.OpModeState;
import org.jjophoven.fakehardware.FakeDriverStationServer;
import org.jjophoven.fakehardware.FakeHardwareMap;
import org.jjophoven.fakehardware.FakeTelemetry;

import java.io.IOException;

public class OpModeSimulator {
    public static void simulate(OpMode opMode) throws InterruptedException, IOException {
        FakeDriverStationServer driverStation = new FakeDriverStationServer();

        driverStation.startServer();
        driverStation.acceptClient();

        FakeHardwareMap fakeHardwareMap = new FakeHardwareMap();
        opMode.hardwareMap = fakeHardwareMap;
        opMode.telemetry = new FakeTelemetry(driverStation);
        opMode.gamepad1 = driverStation.gamepad1;
        opMode.gamepad2 = driverStation.gamepad2;

        System.out.println(driverStation.state);

        while (driverStation.state == OpModeState.WAIT_FOR_INIT) {
            driverStation.poll();

            Thread.sleep(20);
        }

        opMode.init();

        while (driverStation.state == OpModeState.INITIALIZING) {
            driverStation.poll();

            fakeHardwareMap.updateHardware();

            opMode.init_loop();

            Thread.sleep(20);
        }

        opMode.start();

        while (driverStation.state == OpModeState.RUNNING) {
            driverStation.poll();


            fakeHardwareMap.updateHardware();

            opMode.loop();

            Thread.sleep(20);
        }
    }
}