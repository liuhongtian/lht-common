package com.bhz.lht.mystudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class FileWriter {
	private static final int BUFFER_LENGTH = 100;

	public static void main(String[] args) {
		String imgName = "/Users/liuhongtian/Downloads/DSC00357.jpg";
		byte[] buff = new byte[BUFFER_LENGTH];

		String[] fileNames = { "/Users/liuhongtian/Downloads/test1", "/Users/liuhongtian/Downloads/test2" };

		Arrays.stream(fileNames, 0, fileNames.length).forEach(f -> {
			try (InputStream is = new FileInputStream(new File(imgName))) {
				File file = new File(f);
				int len = 0;
				try (OutputStream os = new FileOutputStream(file);) {
					while ((len = is.read(buff)) != -1) {
						System.out.println("read: " + len);
						os.write(buff, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});

		byte[] bytes = { (byte) 0xe6, (byte) 0xa1, (byte) 0xe6, (byte) 0xa1, (byte) 0xe6, (byte) 0xa1, (byte) 0xe6,
				(byte) 0xa1 };
		try (OutputStream os3 = new FileOutputStream(new File("/Users/liuhongtian/Downloads/test3"))) {
			os3.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done.");
	}

}
