package core.data;

import java.util.HashMap;

import core.log.LogService;
import core.util.ParseTool;

public class SingleData extends HashMap<String, String>
{
	public DataTable data;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int GetInt(String key)
    {
        if (this.containsKey(key))
        {
            return Integer.parseInt(this.get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Integer.parseInt(data.defaultValue.get(key));
        }

        LogService.Error("SingleData","GetInt Don't Exist Value or DefaultValue by " + key); // throw 
        return 0;
    }
	
    public int[] GetIntArray(String key)
    {
        String content = null;

        if (this.containsKey(key))
        {
            content = this.get(key);
            return ParseTool.String2IntArray(content);
        }

        if (data.defaultValue.containsKey(key))
        {
            content = data.defaultValue.get(key);
            return ParseTool.String2IntArray(content);
        }
        
        LogService.Error("SingleData","GetIntArray Don't Exist Value or DefaultValue by " + key); // throw 
        return new int[0];
    }
	

    public float GetFloat(String key)
    {
        if (this.containsKey(key))
        {
            return Float.parseFloat(get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Float.parseFloat(data.defaultValue.get(key));
        }

        LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
        return 0;
    }
    
    public float[] GetFloatArray(String key)
    {
        String content = null;

        if (this.containsKey(key))
        {
            content = this.get(key);
            return ParseTool.String2FloatArray(content);
        }

        if (data.defaultValue.containsKey(key))
        {
            content = data.defaultValue.get(key);
            return ParseTool.String2FloatArray(content);
        }
        
        LogService.Error("SingleData","GetIntArray Don't Exist Value or DefaultValue by " + key); // throw 
        return new float[0];
    }

    public Boolean GetBool(String key)
    {
        if (this.containsKey(key))
        {
            return Boolean.parseBoolean(get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Boolean.parseBoolean(data.defaultValue.get(key));
        }

        LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
        return false;
    }
    
    public boolean[] GetBoolArray(String key)
    {
        String content = null;

        if (this.containsKey(key))
        {
            content = this.get(key);
            return ParseTool.String2BoolArray(content);
        }

        if (data.defaultValue.containsKey(key))
        {
            content = data.defaultValue.get(key);
            return ParseTool.String2BoolArray(content);
        }
        
        LogService.Error("SingleData","GetIntArray Don't Exist Value or DefaultValue by " + key); // throw 
        return new boolean[0];
    }

    public String GetString(String key) 
    {
        if (this.containsKey(key))
        {
        	String content = get(key);
        	
        	if(ParseTool.NullStringfFilter(content))
        	{
        		return content;
        	}
        	else
        	{
				return null;
			}
        }

        if (data.defaultValue.containsKey(key))
        {
        	String content = data.defaultValue.get(key);
        	
        	if(ParseTool.NullStringfFilter(content))
        	{
        		return content;
        	}
        	else
        	{
				return null;
			}
        }

        LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
        return null;
    }

	public String[] GetStringArray(String key)
	{
		 if (this.containsKey(key))
         {
             return ParseTool.String2StringArray(this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return ParseTool.String2StringArray(data.defaultValue.get(key));
         }
         
         LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
         return null;
	}
	
	public <T extends Enum<T>> T GetEnum(Class<T> c,String key)
	{
		 if (this.containsKey(key))
         {
             return (T)Enum.valueOf(c, this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return (T)Enum.valueOf(c, data.defaultValue.get(key));
         }
		
		
		return (T)Enum.valueOf(c, "0");
	}
	
	public Vector2 GetVector2(String key)
	{
		 if (this.containsKey(key))
         {
             return ParseTool.String2Vector2(this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return ParseTool.String2Vector2(data.defaultValue.get(key));
         }
         
         LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
         return null;
	}
	
	public Vector2[] GetVector2Array(String key)
	{
		 if (this.containsKey(key))
         {
             return ParseTool.String2Vector2Array(this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return ParseTool.String2Vector2Array(data.defaultValue.get(key));
         }
         
         LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
         return null;
	}
	
	public Vector3 GetVector3(String key)
	{
		 if (this.containsKey(key))
         {
             return ParseTool.String2Vector3(this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return ParseTool.String2Vector3(data.defaultValue.get(key));
         }
         
         LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
         return null;
	}
	
	public Vector3[] GetVector3Array(String key)
	{
		 if (this.containsKey(key))
         {
             return ParseTool.String2Vector3Array(this.get(key));
         }

         if (data.defaultValue.containsKey(key))
         {
             return ParseTool.String2Vector3Array(data.defaultValue.get(key));
         }
         
         LogService.Error("SingleData","Don't Exist Value or DefaultValue by " + key); // throw  
         return null;
	}
}
