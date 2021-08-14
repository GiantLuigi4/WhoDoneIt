package tfc.whodoneit.util;

import com.google.gson.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class GeneralUtils {
	private static final Gson gson = new GsonBuilder().setLenient().create();
	
	public static JsonObject webRequest(String url) {
//		"https://playerdb.co/api/player/minecraft/uuid";
		try {
			return gson.fromJson(readUrl(url), JsonObject.class);
		} catch (JsonParseException ignored) {
			return new JsonObject();
		} catch (IOException exception) {
			if (exception.getMessage().contains("response code: 400")) {
				return null;
			}
			exception.printStackTrace();
		} catch (Throwable ignored) {
		}
		return null;
	}
	
	public static String[] splitUUID(String uuid) {
		String[] array = new String[5];
		array[0] = uuid.substring(0, 8);
		uuid = uuid.substring(8);
		array[1] = uuid.substring(0, 4);
		uuid = uuid.substring(4);
		array[2] = uuid.substring(0, 4);
		uuid = uuid.substring(4);
		array[3] = uuid.substring(0, 4);
		uuid = uuid.substring(4);
		array[4] = uuid;
		return array;
	}
	
	public static boolean checkUUID(String uuid) {
		if (uuid.split("-").length != 5) return false;
		if (splitUUID(uuid.replace("-", ""))[4].length() != 12) return false;
		return true;
	}
	
	public static String addDashes(String trimmedUUID) {
		StringBuilder builder = new StringBuilder();
		for (String s : splitUUID(trimmedUUID)) {
			builder.append(s).append('-');
		}
		return builder.substring(0, builder.length() - 1);
	}
	
	public static String readUrl(String urlString) throws IOException {
		BufferedReader reader = null;
		try {
//			URL obj = new URL(urlString);
//			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//			con.setRequestMethod("GET");
//			con.setRequestProperty("Content-Type", "");
//			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder builder = new StringBuilder();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				builder.append(chars, 0, read);

			return builder.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	public static UUID getRootCause(Entity cause) {
		if (cause instanceof PlayerEntity)
			return cause.getUuid();
		else if (cause instanceof TameableEntity)
			return ((TameableEntity) cause).getOwnerUuid();
		else if (cause instanceof MobEntity)
			return getRootCause(((MobEntity) cause).getTarget());
		else if (cause instanceof ProjectileEntity)
			return getRootCause(((ProjectileEntity) cause).getOwner());
		return null;
	}
	
	public static UUID getCause(Entity cause) {
		if (cause instanceof PlayerEntity)
			return cause.getUuid();
		else if (cause instanceof TameableEntity)
			return ((TameableEntity) cause).getOwnerUuid();
		else if (cause instanceof MobEntity) {
			if ((((MobEntity) cause).getTarget()) instanceof PlayerEntity)
				return (((MobEntity) cause).getTarget()).getUuid();
		} else if (cause instanceof ProjectileEntity) {
			if (((ProjectileEntity) cause).getOwner() instanceof PlayerEntity) {
				return ((ProjectileEntity) cause).getOwner().getUuid();
			}
		}
		return null;
	}
}
