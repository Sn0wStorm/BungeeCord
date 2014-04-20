package net.md_5.bungee.connection;

import java.util.logging.Level;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.md_5.bungee.BungeeCord;

@Data
@AllArgsConstructor
public class AuthResult
{

    private String id;
	private Property[] properties;

	public String getFormattedId() {
		return id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32);
	}

	@Data
	@AllArgsConstructor
	public static class Property {

		private String name;
		private String value;
		private String signature;

	}
}
