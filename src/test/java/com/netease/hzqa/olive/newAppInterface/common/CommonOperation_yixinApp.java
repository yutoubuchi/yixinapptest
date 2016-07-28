package com.netease.hzqa.olive.newAppInterface.common;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.commontemplate.util.MD5;

import com.netease.hzqa.olive.newAppInterface.utils.Entity;
import com.netease.hzqa.olive.newAppInterface.utils.Http;
import com.netease.hzqa.olive.newAppInterface.utils.HttpRequest;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.AESUtils;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.EnDecryptionUtil;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.RSAUtils;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import net.sf.json.JSONObject;

public class CommonOperation_yixinApp {
    static Logger logger = Logger.getLogger(CommonOperation_yixinApp.class);
    static int deviceKeyCount = 1;
    public static String getClientNonce(){
        String clientOnece = EnDecryptionUtil.createRandomString(16, System.currentTimeMillis());
        return clientOnece;
    }
    
    /**
     * 
      * @Title: getOpenId
      * @Description: TODO
        * @Author: hzzhengyinyan
      * @param userName
      * @param phoneArea
      * @param pwd
      * @param loginToken
      * @return
      * @throws Exception
     */
    public static String getOpenId2_1(String userName, String phoneArea, String pwd, String deviceKey) throws Exception{
        JSONObject httpResponse = login2_1(userName, phoneArea, pwd, deviceKey, false);
        return httpResponse.getJSONObject("result").getString("openId");
   //     return httpResponse.getString("message");
    }
    /**
     * 
     * @Title: login
     * @Description: 用户登录接口
       * @Author: hzzhengyinyan
     * @param entity
     * @return
     * @throws Exception
    */
    public static JSONObject login2_1(String userName, String phoneArea, String pwd, String deviceKey, Boolean isAuto) throws Exception{
        //获取公钥和loginToken
          JSONObject res0 = CommonOperation_yixinApp.getEncryptData("{}");
          String loginToken = res0.getJSONObject("result").getString("loginToken");
          
          //生成客户端增益值
          String clientOnece = EnDecryptionUtil.createRandomString(16, System.currentTimeMillis());        
          //MD5加密
          String mdPass = DigestUtils.md5Hex(pwd);
         
          //用服务器加密值第一次AES加密
          String encryptPassword = AESUtils.encrypt(mdPass,res0.getJSONObject("result").getString("nonce"));     
          //用客户端自己生成的clientOnece第二次AES加密。得到最终密文
          encryptPassword = AESUtils.encrypt(encryptPassword, clientOnece);
          
          //用拼接的公钥RSA加密
          RSAPublicKey rsaPublicKey =RSAUtils.getRSAPublidKey( res0.getJSONObject("result").getString("modulus"),res0.getJSONObject("result").getString("pubKey"));
          //加密反转客户端增益值
          String secKey = RSAUtils.encryptStringForPkcs(rsaPublicKey,StringUtils.reverse(clientOnece));
          
          String entity = "{\"isAuto\":"+isAuto+",\"loginToken\":\""+loginToken+"\",\"userName\":\""+userName+"\",\"userPass\":\""+encryptPassword+
          "\",\"clientNonce\":\""+secKey+"\",\"phoneArea\":\""+phoneArea+"\"}";
          
          // 包头
             List<Parameter> headers = new ArrayList<Parameter>();
             headers.add(new Parameter("Content-Type", "application/json"));
             headers.add(new Parameter("Accept", "application/json"));
             headers.add(new Parameter("version", "2.1"));
             headers.add(new Parameter("deviceKey", deviceKey));
             headers.add(new Parameter("platform", "yixin"));

          // 消息体
             List<Entity> entities = new ArrayList<Entity>();
             entities.add(new Entity(entity));
          // 发起请求
             Http httpRequest = new Http("post", null, headers, entities);
             JSONObject httpResponse;
             if(isAuto){
                 httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, "/yiXinApp/user/login", Config_yixinApp.Port);
             }
             else{
                 httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, "/yiXinApp/user/login",Config_yixinApp.Port,null);
             }
         
             return httpResponse;
         }
   public static JSONObject getEncryptData(String entity) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
       // 消息体
          List<Entity> entities = new ArrayList<Entity>();
          entities.add(new Entity(entity));
       // 发起请求
          Http httpRequest = new Http("post", null, headers, entities);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, "/yiXinApp/user/getEncryptData", Config_yixinApp.Port);
      
          return httpResponse;
      }
   public static JSONObject postRequest(String entity, String openId, String url, String deviceKey) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
          headers.add(new Parameter("userKey", openId));
          headers.add(new Parameter("version", "2.1"));
          headers.add(new Parameter("deviceKey", deviceKey));
          headers.add(new Parameter("platform", "yixin"));

       // 消息体
          List<Entity> entities = new ArrayList<Entity>();
          entities.add(new Entity(entity));
       // 发起请求
          Http httpRequest = new Http("post", null, headers, entities);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port,null);      
          return httpResponse;
      }
   
   public static JSONObject postRequest_notlogin(String entity, String openId, String url, String deviceKey) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
    //      headers.add(new Parameter("userKey", openId));
          headers.add(new Parameter("version", "2.1"));
          headers.add(new Parameter("deviceKey", deviceKey));
          headers.add(new Parameter("platform", "yixin"));

       // 消息体
          List<Entity> entities = new ArrayList<Entity>();
          entities.add(new Entity(entity));
       // 发起请求
          Http httpRequest = new Http("post", null, headers, entities);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port,null);      
          return httpResponse;
      }
   
   public static JSONObject postRequest_yixin(String entity, String openId, String url) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
          headers.add(new Parameter("userKey", openId));

       // 消息体
          List<Entity> entities = new ArrayList<Entity>();
          entities.add(new Entity(entity));
       // 发起请求
          Http httpRequest = new Http("post", null, headers, entities);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port,null);      
          return httpResponse;
      }
   
   public static JSONObject getRequest(List<Parameter> paras, String openId, String url, String deviceKey) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
          headers.add(new Parameter("userKey", openId));
          headers.add(new Parameter("version", "2.1"));
          headers.add(new Parameter("deviceKey", deviceKey));
          headers.add(new Parameter("platform", "yixin"));

       // 发起请求
          Http httpRequest = new Http("get", paras, headers, null);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port, null);
      
          return httpResponse;
      }
   
   public static JSONObject getRequest_notlogin(List<Parameter> paras, String openId, String url, String deviceKey) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "application/json"));
