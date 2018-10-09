package core.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.binary.Base64;

public class HttpUtil 
{
	  public static String GetAPIResult(String url,String auth,String param) 
	  {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            URLConnection conn = realUrl.openConnection();
	            //conn.setConnectTimeout(5000);

	            String base64Credentials = new String(Base64.encodeBase64(auth.getBytes()));
	            conn.setRequestProperty("Authorization", "Basic " + base64Credentials);
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("Content-type", "application/json");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            out = new PrintWriter(conn.getOutputStream());
	            out.print(param);
	            out.flush();
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                out.close();
	                in.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }
}
