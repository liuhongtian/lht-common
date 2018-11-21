package com.bhz.lht.mystudy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;

public class YamlTest {

	public static void main(String[] args) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		Iterable<Object> ret = yaml.loadAll(new FileReader(new File("src/main/resource/test.yaml")));
		int i = 0;
		for(Object r: ret) {
			i++;
			System.out.println(i + ": " + JSON.toJSONString(r, true));
		}
	}

}
