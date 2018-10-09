package core.data;

import java.util.HashMap;

import core.log.LogService;
import core.resources.ResourcesManager;

public class DataManager
{
	static String s_modelName = "";
	static String path = "./Resources/Data/";
	
	static HashMap<String, DataTable> s_datas = new HashMap<>();
	
	public static DataTable GetData(String dataName)
	{
		try
		{
			if(s_datas.containsKey(dataName))
			{
				return s_datas.get(dataName);
			}
			
			return ReadData(dataName);
		}catch (Exception e)
		{
			LogService.Exception("DataManager", "GetData", e);
			return null;
		}
	}
	
	static DataTable ReadData(String dataName) throws Exception
	{
		String conetnt = ReadText(dataName);
		
		DataTable dataTable = DataTable.Analysis(conetnt);
		
		s_datas.put(dataName, dataTable);
		
		return dataTable;
	}
	
	static String ReadText(String dataName)
	{
		return ResourcesManager.LoadTextFileByName(dataName);
	}
}
