package org.jjophoven.simhardware;

import androidx.annotation.Nullable;
import com.qualcomm.robotcore.hardware.*;
import org.jjophoven.simhardware.devices.*;
import org.jjophoven.simhardware.drivetrain.SimulatedDrivetrain;

import java.util.List;

public class SimHardwareMap extends HardwareMap {
    private SimulatedDrivetrain drivetrain;

    public SimulatedDrivetrain getDrivetrain() {
        return drivetrain;
    }

    public void setDrivetrain(SimulatedDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public SimHardwareMap() {
        super(null, null);
        SimVoltageSensor voltageSensor = new SimVoltageSensor();

        // TODO automatically do this for every device
        this.voltageSensor.put("voltageSensor", voltageSensor);

        put("voltageSensor", voltageSensor);
    }

    public @Nullable <T> T tryGet(Class<? extends T> classOrInterface, String deviceName) {
        synchronized (lock) {
            deviceName = deviceName.trim();
            List<HardwareDevice> list = allDevicesMap.get(deviceName);
            @Nullable T result = null;

            if (list != null) {
                for (HardwareDevice device : list) {
                    if (classOrInterface.isInstance(device)) {
                        rebuildDeviceNamesIfNecessary();
                        result = classOrInterface.cast(device);
                        break;
                    }
                }
            }
            if (result == null) {
                System.out.println("Could not find device " + deviceName + " of type " + classOrInterface.getSimpleName());
            }

            return result;
        }
    }

    public SimMotor motor(SimMotorConfig config) {
        return register(config.name, new SimMotor(config));
    }

    public SimGobildaPinpoint pinpoint(String name) {
        if (drivetrain == null) {
            throw new RuntimeException("Drivetrain not set");
        }
        return register(name, new SimGobildaPinpoint(drivetrain));
    }

    public <T extends HardwareDevice> T register(String name, T device) {
        System.out.println("Registering " + name + " as " + device.getClass().getSimpleName());
        put(name, device);
        return device;
    }

    public void update() {
        // TODO add deltaTime here to step and update
        if (drivetrain != null) {
            drivetrain.step(0.02);
        }
        for (List<HardwareDevice> device : allDevicesMap.values()) {
            for (HardwareDevice d : device) {
                try {
                    ((SimHardwareDevice) d).update();
                } catch (ClassCastException ignored) {
                }
            }
        }
    }
}