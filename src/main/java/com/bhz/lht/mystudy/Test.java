package com.bhz.lht.mystudy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Test {

	public static void main(String[] args) {
		List<TestPO> ia = new ArrayList<>();
		ia.add(new TestPO(1, "liuhongtian", 40, 0));
		ia.add(new TestPO(2, "zhangwan", 33, 1));
		ia.add(new TestPO(4, "liji", 32, 1));
		ia.add(new TestPO(5, "liushuwei", 65, 0));
		ia.add(new TestPO(6, "zhangrongzhen", 64, 1));
		ia.add(new TestPO(7, "zhangning", 42, 1));
		ia.add(new TestPO(3, "liuziqiao", 38, 0));

		// predicate
		Predicate<TestPO> predicate = (TestPO n) -> (n.getAge() < 40);

		// map
		Function<TestPO, String> map = n -> n.getName();

		// reduce
		BinaryOperator<String> reduce = (String m, String n) -> (m.concat("," + n));

		// .parallel()
		Optional<String> res = ia.stream().filter(predicate).map(map).reduce(reduce);

		System.out.println("不到40岁的人 = " + res.orElse("无"));

		predicate = (TestPO n) -> (n.getS() == 1);

		long c = ia.stream().filter(predicate).count();
		System.out.printf("女性人数 = %d%n", c);

		List<TestPO> l = ia.stream().filter(predicate).collect(Collectors.toList());
		l.stream().forEach(n -> System.out.println(n.getName()));

		Map<Integer, List<TestPO>> dd = ia.stream().collect(Collectors.groupingBy(n -> n.getS()));
		dd.forEach((key, list) -> {
			System.out.println("分性别人数：\n性别 = " + key + "\t人数 = " + list.stream().parallel().count());
		});

		Optional<Integer> sum = ia.stream().filter(predicate).map(n -> n.getAge()).reduce((m, n) -> (m + n));
		System.out.println("女性年龄合计 = " + sum.orElse(-1));
		
		System.out.println("Done ok.");
	}

}
