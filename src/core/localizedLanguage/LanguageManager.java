package core.localizedLanguage;

import java.util.ArrayList;
import java.util.Hashtable;

import core.config.ConfigService;
import core.data.DataManager;
import core.data.DataTable;
import core.data.SingleData;
import core.log.LogService;

public class LanguageManager {
	public static final String c_DataFilePrefix = "LangData_";
	public static final String c_mainKey = "key";
	public static final String c_valueKey = "value";
	public static final String c_LanguageSetConfig = "LanguageSetConfig";

	private static Hashtable<SystemLanguage, Hashtable<String, Hashtable<String, String>>> langDatas = new Hashtable<SystemLanguage, Hashtable<String, Hashtable<String, String>>>();
	//当前含有那些语言
	private static ArrayList<SystemLanguage> haveLanguageNames;
	//优先语言
    private static SystemLanguage preferredLanguage;

	private static boolean isInit = false;

	private static void Init() {
		if (isInit) {
			return;
		}

		isInit = true;

		String[] tempLNames = ConfigService.GetStringArray(c_LanguageSetConfig, "Languages", new String[0]);
		haveLanguageNames = new ArrayList<SystemLanguage>();
		for (int i = 0; i < tempLNames.length; i++) {
			String nString = tempLNames[i];
			haveLanguageNames.add(Enum.valueOf(SystemLanguage.class, nString));
		}
		
		String preS = ConfigService.GetString(c_LanguageSetConfig, "PreferredLanguage", "ChineseSimplified");
		preferredLanguage = Enum.valueOf(SystemLanguage.class, preS);
	}

	public static String GetContentByKey(SystemLanguage language, String key) 
	{
		return GetContentByKey(language, key, null);
	}
	public static String GetContentByKey(SystemLanguage language, String key,Object[] contentParams) 
	{
		String[] tempArr = key.split("/");
		String contentID = tempArr[tempArr.length - 1];
		String moduleName = key.replace('/', '_').replaceAll("_" + contentID, "");

		return GetContent(language, moduleName, contentID,contentParams);
	}
	public static String GetContent(SystemLanguage language, String moduleName, String contentID)
	{
		return GetContent(language, moduleName, contentID,null);
	}
	
	public static String GetContent(SystemLanguage language, String moduleName, String contentID,Object[] contentParams)
	{
		Init();
		if (language== SystemLanguage.Chinese) {
			language =SystemLanguage.ChineseSimplified;
		}
		if (!haveLanguageNames.contains(language)) 
		{
			language=preferredLanguage;
		}
		String content = "";

		Hashtable<String, Hashtable<String, String>> moduleDatas = null;

		if (langDatas.containsKey(language)) {
			moduleDatas = langDatas.get(language);
		} else {
			moduleDatas = new Hashtable<String, Hashtable<String, String>>();
			
		}

		Hashtable<String, String> fileData = null;
		if (moduleDatas.containsKey(moduleName)) {

			fileData = moduleDatas.get(moduleName);

		} else {
			String fileName = c_DataFilePrefix + language + "_" + moduleName;
//			LogService.Log("", "多语言  FileName ："+fileName);
			DataTable dTable = DataManager.GetData(fileName);

			if (dTable == null) {
				
				String error= "LanguageManager Error : dont find DataTable. SystemLanguage :" + language + " moduleName :"
						+ moduleName + " contentID :" + contentID;
				error+="\n"+"fileName :"+fileName;
				LogService.Error("", error);
				return "";
			} else {
				fileData = new Hashtable<String, String>();
				for (SingleData d : dTable.values()) {
					String key="";
					String value="";
					
					for (String k : d.keySet())
					{
//						LogService.Log("", "K :"+k);
						if (k.equals(c_mainKey)) {
							key =d.get(k);
						}else {
							value =d.get(k);
						}					
					}
					//LogService.Log("", "Key :"+key+"  Value"+value);
					fileData.put(key.trim(),value.trim());
				}			
			}
			
		}
		if (!moduleDatas.containsKey(moduleName)) {
			moduleDatas.put(moduleName, fileData);
		}
		if (!langDatas.containsKey(language)) {
			langDatas.put(language, moduleDatas);
		}

		if (fileData.containsKey(contentID)) {
			content = fileData.get(contentID);
		} 
		else 
		{

			String error= "LanguageManager Error : dont find DataTable. SystemLanguage :" + language + " moduleName :"
					+ moduleName + " contentID :" + contentID;
			//error+="\n"+"fileName :"+fileName;
			LogService.Error("", error);
			return "";

		}
//		LogService.Log("", "Key : "+contentID+" Content :"+reString);
		 if (contentParams != null && contentParams.length > 0)
	        {
	            for (int i = 0; i < contentParams.length; i++)
	            {
	                String replaceTmp = "{" + i + "}";
	                content = content.replace(replaceTmp, contentParams[i].toString());
	            }
	        }
		return content;

	}

	public static void ClearCache() {

		langDatas.clear();
	}

}
