package org.jjophoven.fakehardware;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.jjophoven.simulator.SimulationConfig;
import org.jjophoven.fakehardware.drivetrain.SimulatedDrivetrain;

public class FakeHardwareMap extends HardwareMap {
    public FakeHardwareMap(SimulationConfig simulationConfig) {
        super(null, null);
        this.simulationConfig = simulationConfig;
        voltageSensor.put("voltageSensor", new FakeVoltageSensor());
    }

    FakePinpoint pinpoint;
    SimulationConfig simulationConfig;
    SimulatedDrivetrain drivetrain;

    @Override
    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        synchronized (lock) {
            T consumed = simulationConfig.drivetrain.configureDevice(classOrInterface, deviceName);
            if (consumed != null) return consumed;

            if (classOrInterface.equals(DcMotor.class) || classOrInterface.equals(DcMotorEx.class)) {
                throw new RuntimeException("Cannot fake motor without knowing component being used");
            } else if (classOrInterface.equals(VoltageSensor.class)) {
                return (T) new FakeVoltageSensor();
            } else if (classOrInterface.equals(GoBildaPinpointDriver.class)) {
                pinpoint = new FakePinpoint();
                return (T) pinpoint;
            }
            System.out.println("Unable to find a hardware device with name \"" + deviceName + "\" and type " + classOrInterface.getSimpleName());
            return null;
        }
    }

    public void initializeHardware() {
        drivetrain = simulationConfig.drivetrain.createDrivetrain();
    }

    public void updateHardware() {
        drivetrain.step(0.02);
        pinpoint.pose2D =
                new Pose2D(DistanceUnit.MM,
                0,
                0,
                AngleUnit.RADIANS,
                AngleUnit.normalizeRadians(drivetrain.position.theta));
    }
}
//double accelK = 0.7;
//double dutyBackEMF = 1.5;
//double viscousFriction = 0.7;
//double coulombFriction = 3; // rolling friction?
//double staticFriction = 4.5; // accel in/s^2 must be > coulombFriction
//double staticVelocityRegion = 0.05;
//double[] coefficients = new double[]{
//        accelK,
//        dutyBackEMF,
//        viscousFriction,
//        coulombFriction
//};
//
//drivetrain = new FakeMecanum(motors.toArray(new FakeMotor[4]), coefficients, staticVelocityRegion, staticFriction, 0.2286, 0.2286);