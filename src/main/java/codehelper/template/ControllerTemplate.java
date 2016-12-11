package codehelper.template;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import codehelper.DBUtil;
import codehelper.FunctionUtils;

public class ControllerTemplate {
	public static final StringBuffer template = new StringBuffer("");

	public static final void doInit() {
		template.append("package #packageName#.controller;\n");
		template.append("import java.util.List;\n");
		template.append("import java.util.ArrayList;\n");
		template.append("import java.util.HashMap;\n");
		template.append("import java.util.Map;\n");
		template.append("import org.springframework.beans.factory.annotation.Autowired;\n");
		template.append("import #packageName#.entity.#entityName#VO;\n");
		template.append("import #packageName#.service.#entityName#Service;\n");
		template.append("import com.cares.asis.mybatis.base.BaseAction;\n");
		template.append("public class #entityName#Controller extends BaseAction<#entityName#VO> {\n");
		template.append("@Autowired\n");
		template.append("private #entityName#Service #lowerEntityName#Service;\n");
		template.append("protected String Msg;\n");
		template.append("protected int start = 0;\n");
		template.append("protected List list;\n");
		template.append("protected boolean success;\n");
		template.append("public String query() {\n");
		template.append("Map searchMap = new HashMap();\n");
		template.append("searchMap.put(\"start\", start);\n");
		template.append("searchMap.put(\"end\", pagesize + start);\n");
		template.append("list=#lowerEntityName#Service.query(searchMap);\n");
		template.append("total =#lowerEntityName#Service.queryPage(searchMap);\n");
		template.append("success = true;\n");
		template.append("return SUCCESS;\n");
		template.append("}\n");
		template.append("public String doAdd(){\n");
		template.append("createEntityBySuperclass();\n");
		template.append("try { \n");
		template.append("#lowerEntityName#Service.doAdd(entity);\n");
		template.append("} catch (Exception e){ \n");
		template.append("success=false;\n");
		template.append("Msg=OP_F;\n");
		template.append("return SUCCESS;\n");
		template.append("} \n");
		template.append("success=true; \n");
		template.append("Msg=OP_S; \n");
		template.append("return SUCCESS;\n}\n");
		template.append("public String doUpdate(){\n");
		template.append("#entityName#VO vo=#lowerEntityName#Service.find(Long.parseLong(getParamByName(\"id\")));\n");
		template.append("createEntityByDefinedclass(vo);");
		template.append("try { \n");
		template.append("#lowerEntityName#Service.doUpdate(vo);\n");
		template.append("} catch (Exception e){ \n");
		template.append("success=false;\n");
		template.append("Msg=OP_F;\n");
		template.append("return SUCCESS;\n");
		template.append("} \n");
		template.append("success=true; \n");
		template.append("Msg=OP_S; \n");
		template.append("return SUCCESS;\n}\n");
		template.append("public String doDelete(){\n");
		template.append("try { \n");
		template.append("#lowerEntityName#Service.doDelete(Long.parseLong(getParamByName(\"id\")));\n");
		template.append("} catch (Exception e){ \n");
		template.append("success=false;\n");
		template.append("Msg=OP_F;\n");
		template.append("return SUCCESS;\n");
		template.append("} \n");
		template.append("success=true; \n");
		template.append("Msg=OP_S; \n");
		template.append("return SUCCESS;\n}\n");
		template.append("public String find(){\n");
		template.append("list=new ArrayList<#entityName#VO>();\n");
		template.append("list.add(#lowerEntityName#Service.find(Long.parseLong(getParamByName(\"id\"))));\n");
		template.append("success = true;\n");
		template.append("return SUCCESS;\n}\n");
		template.append("public String getMsg() {return Msg;\n}\n");
		template.append("public int getStart()  {return start;\n}\n");
		template.append("public List getList() {return list;\n}\n");
		template.append("public boolean isSuccess() {return success;\n}\n");
		template.append("public void setStart(int start) {this.start = start;\n}\n");
		template.append("}");
	}

	private static String getTypeParam(String name, String type) {
		String s = "";
		if ("Long".equals(type)) {
			s = "Long.parseLong(getParamByName(\"" + name + "\"))";
		} else if ("Double".equals(type)) {
			s = "Double.parseDouble(getParamByName(\"" + name + "\"))";
		} else if ("Date".equals(type)) {
			s = "new Date()";
		} else {
			s = "getParamByName(\"" + name + "\")";
		}
		return s;
	}

	public static final void doCodeGenerate(String tableName, String packageName, String entityName) throws IOException {
		doInit();
		StringBuffer entityPro = new StringBuffer();
		DBUtil db = new DBUtil();
		String objTableId = db.fetchTablePrimaryKey(tableName);
		Map<String, String> map = db.fetchTableInfo(tableName);
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next(), property = FunctionUtils.convertToJavaFormat(key), type = map.get(key);
			if (!objTableId.equals(key)) {
				entityPro.append("vo.set" + FunctionUtils.toFirstUpperCase(property) + "(" + getTypeParam(property, type) + ");\n");
			}
		}

		String content = template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName).replaceAll("#entityPro#", entityPro.toString()).replaceAll("#lowerEntityName#", FunctionUtils.toFirstLowerCase(entityName));
		FunctionUtils.writeFile("controller", content, entityName + "Controller.java");
	}

	public static void main(String[] args) throws IOException {
	}

}
