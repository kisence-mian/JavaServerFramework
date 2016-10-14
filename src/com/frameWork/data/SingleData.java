package com.frameWork.data;

import java.util.HashMap;

public class SingleData extends HashMap<String, String>
{
	public DataTable data;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int GetInt(String key) throws Exception
    {
		
        if (this.containsKey(key))
        {
            return Integer.parseInt(this.get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Integer.parseInt(data.defaultValue.get(key));
        }

        throw new Exception("Don't Exist Value or DefaultValue by " + key); // throw  
    }

    public float GetFloat(String key) throws Exception
    {
        if (this.containsKey(key))
        {
            return Float.parseFloat(get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Float.parseFloat(data.defaultValue.get(key));
        }

        throw new Exception("Don't Exist Value or DefaultValue by " + key); // throw  
    }

    public Boolean GetBool(String key) throws Exception
    {
        if (this.containsKey(key))
        {
            return Boolean.parseBoolean(get(key));
        }

        if (data.defaultValue.containsKey(key))
        {
            return Boolean.parseBoolean(data.defaultValue.get(key));
        }

        throw new Exception("Don't Exist Value or DefaultValue by " + key); // throw  
    }

    public String GetString(String key) throws Exception
    {
        if (this.containsKey(key))
        {
            return get(key);
        }

        if (data.defaultValue.containsKey(key))
        {
            return data.defaultValue.get(key);
        }

        throw new Exception("Don't Exist Value or DefaultValue by " + key); // throw  
    }
}
