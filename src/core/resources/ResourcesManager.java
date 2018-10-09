package core.resources;

import java.util.HashMap;

import core.log.LogService;
import core.util.FileUtils;

public class ResourcesManager {
	
	private static HashMap<String,String> resMap = new HashMap<String,String>();
	public static String LoadTextFileByName(String fileName) {
		String res=null;
		if(resMap.containsKey(fileName))
			res = resMap.get(fileName);
		else
		{
			String path = GetFullPath(fileName);
			 res = FileUtils.LoadTextFile(path);
			resMap.put(fileName, res);
		}
		return res;
	}

	public static String GetFullPath(String fileName) {
		
		InitPath();
		if(!pathMap.containsKey(fileName)) {
			LogService.Error("ResourcesManager", "没有文件名为："+fileName);
			return "";
		}
	  return PathFileDic+ (String)pathMap.get(fileName);
	}
	
	private static boolean isInit =false;
	private final static String PathFileName="PathFile.txt";
	private final static String PathFileDic="./Resources/";

	private static HashMap<String, String> pathMap = new HashMap<String, String>();
	private static void InitPath()
	{
		if(isInit)
			return;
		isInit = true;
		
		String[] arrPath=	FileUtils.LoadLineTextFile(PathFileDic+PathFileName);
	 
		for(int i=0;i<arrPath.length;i++)
		{
			String s = arrPath[i];
			String[] ssArr= s.split(",");
			pathMap.put(ssArr[0], ssArr[1]);		
		}
	}
	
	public static void ClearCache() {
		resMap.clear();
		pathMap.clear();
		isInit =false;
	}
}
