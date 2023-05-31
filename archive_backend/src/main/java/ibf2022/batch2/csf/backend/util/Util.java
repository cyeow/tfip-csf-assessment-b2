package ibf2022.batch2.csf.backend.util;

import java.util.UUID;

public class Util {
    
	public static String generateUUID() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
