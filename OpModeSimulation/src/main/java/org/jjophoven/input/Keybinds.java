package org.jjophoven.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Set;

public class Keybinds {
    public void apply(Gamepad g, Set<Integer> keys) {
        Gamepad changes = new Gamepad();

        changes.dpad_up = keys.contains(Keys.UP);
        changes.dpad_down = keys.contains(Keys.DOWN);
        changes.dpad_left = keys.contains(Keys.LEFT);
        changes.dpad_right = keys.contains(Keys.RIGHT);

        changes.right_stick_x = joystick(keys, Keys.J, Keys.L);
        changes.right_stick_y = joystick(keys, Keys.K, Keys.I);

        changes.left_stick_x = joystick(keys, Keys.A, Keys.D);
        changes.left_stick_y = joystick(keys, Keys.S, Keys.W);

        changes.back = keys.contains(Keys.TAB);
        changes.start = keys.contains(Keys.ENTER);

        changes.right_bumper = keys.contains(Keys.Q);
        changes.left_bumper = keys.contains(Keys.E);
        changes.right_trigger = keys.contains(Keys.U) ? 1 : 0;
        changes.left_trigger = keys.contains(Keys.O) ? 1 : 0;

        changes.square = keys.contains(Keys.P);
        changes.circle = keys.contains(Keys.OPEN_BRACKET);
        changes.triangle = keys.contains(Keys.MINUS);
        changes.cross = keys.contains(Keys.SEMICOLON);

        changes.x = keys.contains(Keys.P);
        changes.y = keys.contains(Keys.MINUS);
        changes.b = keys.contains(Keys.OPEN_BRACKET);
        changes.a = keys.contains(Keys.SEMICOLON);

        changes.left_stick_button = keys.contains(Keys.Z);
        changes.right_stick_button = keys.contains(Keys.COMMA);

        g.fromByteArray(changes.toByteArray());
    }

    public float joystick(Set<Integer> keys, int positive, int negative) {
        return keys.contains(positive) && keys.contains(negative) ? 0 :
                keys.contains(negative) ? -1 :
                keys.contains(positive) ? 1 : 0;
    }
}
