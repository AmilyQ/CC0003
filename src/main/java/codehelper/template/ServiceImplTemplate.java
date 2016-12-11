package codehelper.template;

import java.io.IOException;

import codehelper.FunctionUtils;


public class ServiceImplTemplate {
	public static final StringBuffer template=new StringBuffer("");
	
	public static final void doInit(){
		template.append("package #packageName#.service.impl;\n");
		template.append("import java.util.List;\n");
		template.append("import java.util.Map;\n");
		template.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		template.append("import org.springframework.stereotype.Service;\n");
		template.append("import #packageName#.dao.#entityName#Mapper;\n");
		template.append("import #packageName#.entity.#entityName#VO;\n");
		template.append("import #packageName#.service.#entityName#Service;\n");
		template.append("@Service\n");
		template.append("public class #entityName#ServiceImpl implements #entityName#Service {\n");
		template.append("@Autowired\n");
		template.append("private #entityName#Mapper #lowerEntityName#Mapper;\n");
		template.append("@Override\n");
		template.append("public void doAdd(#entityName#VO entity) {\n");
		template.append("#lowerEntityName#Mapper.doAdd(entity);\n");
		template.append("}\n");
		template.append("@Override\n");
		template.append("public void doDelete(Long id) {\n");
		template.append("#lowerEntityName#Mapper.doDelete(id);\n");
		template.append("}\n");
		template.append("@Override\n");
		template.append("public void doUpdate(#entityName#VO entity) {\n");
		template.append("#lowerEntityName#Mapper.doUpdate(entity);\n");
		template.append("}\n");
		template.append("@Override\n");
		template.append("public #entityName#VO find(Long id) {\n");
		template.append("return #lowerEntityName#Mapper.find(id);\n");
		template.append("}\n");
		template.append("@Override\n");
		template.append("public List<#entityName#VO> query(Map searchMap) {\n");
		template.append("return #lowerEntityName#Mapper.query(searchMap);\n");
		template.append("}\n");
		template.append("@Override\n");
		template.append("public Integer queryPage(Map searchMap) {\n");
		template.append("return #lowerEntityName#Mapper.queryPage(searchMap);\n");
		template.append("}}\n");
	}
	public static final void doCodeGenerate(String packageName,String entityName) throws IOException{
		doInit();
		String content= template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName).replaceAll("#lowerEntityName#", FunctionUtils.toFirstLowerCase(entityName));
		FunctionUtils.writeFile("service/impl",content, entityName+"ServiceImpl.java");
	}
	public static void main(String[] args) {
	}
}
