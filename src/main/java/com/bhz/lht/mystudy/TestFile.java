package com.bhz.lht.mystudy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestFile {

	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));
		
		String line = null;
		
		System.out.println("go ...");
		
		while(true) {
			line = reader.readLine();
			if(line == null) {
				break;
			}
			if(line.startsWith("spool")) {
				continue;
			}
			if(line.startsWith("prompt")) {
				continue;
			}
			if(line.startsWith("comment")) {
				continue;
			}
			if(line.startsWith("  is")) {
				continue;
			}
			writer.write(line);
			writer.newLine();
		}
		
		reader.close();
		writer.close();
		
		System.out.println("done.");

	}

}
