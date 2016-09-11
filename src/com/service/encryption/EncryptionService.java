package com.service.encryption;

import java.util.Base64;

import com.service.ConfigService;

import net.sf.json.JSONObject;

public class EncryptionService 
{
	//加密
	public static JSONObject Encryption(String messageString) 
	{
		JSONObject encryption = new JSONObject();
		try {
			
			//先随机生成DESkey
			String desKey;
			
			desKey = DESCoder.initKey();
	
			//用种子加密报文
			byte[] data = messageString.getBytes();  
			String securityKey = Base64.getEncoder().encodeToString(DESCoder.encrypt(data, desKey));
			encryption.put("securityData", securityKey);
			
			
			//加密种子
			byte[] key = desKey.getBytes();  
			byte[] encodedData = RSACoder.encryptByPrivateKey(key, ConfigService.RSA_private_key);
			String encryptionMsg = Base64.getEncoder().encodeToString(encodedData);
			
			encryption.put("securityKey", encryptionMsg);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encryption;
	}
	
	//解密
	public static JSONObject decryption(JSONObject EncryptionJson) 
	{
		JSONObject result  = null;
		
		try {
			String securityKey = EncryptionJson.getString("securityKey");
			String securityData = EncryptionJson.getString("securityData");
			
			String desKey ="";
			String Msg ="";
			
			//解密DESkey
			byte[] data =  securityKey.getBytes();
			
			desKey = new String(RSACoder.decryptByPrivateKey(data, ConfigService.RSA_private_key));
			
			//用DESkey解密报文
			byte[] key = securityData.getBytes();
			Msg = new String(DESCoder.decrypt(key, desKey));
			
			//生成json对象
			result =JSONObject.fromObject(Msg);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
