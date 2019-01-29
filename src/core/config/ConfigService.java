package core.config;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import core.log.LogService;

//当文本为UTF-8有头时会报错，请去头保存
public class ConfigService 
{
	public final static String configPath = "./Resources/Config/"; 
	
	static HashMap<String, Properties> s_configs = new HashMap<>();
	
	public static Properties GetProperties(String configName)
	{
		if(!s_configs.containsKey(configName))
		{
			Properties propertys = new Properties();  

			FileInputStream fis;
			try {
				fis = new FileInputStream( ConfigService.configPath + configName + ".txt" );
				propertys.load(fis);
				
				fis.close();
				
			} catch (Exception e) {
				LogService.Warn("", "找不到配置文件 " + configName);
			}

			s_configs.put(configName, propertys);
		}
		
		return s_configs.get(configName);
	}
	
	public static String GetString(String configName , String key,String defaultValue)
	{
		Properties properties =GetProperties(configName);
		
		if(properties != null)
		{
			return properties.getProperty(key);
		}
		else 
		{
			return defaultValue;
		}
	}
	
	public static int GetInt(String configName , String key,int defaultValue) 
	{
		Properties properties =GetProperties(configName);
		
		if(properties != null)
		{
			String value =null;
			try
			{
				 value = properties.getProperty(key);
				return Integer.parseInt( value);
			}
			catch (Exception e) 
			{
				String keys="";
				
				for (String item : properties.stringPropertyNames()) 
				{
					keys+=item+" ";
				}
				
				LogService.Error("","configName :"+configName+" key :"+key+ " value :"+value+" "+keys+"\nException :"+e);
				return defaultValue;
			}
		}
		else 
		{
			return defaultValue;
		}
	}
	
	public static float GetFloat(String configName , String key,Float defaultValue) throws Exception
	{
		Properties properties =GetProperties(configName);
		
		if(properties != null)
		{	
			try
			{
				return Float.parseFloat( properties.getProperty(key));
			}
			catch (Exception e) 
			{
				LogService.Error("","configName :"+configName+" key :"+key+ "\nException :"+e);
				return defaultValue;
			}
		}
		else 
		{
			return defaultValue;
		}
	}
	
	public static boolean GetBool(String configName , String key,boolean defaultValue)
	{
		Properties properties =GetProperties(configName);
		if(properties != null)
		{			
			try
			{
				return Boolean.parseBoolean(properties.getProperty(key));
			}
			catch (Exception e) 
			{
				LogService.Error("","configName :"+configName+" key :"+key+ "\nException :"+e);
				return defaultValue;
			}
		}
		else 
		{
			return defaultValue;
		}
	}
	public static String[] GetStringArray(String configName , String key,String[] defaultValue)
	{
		Properties properties =GetProperties(configName);
		if(properties != null)
		{			
			try
			{
				String content =properties.getProperty(key);
				if (content.contains("|")) {
					return content.split("\\|");
				}
				else {
					return new String[] {content};
				}
			}
			catch (Exception e) 
			{
				LogService.Error("","configName :"+configName+" key :"+key+ "\nException :"+e);
				return defaultValue;
			}
		}
		else 
		{
			return defaultValue;
		}
	}
}
