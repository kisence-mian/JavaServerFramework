package core.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.util.FileUtils;
import core.util.PackageUtil;

public class GenerateDataTool 
{
	public static void main(String[] args) 
	{
		try {
			
			String[] names = GetDataName();
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				
				name = name.split("\\.")[0];
				
				CreatDataJavaFile(name,DataManager.GetData(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static String[] GetDataName()
	{
		File file = new File("./Resources/Data");
		String[] fileList = file .list();//将该目录下的所有文件放置在一个File类型的数组中
		
		return fileList;
	}
	
	static void CreatDataJavaFile(String dataName,DataTable data)
    {
        String className = dataName + "Generate";
        String content = "";

        content += "package data;\n\n";

        content += GenerateImportCode(data);
        
        content += "import core.data.DataGenerateBase;\n";
        content += "import core.data.DataManager;\n";
        content += "import core.data.DataTable;\n";
        content += "import core.data.SingleData;\n";
        content += "import core.log.LogService;\n\n";

        content += "//" + className + "类\n";
        content += "//该类自动生成请勿修改，以避免不必要的损失";
        content += "\n";

        content += "public class " + className + " extends DataGenerateBase \n";
        content += "{\n";

        content += "\tpublic String m_key;\n";

        //type
        List<String> type = new ArrayList<String>(data.m_tableTypes.keySet());

//        System.out.println("m_tableTypes " + data.m_tableTypes.size());
//        System.out.println("type.size() " + type.size());
        
        if (type.size() > 0)
        {
            for (int i = 1; i < data.TableKeys.size(); i++)
            {
                String key = data.TableKeys.get(i);
                String enumType = null;

                if (data.m_tableEnumTypes.containsKey(key))
                {
                    enumType = data.m_tableEnumTypes.get(key);
                }

                String note = ";";

                if (data.m_noteValue.containsKey(key))
                {
                    note = "; //" + data.m_noteValue.get(key);
                }

                content +="\t";

                if (data.m_tableTypes.containsKey(key))
                {
                    //访问类型 + 字段类型  + 字段名
                    content += "public " + OutPutFieldName(data.m_tableTypes.get(key), enumType) + " m_" + key + note;
                }
                //默认字符类型
                else
                {
                    //访问类型 + 字符串类型 + 字段名 
                    content += "public " + "String" + " m_" + key + note;
                }

                content += "\n";
            }
        }

        content += "\n";

        content += "\tpublic void LoadData(String key) \n";
        content += "\t{\n";
        content += "\t\tDataTable table =  DataManager.GetData(\"" + dataName + "\");\n\n";
        content += "\t\tif (!table.containsKey(key))\n";
        content += "\t\t{\n";
        content += "\t\t\tLogService.Error(\"DataGenerate\",\"" + className + " LoadData Exception Not Fond key ->\" + key + \"<-\");\n";
        content += "\t\t}\n";
        content += "\n";
        content += "\t\tSingleData data = table.get(key);\n\n";

        content += "\t\tm_key = key;\n";

        if (type.size() > 0)
        {
            for (int i = 1; i < data.TableKeys.size(); i++)
            {
                String key = data.TableKeys.get(i);

                content += "\t\t";

                String enumType = null;

                if (data.m_tableEnumTypes.containsKey(key))
                {
                    enumType = data.m_tableEnumTypes.get(key);
                }

                if (data.m_tableTypes.containsKey(key))
                {
                	if(data.m_tableTypes.get(key) != FieldType.Enum)
                	{
                        content += "m_" + key + " = data." + OutPutFieldFunction(data.m_tableTypes.get(key), enumType) + "(\"" + key + "\")";
                	}
                	else
                	{
                        content += "m_" + key + " = (" + enumType + ")data." + OutPutFieldFunction(data.m_tableTypes.get(key), enumType) + "(" + enumType+ ".class,\"" + key + "\")";
					}
                }
                //默认字符类型
                else
                {
                    content += "m_" + key + " = data." + OutPutFieldFunction(FieldType.String, enumType) + "(\"" + key + "\")";
                }

                content += ";\n";
            }
        }

        content += "\t}\n";
        content += "}\n";

        String SavePath = "./src/data/" + className + ".java";

        FileUtils.WriteTextFile(SavePath, content.toString());
    }

    static String OutPutFieldFunction(FieldType fileType,String enumType)
    {
        switch (fileType)
        {
            case Bool: return "GetBool";
            case Color: return "GetColor";
            case Float: return "GetFloat";
            case Int: return "GetInt";
            case String: return "GetString";
            case Vector2: return "GetVector2";
            case Vector3: return "GetVector3";
            case Enum: return "GetEnum";

            case StringArray: return "GetStringArray";
            case IntArray: return "GetIntArray";
            case FloatArray: return "GetFloatArray";
            case BoolArray: return "GetBoolArray";
            case Vector2Array: return "GetVector2Array";
            case Vector3Array: return "GetVector3Array";
            default: return "";
        }
    }

    static String OutPutFieldName(FieldType fileType, String enumType)
    {
        switch (fileType)
        {
            case Bool: return "boolean";
            case Color: return "Color";
            case Float: return "float";
            case Int: return "int";
            case String: return "String";
            case Vector2: return "Vector2";
            case Vector3: return "Vector3";
            case Enum: return enumType;
            
            case StringArray: return "String[]";
            case IntArray: return "int[]";
            case FloatArray: return "float[]";
            case BoolArray: return "boolean[]";
            case Vector2Array: return "Vector2[]";
            case Vector3Array: return "Vector3[]";
            default: return "";
        }
    }
    
    static String GenerateImportCode(DataTable data)
    {
    	String content = "";
    	
    	List<String> type = new ArrayList<String>(data.m_tableTypes.keySet());

	      if (type.size() > 0)
	      {
	          for (int i = 1; i < data.TableKeys.size(); i++)
	          {
	              String key = data.TableKeys.get(i);

	              if (data.m_tableTypes.containsKey(key) 
	            		  && data.m_tableTypes.get(key) == FieldType.Enum)
	              {
	                  //访问类型 + 字段类型  + 字段名
	                  content += "import " + GetPackageName(data.m_tableEnumTypes.get(key))  + ";\n";
	              }
	              
	              if(key.equals("Vector3"))
	              {
	            	  content += "import core.data.Vector3;\n";
	              }
	              
	              if(key.equals("Vector2"))
	              {
	            	  content += "import core.data.Vector2;\n";
	              }
	          }
	      }
	      
	      return content;
    }
    
    static String GetPackageName(String enumName)
    {
    	List<String> classes = PackageUtil.getClassName("");
    	
    	for (int i = 0; i < classes.size(); i++) {
			if(classes.get(i).contains(enumName))
			{
				return classes.get(i).replaceFirst("ct.EclipseWorkSpace.CardGameServer.bin.", "");
			}
		}
    	
    	return "unknow Enum";
    }
}
