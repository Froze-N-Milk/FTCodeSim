package org.jjophoven.fakehardware.drivetrain;

public interface DrivetrainConfig {
    <T> T configureDevice(Class<? extends T> device, String deviceName);
    SimulatedDrivetrain createDrivetrain();
}
