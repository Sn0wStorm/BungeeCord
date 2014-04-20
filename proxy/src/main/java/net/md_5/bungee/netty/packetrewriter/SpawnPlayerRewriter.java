package net.md_5.bungee.netty.packetrewriter;

import java.util.UUID;
import java.util.logging.Level;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.connection.AuthResult;
import net.md_5.bungee.netty.Var;

public class SpawnPlayerRewriter extends PacketRewriter {

	@Override
	public void rewriteClientToServer(ByteBuf in, ByteBuf out) {
		unsupported(true);
	}

	@Override
	public void rewriteServerToClient(ByteBuf in, ByteBuf out) {

		int entityId = in.readInt();
		String name = Var.readString(in, false);
		Var.writeVarInt(entityId, out);
		Var.writeString("wowe_such_packet", out, true);
		Var.writeString(name, out, true);

		out.writeBytes(in.readBytes(16)); // int - x, int - y, int - z, byte - yaw, byte - pitch, short - item

		Var.rewriteEntityMetadata(in, out);
	}

	public void rewriteServerToClient(ByteBuf in, ByteBuf out, Channel ch) {

		int entityId = in.readInt();
		String name = Var.readString(in, false);
		Var.writeVarInt(entityId, out);

		UserConnection target = (UserConnection) BungeeCord.getInstance().getPlayer(ch);

		if (target == null || target.getProtocolVersion() < 5) {
			Var.writeString("wowe_such_packet", out, true);
			Var.writeString(name, out, true);
		} else {

			UserConnection player = (UserConnection) BungeeCord.getInstance().getPlayer(name);
			if (player != null) {
				//BungeeCord.getInstance().getLogger().log(Level.INFO, "found player");

				AuthResult profile = player.getPendingConnection().getProfile();
				if (profile != null && profile.getProperties() != null && profile.getProperties().length >= 1) {
					AuthResult.Property[] properties = profile.getProperties();
					Var.writeString(profile.getFormattedId(), out, true);
					Var.writeString(name, out, true);
					Var.writeVarInt(properties.length, out);
					for (AuthResult.Property property : properties) {
						Var.writeString(property.getName(), out, true);
						Var.writeString(property.getValue(), out, true);
						Var.writeString(property.getSignature(), out, true);
					}
				} else {
					Var.writeString(getRandomUUID(), out, true);
					Var.writeString(name, out, true);
					Var.writeVarInt(0, out);
				}
			} else {
				Var.writeString(getRandomUUID(), out, true);
				Var.writeString(name, out, true);
				Var.writeVarInt(0, out);
			}
		}

		out.writeBytes(in.readBytes(16)); // int - x, int - y, int - z, byte - yaw, byte - pitch, short - item

		Var.rewriteEntityMetadata(in, out);
	}

	// from Citizens 2
	public static String getRandomUUID() {
		UUID uuid = UUID.randomUUID(); // clear version
		return (new UUID(uuid.getMostSignificantBits() | 0x0000000000005000L, uuid.getLeastSignificantBits())).toString();
	}

}
