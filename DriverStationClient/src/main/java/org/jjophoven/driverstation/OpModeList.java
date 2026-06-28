package org.jjophoven.driverstation;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpModeList implements Packet {
    List<OpModeInfo> opmodes;

    public OpModeList(List<OpModeInfo> opmodes) {
        this.opmodes = opmodes;
    }

    @Override
    public byte getPacketType() {
        return PacketType.OPMODE;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeInt(getPacketType());
        try {
            output.writeInt(opmodes.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (OpModeInfo opmode : opmodes) {
            opmode.write(output);
        }
    }

    public static OpModeList read(DataInputStream input) {
        List<OpModeInfo> opmodes = new ArrayList<>();

        int autoCount = 0;
        try {
            autoCount = input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < autoCount; i++) {
            opmodes.add(OpModeInfo.read(input));
        }

        return new OpModeList(opmodes);
    }
}
