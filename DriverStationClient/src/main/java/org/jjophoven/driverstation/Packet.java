package org.jjophoven.driverstation;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet {
    byte getPacketType();
    void write(DataOutputStream out) throws IOException;
}