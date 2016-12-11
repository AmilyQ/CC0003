package codehelper.template;

import java.io.IOException;

import codehelper.FunctionUtils;

public class DaoTemplate {
	public static final StringBuffer template = new StringBuffer("");

	public static final void doInit() {
		template.append("package #packageName#.dao;\n");
		template.append("import com.cares.asis.mybatis.base.BaseDao;\n");
		template.append("import #packageName#.entity.#entityName#VO;\n");
		template.append("public interface #entityName#Mapper extends BaseDao<#entityName#VO> {\n");
		template.append("}\n");
	}

	public static final void doCodeGenerate(String packageName, String entityName) throws IOException {
		doInit();
		String content = template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName);
		FunctionUtils.writeFile("dao", content, entityName + "Mapper.java");
	}

	public static void main(String[] args) {
	}

}
