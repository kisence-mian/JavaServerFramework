package com.frameWork.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.frameWork.service.LogService;

public class DataTable extends HashMap<String,SingleData >
{
	static String s_modelName = "DataTable";
	
	// 单条记录所拥有的字段名
	public List<String> TableKeys;
	
	public List<String> TableIDs;

	public HashMap<String, String> defaultValue;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public static DataTable Analysis(String stringData) throws Exception
	    {
	        try
	        {
	            int lineIndex = 0;
	            DataTable data = new DataTable();
	            String[] line = stringData.split("\r\n");

	            //第一行作为Key
	            data.TableKeys = new ArrayList<>();
	            String[] rowKeys = ConvertStringArray(line[lineIndex]);
	            for (int i = 0; i < rowKeys.length; i++)
	            {
	                if (!rowKeys[i].equals(""))
	                {
	                    data.TableKeys.add(rowKeys[i]);
	                }
	            }

	            String[] LineData;
	            for (lineIndex = 1;;lineIndex++)
	            {
	                LineData = ConvertStringArray(line[lineIndex]);

	                //注释忽略
	                if (LineData[0].equals("note"))
	                {
	                    //nothing
	                }
	                //默认值
	                else if (LineData[0].equals("default"))
	                {
	                    AnalysisDefaultValue(data, LineData);
	                }
	                //数据正文
	                else
	                {
	                    break;
	                }
	            }    
	            
//	            lineIndex = 3;

	            data.TableIDs = new ArrayList<String>();

	            //开始解析数据
	            for (int i = lineIndex; i < line.length; i++)
	            {
	                SingleData dataTmp = new SingleData();
	                dataTmp.data = data;
	                String[] row = ConvertStringArray(line[i]);

	                for (int j = 0; j < row.length; j++)
	                {
	                    if (!row[j].equals(""))
	                    {
	                        dataTmp.put(data.TableKeys.get(j), row[j]);
	                    }
	                }

	                //第一个数据作为这一个记录的Key
	                data.put(row[0], dataTmp);
	                data.TableIDs.add(row[0]);
	            }

	            return data;
	        }
	        catch (Exception e)
	        {
	        	LogService.Exception(s_modelName, e.toString(), e);
	        	
	            throw new Exception("Analysis: Don't convert value to DataTable:" + "\n" + e.toString()); // throw  
	        }
	    }
	 
	 
	 public static String[] ConvertStringArray(String lineContent)
	    {
	        List<String> result = new ArrayList<String>();
	        int startIndex = 0;
	        boolean state = true; //逗号状态和引号状态

	        for (int i = 0; i < lineContent.length(); i++)
	        {
	            if (state)
	            {
	                if (lineContent.charAt(i) == '\t')
	                {
	                    result.add(lineContent.substring(startIndex, i ));
	                    startIndex = i + 1;
	                }
	                else if (lineContent.charAt(i) == '\"')
	                {
	                    //转为引号状态
	                    state = false;
	                }
	            }
	            else
	            {
	                if (lineContent.charAt(i) == '\"')
	                {
	                    //转为逗号状态
	                    state = true;
	                }
	            }
	        }
	        
	        
	        String[] reString = new String[result.size()];
	        for (int i = 0; i < result.size(); i++) 
	        {
	        	reString[i] = result.get(i);
			}
	        return reString;
	    }
	 
	    public static void AnalysisDefaultValue(DataTable l_data,String[] l_lineData)
	    {
	        l_data.defaultValue = new HashMap<String, String>();

	        for (int i = 0; i < l_lineData.length; i++)
	        {
	            if (!l_lineData[i].equals(""))
	            {
	                l_data.defaultValue.put(l_data.TableKeys.get(i), l_lineData[i]);
	            }
	        }
	    }
}
