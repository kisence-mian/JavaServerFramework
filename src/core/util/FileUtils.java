package core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import core.log.LogService;


public class FileUtils {
	//创建文件夹
	  public static boolean CreateDir(String destDirName) {  
	        File dir = new File(destDirName);  
	        if (dir.exists()) {  
	            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");  
	            return false;  
	        }  
	        if (!destDirName.endsWith(File.separator)) {  
	            destDirName = destDirName + File.separator;  
	        }  
	        //创建目录  
	        if (dir.mkdirs()) {  
	            System.out.println("创建目录" + destDirName + "成功！");  
	            return true;  
	        } else {  
	            System.out.println("创建目录" + destDirName + "失败！");  
	            return false;  
	        }  
	    } 
	//读取文本文件
	public static String LoadTextFile(String path) {

		 StringBuilder result = new StringBuilder();
	        try{
	        	File file=new File(path);
	        	InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");
	            BufferedReader br = new BufferedReader(read);//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(s+"\r\n");
	            }
	            br.close();    
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	}
	//读取文本文件
	public static String[] LoadLineTextFile(String path) {
		  /* 读入TXT文件 */  
       // String pathname = "D:\\twitter\\13_9_6\\dataset\\en\\input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
        File file = new File(path); // 要读取以上路径的input。txt文件 
        ArrayList<String> list = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	list.add(s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] arr=new String[list.size()];
        return  list.toArray(arr);
	}
	
	
	public static void WriteTextFile(String path,String data) {
		
		 /* 写入Txt文件 */  
        File file = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件  

		try {
			if(file.exists()) {
				file.delete();
			}else {
				CreateDir(file.getParentFile().getPath());
			}
			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(data);
			 out.flush(); // 把缓存区内容压入文件  
		        out.close(); // 最后记得关闭文件 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}
	
    /** 
     * B方法追加文件：使用FileWriter 
     */  
    public static void AppendTextFile(String path, String content) {  
        try {  
        	 File file = new File(path);
        	if(!file.exists()) {
        		file.createNewFile();
			}
			
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件  
            FileWriter writer = new FileWriter(path, true);  
            writer.write(content);  
            writer.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  

}
