package org.codeblooded.simulator;

import org.codeblooded.input.DefaultKeybinds;
import org.codeblooded.simhardware.SimHardwareMap;
import org.codeblooded.input.Keybinds;


public class SimConfig {
    public SimHardwareMap simHardwareMap;

    public long loopTimeMs = 20;

    public Keybinds gamepad1Keybinds = new DefaultKeybinds();
    public Keybinds gamepad2Keybinds = new DefaultKeybinds();

    public RobotGeometry robotGeometry;
}