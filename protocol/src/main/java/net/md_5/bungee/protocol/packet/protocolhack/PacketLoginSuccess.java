package net.md_5.bungee.protocol.packet.protocolhack;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.packet.AbstractPacketHandler;

public class PacketLoginSuccess extends Defined172Packet {
    String name;
	String uuid;
    public PacketLoginSuccess(String name, String uuid) {
        super( 0x02 );
        this.name = name;
	    this.uuid = uuid;
    }

    @Override
    public void read(ByteBuf buf) {

    }

    @Override
    public void write(ByteBuf buf) {
        writeString( uuid, buf, true );
        writeString( name, buf, true );
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {

    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "UUID:" + uuid + ", NAME:" + name;
    }
}
