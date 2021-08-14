import tfc.whodoneit.util.GeneralUtils;
import tfc.whodoneit.util.NameGetter;

import java.util.UUID;

public class WebRequestTest {
	public static void main(String[] args) {
//		System.out.println(GeneralUtils.webRequest("https://playerdb.co/api/player/minecraft/b36fdbca-7336-4e5a-8e08-045c169ee07e"));
//		System.out.println(UUID.fromString(GeneralUtils.addDashes("b36fdbca73364e5a8e08045c169ee07e")));
		System.out.println("b36fdbca-7336-4e5a-8e08-045c169ee07e");
		System.out.println(UUID.fromString("b36fdbca-7336-4e5a-8e08-045c169ee07e"));
		System.out.println(GeneralUtils.addDashes("b36fdbca73364e5a8e08045c169ee07e"));
		System.out.println(UUID.fromString(GeneralUtils.addDashes("b36fdbca73364e5a8e08045c169ee07e")));
		System.out.println(NameGetter.getNameFor(UUID.fromString(GeneralUtils.addDashes("b36fdbca73364e5a8e08045c169ee07e"))));
		System.out.println(NameGetter.getUUIDFor("GoldPichu69"));
	}
}
