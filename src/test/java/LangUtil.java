import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tfc.whodoneit.registry.ChangeReasons;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

public class LangUtil {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
		
		JsonObject oldLang = new JsonObject();
		
		try {
			InputStream stream = new FileInputStream("src/main/resources/assets/whodoneit/lang/en_us.json");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int b;
			while ((b = stream.read()) != -1) outputStream.write(b);
			oldLang = gson.fromJson(outputStream.toString(), JsonObject.class);
		} catch (Throwable ignored) {
			ignored.printStackTrace();
		}
		
		Scanner s = new Scanner(System.in);
		// TODO: make this read the existing entries in en_us.json and not repeat them
		JsonObject jsonObject = new JsonObject();
		
		for (Map.Entry<String, JsonElement> stringJsonElementEntry : oldLang.entrySet()) jsonObject.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
		
		ChangeReasons.forEach((pair)->{
			if (!jsonObject.has(pair.getLeft().toString())) {
				System.out.println(pair.getLeft());
//				while (!s.hasNextLine()) {}
				String text = s.nextLine();
				jsonObject.addProperty(pair.getLeft().toString().replace(":", ".blocklog."), text);
			}
		});
		System.out.println(gson.toJson(jsonObject));
	}
}
