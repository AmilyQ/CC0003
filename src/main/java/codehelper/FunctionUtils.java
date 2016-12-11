package codehelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FunctionUtils {
	private static String system_dir = "D:";

	public static String toFirstLowerCase(String s) {
		if (null == s) {
			return "";
		}
		return Character.toLowerCase(s.charAt(0)) + s.substring(1).toString();
	}

	public static String toFirstUpperCase(String s) {
		if (null == s) {
			return "";
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1).toString();
	}

	public static String convertToJavaFormat(String s) {
		String[] params = s.split("_");
		if (params.length == 1) {
			return s;
		}
		StringBuffer results = new StringBuffer("");
		for (int i = 0, l = params.length; i < l; i++) {
			if (i == 0) {
				results.append(params[i].toLowerCase());
			} else {
				results.append(FunctionUtils.toFirstUpperCase(params[i].toLowerCase()));
			}
		}
		return results.toString();
	}

	public static String convertToJdbcType(String s) throws Exception {
		if (s == null) {
			throw new Exception("获取字段类型错误!");
		}
		if (s.equals("String")) {
			return "VARCHAR";
		} else if (s.equals("Double") || s.equals("Long") || s.equals("Integer")) {
			return "DECIMAL";
		} else if (s.equals("Date")) {
			return "TIMESTAMP";
		} else {
			return s;
		}
	}

	public static String convertJdbcType(String s, int scale) throws Exception {
		if (s == null) {
			throw new Exception("获取字段类型错误!");
		}
		s = s.split("\\.")[2];
		if (s.toLowerCase().equals("timestamp")) {
			return "Date";
		} else if (s.equals("BigDecimal")) {
			if (scale <= 0) {
				return "Long";
			} else {
				return "Double";
			}
		} else {
			return s;
		}
	}

	public static void writeFile(String doc, String content, String fileName) throws IOException {
		File md = new File(system_dir + "/CaresWeb-CodeHelper/" + doc);
		if (!md.exists()) {
			md.mkdir();
		}
		File file = new File(system_dir + "/CaresWeb-CodeHelper/" + doc + "/" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		writer.write(content);
		writer.close();
	}

	public static StringBuffer readFile(String fileName) throws Exception {
		File file = new File(fileName);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			rs.append(lineTxt);
		}
		read.close();
		return rs;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(convertToJavaFormat("assigne_type"));
	}

}
