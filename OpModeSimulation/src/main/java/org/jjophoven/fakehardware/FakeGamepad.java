package org.jjophoven.fakehardware;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.jjophoven.input.Keybinds;

import java.util.Set;


public class FakeGamepad extends Gamepad {
    public void setFromKeys(Set<Integer> keys, Keybinds binds) {
        binds.apply(this, keys);
    }
}
