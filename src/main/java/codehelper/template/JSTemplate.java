package codehelper.template;

import java.util.Iterator;
import java.util.Map;

import codehelper.DBUtil;
import codehelper.FunctionUtils;

public class JSTemplate {
	public static void main(String[] args) throws Exception {
		doCodeGenerate("LOG_ERROR","LogError");
	}
	public static final String refName="obj";
	public static final StringBuffer template = new StringBuffer("");
	public static final void doCodeGenerate(String tableName, String entityName) throws Exception {
		doInit();
		StringBuffer columnTitle=new StringBuffer(),filedForm1=new StringBuffer(),filedForm2=new StringBuffer(),jsonFormat=new StringBuffer();
		
		DBUtil db = new DBUtil();
		String objTableId=db.fetchTablePrimaryKey(tableName);
		Map<String, String> map = db.fetchTableInfo(tableName);
		Iterator<String> iter = map.keySet().iterator();
		
		jsonFormat.append("Ext.define('"+entityName+"VO', {extend : 'Ext.data.Model',fields : [");
		jsonFormat.append("{name : 'id',convert : function(v, r) {return r.get('"+FunctionUtils.convertToJavaFormat(objTableId)+"');}},");
		int countField=0;
		while (iter.hasNext()) {
			String key = iter.next(), property = FunctionUtils.convertToJavaFormat(key);
			jsonFormat.append("{name:'"+property+"'},\n");
			if(!key.equals(objTableId)){
				columnTitle.append("{header : '"+key+"',dataIndex : '"+property+"',xtype:'"+jsColumn(map.get(key))+"'},\n");
				if(countField%2==0){
					filedForm1.append("{columnWidth : .5,fieldLabel : '"+key+"',name : '"+property+"',xtype : '"+jsComp(map.get(key))+"'},\n");
				}else{
					filedForm2.append("{columnWidth : .5,fieldLabel : '"+key+"',name : '"+property+"',xtype : '"+jsComp(map.get(key))+"'},\n");
				}
				countField++;
			}
		}
		columnTitle.deleteCharAt(columnTitle.length()-2);
		jsonFormat.deleteCharAt(jsonFormat.length()-2);
		filedForm1.deleteCharAt(filedForm1.length()-2);
		filedForm2.deleteCharAt(filedForm2.length()-2);
		jsonFormat.append("],idProperty : '"+FunctionUtils.convertToJavaFormat(objTableId)+"'});");
		System.out.println(jsonFormat.toString());
		String content=template.toString().replaceAll("#entityName#", entityName)
		.replaceAll("#columnTitle#", columnTitle.toString())
		.replaceAll("#filedForm1#", filedForm1.toString())
		.replaceAll("#filedForm2#", filedForm2.toString());
		FunctionUtils.writeFile("",content, entityName.toLowerCase()+".js");
	}
	private static String jsComp(String type){
		if("Date".equals(type)){
			return "datefield',format:'Y-m-d";
		}else if("Double".equals(type)||"Long".equals(type)){
			return "numberfield";
		}else{
			return "textfield";
		}
	}
	private static String jsColumn(String type){
		if("Date".equals(type)){
			return "datecolumn";
		}else if("Double".equals(type)||"Long".equals(type)){
			return "numbercolumn";
		}else{
			return "gridcolumn";
		}
	}
	public static final void doInit() throws Exception {
		template.append(FunctionUtils.readFile("JsTemplate.txt"));
	}
	
	
}
