package tfc.whodoneit.util;

import net.minecraft.util.Pair;
import net.minecraft.util.Util;

import java.util.HashMap;
import java.util.function.Function;

public class ExpiringCache<T, V> {
	private final HashMap<T, Pair<Long, V>> map = new HashMap<>();
	private final Function<T, V> defaultVal;
	private final long expiryTime;
	
	public ExpiringCache(Function<T, V> defaultVal, long expiryTime) {
		this.defaultVal = defaultVal;
		this.expiryTime = expiryTime;
	}
	
	public V get(T key) {
		Pair<Long, V> val = map.getOrDefault(key, null);
		if (val != null) {
			if (Util.getEpochTimeMs() - expiryTime > val.getLeft()) {
				map.remove(key);
				val = null;
			}
		}
		if (val == null) {
			val = new Pair<>(Util.getEpochTimeMs(), defaultVal.apply(key));
			map.put(key, val);
		}
		return val.getRight();
	}
}
