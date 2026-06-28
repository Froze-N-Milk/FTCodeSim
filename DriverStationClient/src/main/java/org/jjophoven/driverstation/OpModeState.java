package org.jjophoven.driverstation;

import java.io.*;

public enum OpModeState implements Packet {
    WAIT_FOR_INIT,
    INITIALIZING,
    RUNNING;

    @Override
    public byte getPacketType() {
        return PacketType.STATE;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(ordinal());
    }

    public static OpModeState read(DataInputStream in) throws IOException {
        return values()[in.readByte()];
    }
}
