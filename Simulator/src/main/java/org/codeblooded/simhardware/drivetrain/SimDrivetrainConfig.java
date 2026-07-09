package org.codeblooded.simhardware.drivetrain;

import org.codeblooded.simhardware.SimHardwareMap;

public abstract class SimDrivetrainConfig {
    public SimHardwareMap simHardwareMap;
    public double maxVelocity;
    public double maxAcceleration;
    public double naturalDeceleration;
    public double wheelRadius;
    public double staticVelocityRegion;
    public double staticFriction;
    public double nominalVoltage = 13;
}
