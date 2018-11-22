package net.lht.common.misc;

import java.util.UUID;

public class UUIDUtils {

	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String rawUUID() {
		return UUID.randomUUID().toString().toLowerCase();
	}
}
