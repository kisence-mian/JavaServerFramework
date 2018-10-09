package core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils
{
	static  Gson gson = new Gson();
	
	static  Gson gson_field = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	public static String bean2Json(Object obj){
        return gson.toJson(obj);
    }
    
    public static <T> T json2Bean(String jsonStr,Class<T> objClass){
        return  gson.fromJson(jsonStr, objClass);
    }
    //当Json被当做字符串保存时使用的转换
    public static String bean2Json2SaveStringData(Object obj){
        return gson.toJson(obj).replace("\"", "\\\"");
    }
    
    public static <T> T json2BeanBySaveStringData(String jsonStr,Class<T> objClass){
    	String s = jsonStr.replace( "\\\"","\"");
        return  gson.fromJson(s, objClass);
    }
	public static String bean2JsonF(Object obj){ 
        return gson_field.toJson(obj);
    }
    
    public static <T> T json2BeanF(String jsonStr,Class<T> objClass){
        return  gson_field.fromJson(jsonStr, objClass);
    }
}
