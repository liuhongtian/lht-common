package com.bhz.lht.mystudy;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MapStudy {

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");

		System.out.println(
				map.keySet().stream().map(n -> n + ": " + map.get(n)).reduce((m, n) -> m + "; " + n).orElse("null"));

		Stream.of("a", "b", "c").forEach(System.out::println);
	}

}
