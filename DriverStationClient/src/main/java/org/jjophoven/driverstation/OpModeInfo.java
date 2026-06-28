package org.jjophoven.driverstation;

import java.io.*;

public class OpModeInfo implements Packet {
    public Type type;
    public String group;
    public String name;

    public OpModeInfo(Type type, String name, String group) {
        this.type = type;
        this.group = group;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public byte getPacketType() {
        return PacketType.OPMODE;
    }

    public enum Type {
        TELEOP, AUTO
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeByte(type.ordinal());
        output.writeUTF(name);
        output.writeUTF(group);
    }

    public static OpModeInfo read(DataInput input) {
        try {
            return new OpModeInfo(
                    Type.values()[input.readByte()],
                    input.readUTF(),
                    input.readUTF()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}