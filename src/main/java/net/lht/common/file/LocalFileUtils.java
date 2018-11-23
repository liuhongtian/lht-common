package net.lht.common.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(LocalFileUtils.class);

	private static String FIX = File.separator;

	/**
	 * 删除指定文件
	 * 
	 * @param file 待删除的文件
	 */
	public static void deleteFile(File file) {
		if (file != null) {
			if (file.exists()) {
				file.delete();
			} else {
				logger.warn("try to delete file: " + file.getName() + " not exists");
			}
		}
	}

	/**
	 * 将指定文件移动到指定位置
	 * 
	 * @param srcPath  源路径
	 * @param destPath 目标路径
	 */
	public static void moveFile(String srcPath, String destPath) {
		File file = new File(srcPath);
		File destDir = new File(destPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].renameTo(new File(destPath + FIX + files[i].getName())); // TODO
			}
		} else {
			file.renameTo(new File(destPath + FIX + file.getName()));
		}
	}

	/**
	 * 获取指定目录下的文件名列表，只包含文件，不包括目录
	 * 
	 * @param localPath 指定目录
	 * @return 文件列表
	 */
	public static List<String> getFileNameList(String localPath) {
		List<String> fileNameList = new ArrayList<String>();
		File file = new File(localPath);
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					fileNameList.add(files[i].getName());
				}
			}
		}
		return fileNameList;
	}

	/**
	 * 根据文件名模式获取指定目录下的文件名列表，只包含文件，不包括目录
	 * 
	 * @param localPath 指定目录
	 * @param regex     文件名模式
	 * @return 文件列表
	 */
	public static List<String> getFileNameList(String localPath, String regex) {
		List<String> fileNames = getFileNameList(localPath);
		if (regex == null || regex.length() == 0) {
			return fileNames;
		}
		List<String> fileNameList = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		for (String fileName : fileNames) {
			Matcher m = p.matcher(fileName);
			if (m.find()) {
				fileNameList.add(fileName);
			}
		}
		return fileNameList;
	}

	/**
	 * 将文件内容读入到字符串（主要用于读取配置文件，不适用于大文件）
	 * 
	 * @param fileName 文件名
	 * @return 文件内容
	 */
	public static String readToString(String fileName) {
		StringBuffer buff = new StringBuffer();
		try (InputStream is = new FileInputStream(fileName);
				InputStreamReader ir = new InputStreamReader(is, "UTF-8");
				BufferedReader reader = new BufferedReader(ir);) {
			String line;
			while ((line = reader.readLine()) != null) {
				buff.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	/**
	 * 将文件内容读入到字符串（主要用于读取配置文件，不适用于大文件），文件位于类路径中
	 * 
	 * @param fileName 文件名
	 * @return 文件内容
	 */
	public static String readToStringFromClasspath(String fileName) {
		StringBuffer buff = new StringBuffer();
		try (InputStream is = LocalFileUtils.class.getClassLoader().getResourceAsStream(fileName);
				InputStreamReader ir = new InputStreamReader(is, "UTF-8");
				BufferedReader reader = new BufferedReader(ir);) {
			String line;
			while ((line = reader.readLine()) != null) {
				buff.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buff.toString();
	}

	/**
	 * 在指定路径创建文件并写入指定内容
	 * 
	 * @param path    文件全路径
	 * @param content 文件内容
	 * @return
	 */
	public static boolean writeFile(String path, String content) {
		boolean result = false;

		if (path.endsWith(FIX)) {
			path = path.substring(0, path.lastIndexOf(FIX));
		}

		File file = new File(path);

		if (file.exists()) {
			if (file.isDirectory()) {
				logger.error("the path is directory: " + path);
				return result;
			} else {
				file.delete();
			}
		}

		try {
			String dir = path.substring(0, path.lastIndexOf(FIX));
			if (dir != null && dir.length() > 0) {
				mkdirs(dir);
			}
			file.createNewFile();
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			pw.print(content);
			pw.close();
			result = true;
		} catch (IOException e) {
			logger.error("create file failed: " + path, e);
		}

		return result;
	}

	/**
	 * 删除指定位置的文件或目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delete(String path) {
		boolean result = true;

		logger.debug("delete: " + path);

		File file = new File(path);

		if (!file.exists()) {
			logger.warn("delete file / directory not found: " + path);
			return result;
		}

		delete(file);

		return result;
	}

	/**
	 * 级联创建路径中的所有目录
	 * 
	 * @param path 资源路径
	 */
	private static void mkdirs(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 删除指定的文件或目录
	 * 
	 * @param path
	 */
	private static void delete(File path) {
		// 删除整个目录
		if (path.isDirectory()) {
			File[] subs = path.listFiles();
			List<File> lf = new ArrayList<>();
			for (File s : subs) {
				lf.add(s);
			}
			lf.stream().forEach(f -> delete(f)); // 删除目录下的所有内容
			path.delete(); // 删除目录本身
		}

		// 删除单个文件
		else {
			path.delete();
		}
	}

	public static void main(String[] args) {
		logger.debug(FIX);
	}

}
