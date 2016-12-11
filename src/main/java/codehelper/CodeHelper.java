package codehelper;

import codehelper.template.ControllerTemplate;
import codehelper.template.DaoTemplate;
import codehelper.template.JSTemplate;
import codehelper.template.MappingTemplate;
import codehelper.template.ServiceImplTemplate;
import codehelper.template.ServiceTemplate;
import codehelper.template.VOTemplate;

public class CodeHelper {
	private static boolean isCreateController = true;
	private static boolean isCreateDao = true;
	private static boolean isCreateEntity = true;
	private static boolean isCreateMapping = true;
	private static boolean isCreateService = true;
	private static boolean isCreateServiceImpl = true;
	private static boolean isCreateJs = false;

	private static String packageName = "com.cares.asis.test";// 引用路径
	private static String entityName = "DAORU";// 对象名称
	private static String tableName = "XT_DAORU";// 表名

	public static void main(String[] args) throws Exception {
		createCode();
	}

	public static void createCode() throws Exception {
		if (isCreateController) {
			ControllerTemplate.doCodeGenerate(tableName, packageName, entityName);
		}
		if (isCreateDao) {
			DaoTemplate.doCodeGenerate(packageName, entityName);
		}
		if (isCreateEntity) {
			VOTemplate.doCodeGenerate(tableName, packageName, entityName);
		}
		if (isCreateMapping) {
			MappingTemplate.doCodeGenerate(tableName, packageName, entityName);
		}
		if (isCreateService) {
			ServiceTemplate.doCodeGenerate(packageName, entityName);
		}
		if (isCreateServiceImpl) {
			ServiceImplTemplate.doCodeGenerate(packageName, entityName);
		}
		if (isCreateJs) {
			JSTemplate.doCodeGenerate(tableName, entityName);
		}
	}
}
