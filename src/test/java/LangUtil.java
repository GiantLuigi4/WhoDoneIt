import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import tfc.whodoneit.registry.ChangeReasons;

import java.util.Scanner;

public class LangUtil {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		// TODO: make this read the existing entries in en_us.json and not repeat them
		JsonObject jsonObject = new JsonObject();
		ChangeReasons.forEach((pair)->{
			System.out.println(pair.getLeft());
//			while (!s.hasNextLine()) {}
			String text = s.nextLine();
			jsonObject.addProperty(pair.getLeft().toString().replace(":", ".blocklog."), text);
		});
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(jsonObject));
	}
}
