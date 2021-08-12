package tfc.whodoneit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

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
		} catch (Throwable ignored) {
			return null;
		}
	}
	public static String readUrl(String urlString) throws IOException {
		BufferedReader reader = null;
		try {
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