//          headers.add(new Parameter("userKey", openId));
          headers.add(new Parameter("version", "2.1"));
          headers.add(new Parameter("deviceKey", deviceKey));
          headers.add(new Parameter("platform", "yixin"));

       // 发起请求
          Http httpRequest = new Http("get", paras, headers, null);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port, null);
      
          return httpResponse;
      }
   
   public static JSONObject getRequest_free(List<Parameter> paras, String openId, String url, String deviceKey) throws Exception{
       // 包头
          List<Parameter> headers = new ArrayList<Parameter>();
          headers.add(new Parameter("Content-Type", "application/json"));
          headers.add(new Parameter("Accept", "text/html"));
          headers.add(new Parameter("userKey", openId));
          headers.add(new Parameter("deviceKey", deviceKey));
          headers.add(new Parameter("platform", "yixin"));

       // 发起请求
          Http httpRequest = new Http("get", paras, headers, null);
          JSONObject httpResponse = HttpRequest.search(httpRequest, Config_yixinApp.Host, url, Config_yixinApp.Port, null);
      
          return httpResponse;
      }
   //获得设备签名
   public static String getMD5Value(String strObj){
       String resultString = new String(strObj);
       resultString =  MD5.encode(strObj);
       return resultString;
   }
 /**
  * 设备接入服务器接口
   * @Title: accessNew
   * @Description: TODO
    * @Author: hzzhengyinyan
   * @param entity
   * @param key
   * @return
   * @throws Exception
  */
   public static JSONObject accessNew(String entity, String key) throws Exception{
       Date date = new Date();
       String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
       String sign = CommonOperation_yixinApp.getMD5Value(entity+"&timestamp="+timestamp+key);
       List<Parameter> headers = new ArrayList<Parameter>();
       headers.add(new Parameter("Content-Type", "application/x-www-form-urlencoded"));
       headers.add(new Parameter("Accept", "application/json"));
       headers.add(new Parameter("sign", sign)); 
       headers.add(new Parameter("apiVersion", "4.0")); 
       headers.add(new Parameter("timestamp", timestamp)); 
       
       List<Entity> entities = new ArrayList<Entity>();
       entities.add(new Entity(entity));
       Http httpRequest = new Http("post",null, headers, entities);
       JSONObject res = HttpRequest.search(httpRequest,Config_yixinApp.Host,"/init/access",Config_yixinApp.Port, null);
       return res;      
   }
   
   public static long getTimeStemp(String strDate) throws ParseException{
       SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date date=simpleDateFormat.parse(strDate);
       long timeStemp = date.getTime();
       return timeStemp;
   }
}
