package tfc.whodoneit.util;

import com.google.gson.JsonObject;

import java.util.UUID;

import static tfc.whodoneit.util.GeneralUtils.*;

public class NameGetter {
	protected static ExpiringCache<UUID, String> nameCache = new ExpiringCache<>(NameGetter::getName, 30000);
	
	public static String getNameFor(UUID name) {
		return nameCache.get(name);
	}
	
	protected static String getName(UUID culprit) {
		JsonObject object = webRequest("https://playerdb.co/api/player/minecraft/" + culprit);
		if (object == null) return culprit.toString();
		if (!object.getAsJsonPrimitive("success").getAsBoolean()) return culprit.toString();
		return object.getAsJsonObject("data").getAsJsonObject("player").getAsJsonPrimitive("username").getAsString();
	}
}
