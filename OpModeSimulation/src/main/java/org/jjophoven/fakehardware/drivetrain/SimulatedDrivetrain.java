package org.jjophoven.fakehardware.drivetrain;

import org.jjophoven.fakehardware.FakeMotor;
import org.jjophoven.fit.MotorModel;
import org.psilynx.psikit.core.Logger;

public abstract class SimulatedDrivetrain {
    private final FakeMotor[] motors;

    public MotionVector position = new MotionVector(0, 0, 0); // model start heading
    public MotionVector velocity = new MotionVector(0, 0, 0);

    protected double[] motorVelocities;
    protected MotorModel model;
    protected double[] coefficients;

    public SimulatedDrivetrain(FakeMotor[] motors, MotorModel model, double[] coefficients) {
        this.motors = motors;

        motorVelocities = new double[motors.length];
        this.model = model;
        this.coefficients = coefficients;
    }

    public void step(double deltaTime) {
        boolean allMotorsStationary = true;
        for (int i = 0; i < motors.length; i++) {
            motors[i].step(deltaTime);

            FakeMotor motor = motors[i];
            motorVelocities[i] = motor.getVelocity();

            Logger.recordOutput("Mecanum/vels/" + motor.deviceName, motor.getVelocity());
            Logger.recordOutput("Mecanum/powers/" + motor.deviceName, motor.getPower());
            Logger.recordOutput("Mecanum/accelerations/" + motor.deviceName, motor.getAcceleration());

            if (!motor.isStationary()) {
                allMotorsStationary = false;
            }
        }

        velocity = forwardKinematics(motorVelocities).toFieldFrame(position.theta);

        if (allMotorsStationary) {
            velocity = new MotionVector(0, 0, 0);
        }

        position = position.step(velocity, deltaTime);

        MotionVector motionVector = position.plus(velocity);
        motionVector.log("Mecanum/velocity");

        // Accounts for wheels moving from whole robot moving
        motorVelocities = inverseKinematics(velocity.toRobotFrame(position.theta));
        for (int i = 0; i < motors.length; i++) {
            motors[i].setRollVelocity(motorVelocities[i]);
        }

        // TODO maybe make it more accurate by calculating rolling accel?

        position.log("Mecanum/pose");
    }

    abstract MotionVector forwardKinematics(double[] motors);
    abstract double[] inverseKinematics(MotionVector motion);
}