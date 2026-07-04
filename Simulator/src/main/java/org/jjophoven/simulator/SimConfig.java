package org.jjophoven.simulator;

import org.jjophoven.input.DefaultKeybinds;
import org.jjophoven.simhardware.SimHardwareMap;
import org.jjophoven.input.Keybinds;


public class SimConfig {
    public SimHardwareMap simHardwareMap;

    public long loopTimeMs = 20;

    public Keybinds gamepad1Keybinds = new DefaultKeybinds();
    public Keybinds gamepad2Keybinds = new DefaultKeybinds();
}