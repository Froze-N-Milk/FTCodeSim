package org.jjophoven.fakehardware.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.jjophoven.fakehardware.FakeMotor;
import org.jjophoven.fit.MotorModel;
import org.jjophoven.simulator.SimulationConfig;

public class MecanumConfig implements DrivetrainConfig {
    public String frontLeftMotorName;
    public String frontRightMotorName;
    public String backLeftMotorName;
    public String backRightMotorName;

    public MotorModel motorModel = MotorModel.fromString("a=Au-Bv*abs(d)-Cv-Dsgn(v)");
    public double[] coefficients;
    public double staticVelocityRegion;
    public double staticFriction;
    public double wheelbase;
    public double trackWidth;
    public double wheelDiameter;

    public double maxVelocity;
    public double maxAcceleration;
    public double naturalDeceleration;
    public double strafeEfficiency;

    public DcMotor.ZeroPowerBehavior zeroPowerBehavior;

    FakeMotor[] motors = new FakeMotor[4];

    @SuppressWarnings("unchecked")
    public <T> T configureDevice(Class<? extends T> device, String deviceName) {
        synchronized (new Object()) {
            if (!(device.equals(DcMotor.class)) && !(device.equals(DcMotorEx.class))) {
                return null;
            }

            double radius = wheelDiameter / 2;

            FakeMotor motor = new FakeMotor(deviceName, motorModel, coefficients, staticVelocityRegion/radius, staticFriction/radius);

            if (deviceName.equals(frontLeftMotorName)) {
                motors[0] = motor;
                return (T) motor;
            }
            if (deviceName.equals(frontRightMotorName)) {
                motors[1] = motor;
                return (T) motor;
            }
            if (deviceName.equals(backLeftMotorName)) {
                motors[2] = motor;
                return (T) motor;
            }
            if (deviceName.equals(backRightMotorName)) {
                motors[3] = motor;
                return (T) motor;
            }
            return null;
        }
    }

    @Override
    public SimulatedDrivetrain createDrivetrain() {
        double radius = wheelDiameter / 2;

        double maxOmega = maxVelocity / radius;
        double maxAlpha = maxAcceleration / radius;
        double naturalAlpha = naturalDeceleration / radius;

        double kA = (maxAlpha + naturalAlpha) / 13; // TODO get battery voltage
        double kBackEMF = maxAlpha / maxOmega;
        double kCoulombFriction = naturalDeceleration / radius;

        if (zeroPowerBehavior == DcMotor.ZeroPowerBehavior.BRAKE) {
            coefficients = new double[]{
                    kA, 0, kBackEMF, kCoulombFriction
            };
        } else {
            coefficients = new double[]{
                    kA, kBackEMF, 0, kCoulombFriction
            };
        }
        return new FakeMecanum(
                motors, wheelbase, trackWidth, wheelDiameter, strafeEfficiency);
    }
}
