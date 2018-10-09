package core.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class PackageUtil 
{
    public static List<String> getClassName(String packageName) {  
        String filePath = ClassLoader.getSystemResource("").getPath() + packageName.replace(".", "\\");  
        try {
			filePath= URLDecoder.decode(filePath,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        LogService.Log("", "filePath " + filePath);
        
        List<String> fileNames = getClassName(filePath, null);  
        return fileNames;  
    }  
  
    private static List<String> getClassName(String filePath, List<String> className) {  
        List<String> myClassName = new ArrayList<String>();  
        File file = new File(filePath);  
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                myClassName.addAll(getClassName(childFile.getPath(), myClassName));  
            } else {  
                String childFilePath = childFile.getPath();  
//                LogService.Log("", "childFilePath " + childFilePath);
//                LogService.Log("", "filePath " + filePath);
                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
                childFilePath = childFilePath.replace("\\", ".");  
                childFilePath= childFilePath.split("bin.")[1];
//                LogService.Log("", "childFilePath " + childFilePath);
                myClassName.add(childFilePath);  
            }  
        }  
  
        return myClassName;  
    } 
}
