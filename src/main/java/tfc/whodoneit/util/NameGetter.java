package tfc.whodoneit.util;

import com.google.gson.JsonObject;

import java.util.UUID;

import static tfc.whodoneit.util.GeneralUtils.*;

public class NameGetter {
	protected static ExpiringCache<UUID, String> nameCache = new ExpiringCache<>(NameGetter::getName, 300000);
	protected static ExpiringCache<String, UUID> UUIDCache = new ExpiringCache<>(NameGetter::getUUID, 300000);
	
	public static String getNameFor(UUID name) {
		String val = nameCache.get(name);
		UUIDCache.set(val, name);
		return val;
	}
	
	public static UUID getUUIDFor(String name) {
		UUID val = UUIDCache.get(name);
		nameCache.set(val, name);
		return val;
	}
	
	protected static String getName(UUID culprit) {
		if (!checkUUID(culprit.toString())) return null;
		JsonObject object = webRequest("https://sessionserver.mojang.com/session/minecraft/profile/" + culprit);
		if (object == null) {
			return culprit.toString();
//			object = webRequest("https://playerdb.co/api/player/minecraft/" + culprit);
//			if (object == null) return culprit.toString();
//			if (!object.getAsJsonPrimitive("success").getAsBoolean()) return culprit.toString();
//			return object.getAsJsonObject("data").getAsJsonObject("player").getAsJsonPrimitive("username").getAsString();
		}
		if (object.has("error")) return culprit.toString();
		return object.getAsJsonPrimitive("name").getAsString();
	}
	
	protected static UUID getUUID(String uuid) {
		if (checkUUID(uuid)) return UUID.fromString(uuid);
		JsonObject object = webRequest("https://api.mojang.com/users/profiles/minecraft/" + uuid);
		if (object == null) {
//			return UUID.fromString(uuid.toString());
			return null;
//			object = webRequest("https://playerdb.co/api/player/minecraft/" + uuid);
//			if (object == null) return UUID.fromString(uuid.toString());
//			if (!object.getAsJsonPrimitive("success").getAsBoolean()) return UUID.fromString(uuid.toString());
//			return UUID.fromString(object.getAsJsonObject("data").getAsJsonObject("player").getAsJsonPrimitive("id").getAsString());
		}
//		if (object.has("error")) return UUID.fromString(uuid.toString());
		if (object.has("error")) return null;
		return UUID.fromString(addDashes(object.getAsJsonPrimitive("id").getAsString()));
	}
}
