package codehelper.template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import codehelper.DBUtil;
import codehelper.FunctionUtils;

public class VOTemplate {
	public static final StringBuffer template = new StringBuffer("");

	public static final void doInit() {
		template.append("package #packageName#.entity;\n");
		template.append("import com.cares.asis.mybatis.base.BaseEntity;\n");
		template.append("import java.util.Date;\n");
		template.append("public class #entityName#VO extends BaseEntity {\n");
		template.append("#property#\n");
		template.append("}\n");
	}

	public static final void doCodeGenerate(String tableName, String packageName, String entityName) throws IOException {
		doInit();
		StringBuffer obj = new StringBuffer();
		StringBuffer method = new StringBuffer();
		DBUtil db = new DBUtil();
		Map<String, String> map = db.fetchTableInfo(tableName);
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next(), property = FunctionUtils.convertToJavaFormat(key);
			obj.append("private " + map.get(key) + " " + property + ";\n");
			method.append("public " + map.get(key) + " get" + FunctionUtils.toFirstUpperCase(property) + "(){return " + property + ";}\n");
			method.append("public void set" + FunctionUtils.toFirstUpperCase(property) + "(" + map.get(key) + " " + property + "){this." + property + "=" + property + ";}\n");
		}
		obj.append(method);
		String content = template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName).replaceAll("#property#", obj.toString());
		FunctionUtils.writeFile("entity", content, entityName + "VO.java");
	}

	public static void main(String[] args) throws SQLException {
	}
}
