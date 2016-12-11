package codehelper.template;

import java.util.Iterator;
import java.util.Map;

import codehelper.DBUtil;
import codehelper.FunctionUtils;

public class MappingTemplate {
	public static final StringBuffer template = new StringBuffer("");

	public static void main(String[] args) throws Exception {
	}

	public static final void doInit() {
		template.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		template.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n");
		template.append("<mapper namespace=\"#packageName#.dao.#entityName#Mapper\">\n");
		template.append("<resultMap id=\"#entityName#Map\" type=\"#packageName#.entity.#entityName#VO\">\n");
		template.append("#tableMapping#\n");
		template.append("</resultMap>\n");

		template.append("<sql id=\"whereStatement\">\n");
		template.append("<trim prefix=\"WHERE\" prefixOverrides=\"AND\">\n");
		template.append("1=1\n");
		template.append("<if test=\"#objId# != null and #objId# != ''\">\n");
		template.append("and #objId# = ${#objId#}\n");
		template.append("</if>\n");
		template.append("</trim>\n");
		template.append("</sql>\n");

		template.append("<select id=\"query\" resultMap=\"#entityName#Map\"> select * from ( select ROWNUM AS rowno,t.* from (select * from  #tableName# <include refid=\"whereStatement\"/> ) t WHERE ROWNUM <![CDATA[<=]]> #{end} ) where rowno <![CDATA[>]]> #{start} </select>\n");
		template.append("<select id=\"queryPage\" resultType=\"java.lang.Integer\" parameterType=\"java.util.Map\"> select count(*) from #tableName# <include refid=\"whereStatement\"/></select>\n");
		template.append("<select id=\"find\" resultMap=\"#entityName#Map\" parameterType=\"java.lang.Long\">\n");
		template.append("select * from #tableName# where #objTableId#= #{id}</select>\n");
		template.append("<delete id=\"doDelete\" parameterType=\"java.lang.Long\">\n");
		template.append("delete from #tableName# where #objTableId# = #{#objId#}</delete>\n");
		template.append("<update id=\"doUpdate\" parameterType=\"#packageName#.entity.#entityName#VO\">\n");
		template.append("update #tableName# set #updateMapping# where #objTableId#=#{#objId#}</update>\n");
		template.append("<insert id=\"doAdd\" parameterType=\"#packageName#.entity.#entityName#VO\" keyProperty=\"#objId#\">\n");
		template.append("<selectKey resultType=\"java.lang.Long\" order=\"BEFORE\" keyProperty=\"#objId#\" >\n");
		template.append("select  Seq_#tableName#.nextval as #objId# from dual");
		template.append("</selectKey>");
		template.append("insert into #tableName# #insertMapping# </insert>\n");
		template.append("</mapper>\n");
	}

	public static final void doCodeGenerate(String tableName, String packageName, String entityName) throws Exception {
		doInit();
		StringBuffer tableMapping = new StringBuffer(), updateMapping = new StringBuffer();
		StringBuffer insertMappingF = new StringBuffer("("), insertMappingL = new StringBuffer("(");
		DBUtil db = new DBUtil();
		String objTableId = db.fetchTablePrimaryKey(tableName);
		tableMapping.append("<id column=\"" + objTableId + "\" property=\"" + FunctionUtils.convertToJavaFormat(objTableId) + "\" jdbcType=\"DECIMAL\" />\n");
		Map<String, String> map = db.fetchTableInfo(tableName);
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next(), property = FunctionUtils.convertToJavaFormat(key);
			insertMappingF.append(key + ",\n");
			insertMappingL.append("#{" + FunctionUtils.convertToJavaFormat(key) + ",jdbcType=" + FunctionUtils.convertToJdbcType(map.get(key)) + "},\n");
			if (!key.equals(objTableId)) {
				tableMapping.append("<result column=\"" + key + "\" property=\"" + FunctionUtils.convertToJavaFormat(key) + "\" jdbcType=\"" + FunctionUtils.convertToJdbcType(map.get(key)) + "\" />\n");
				updateMapping.append("" + key + " = #{" + FunctionUtils.convertToJavaFormat(key) + ",jdbcType=" + FunctionUtils.convertToJdbcType(map.get(key)) + "},\n");
			}
		}
		insertMappingF.deleteCharAt(insertMappingF.length() - 2).append(")\n");
		insertMappingL.deleteCharAt(insertMappingL.length() - 2).append(")\n");
		updateMapping.deleteCharAt(updateMapping.length() - 2);
		String content = template.toString().replaceAll("#packageName#", packageName).replaceAll("#entityName#", entityName).replaceAll("#objTableId#", objTableId).replaceAll("#objId#", FunctionUtils.convertToJavaFormat(objTableId)).replaceAll("#tableName#", tableName.toUpperCase()).replaceAll("#tableMapping#", tableMapping.toString()).replaceAll("#updateMapping#", updateMapping.toString()).replaceAll("#insertMapping#", insertMappingF.append(" values ").append(insertMappingL).toString());
		FunctionUtils.writeFile("mapping", content, entityName + "Mapper.xml");
	}

}
