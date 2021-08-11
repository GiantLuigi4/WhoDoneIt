package tfc.whodoneit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
	private static String readUrl(String urlString) throws IOException {
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
}
