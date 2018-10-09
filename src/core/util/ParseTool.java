package core.util;

import core.data.Vector2;
import core.data.Vector3;
import core.log.LogService;

public class ParseTool {
	
    static String[] c_NullStringArray = new String[0];
    
    public static Boolean NullStringfFilter(String value)
    {
        if (value != null
                && !value.equals( "")
                && !value.equals( "null")
                && !value.equals( "Null")
                && !value.equals( "NULL")
                && !value.equals( "None")
                )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    public static String[] String2StringArray(String value)
    {
        if (NullStringfFilter(value))
        {
            return value.split("\\|");
        }
        else
        {
            return c_NullStringArray;
        }
    }
    
    public static int[] String2IntArray(String value)
    {
        String[] strArray = String2StringArray(value);
        int[] array = new int[strArray.length];

        for (int i = 0; i < strArray.length; i++)
        {
        	int tmp = Integer.parseInt(strArray[i]);

            array[i] = tmp;
        }

        return array;
    }
    
    public static float[] String2FloatArray(String value)
    {
        String[] strArray = String2StringArray(value);
        float[] array = new float[strArray.length];

        for (int i = 0; i < strArray.length; i++)
        {
            float tmp = Float.parseFloat(strArray[i]);

            array[i] = tmp;
        }

        return array;
    }
    
    public static boolean[] String2BoolArray(String value)
    {
        String[] strArray = String2StringArray(value);
        boolean[] array = new boolean[strArray.length];

        for (int i = 0; i < strArray.length; i++)
        {
        	boolean tmp = Boolean.parseBoolean(strArray[i]);
            array[i] = tmp;
        }

        return array;
    }
    
    public static Vector2[] String2Vector2Array(String value)
    {
        String[] strArray = String2StringArray(value);
        Vector2[] array = new Vector2[strArray.length];

        for (int i = 0; i < strArray.length; i++)
        {
        	Vector2 tmp = String2Vector2(strArray[i]);
            array[i] = tmp;
        }

        return array;
    }
    
    public static Vector3[] String2Vector3Array(String value)
    {
        String[] strArray = String2StringArray(value);
        Vector3[] array = new Vector3[strArray.length];

        for (int i = 0; i < strArray.length; i++)
        {
        	Vector3 tmp = String2Vector3(strArray[i]);
            array[i] = tmp;
        }

        return array;
    }
    
    
    public static Vector2 String2Vector2(String value)
    {
        try
        {
            String[] values = value.split(",");
            float x = Float.parseFloat(values[0]);
            float y = Float.parseFloat(values[1]);

            return new Vector2(x, y);
        }
        catch (Exception e)
        {
        	LogService.Error("ParseTool", "ParseVector2: Don't convert value to Vector2 value:" + value + "\n" + e.toString());
        	return new Vector2();
        }
    }
    
    public static Vector3 String2Vector3(String value)
    {
        try
        {
            String[] values = value.split(",");
            float x = Float.parseFloat(values[0]);
            float y = Float.parseFloat(values[1]);
            float z = Float.parseFloat(values[2]);

            return new Vector3(x, y, z);
        }
        catch (Exception e)
        {
        	LogService.Error("ParseTool", "ParseVector3: Don't convert value to Vector3 value:" + value + "\n" + e.toString());
        	return new Vector3();
        }
    }
}
