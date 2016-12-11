package codehelper.template;

import java.io.IOException;

import codehelper.FunctionUtils;

public class ServiceTemplate {
	public static final StringBuffer template = new StringBuffer("");

	public static final void doInit() {
		template.append("package #packageName#.service;\n");
		template.append("import com.cares.asis.mybatis.base.BaseService;\n");
		template.append("import #packageName#.entity.#entityName#VO;\n");
		template.append("public interface #entityName#Service extends BaseService<#entityName#VO> {\n");
		template.append("}\n");
	}

	public static final void doCodeGenerate(String packageName, String entityName) throws IOException {
		doInit();
		String content = template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName);
		FunctionUtils.writeFile("service", content, entityName + "Service.java");
	}

	public static void main(String[] args) {
	}
}
