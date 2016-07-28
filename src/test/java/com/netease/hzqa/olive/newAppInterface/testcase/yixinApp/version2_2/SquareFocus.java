package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SquareFocus {
    String openId;
    String indexurl = "/yiXinApp/square/index";
    String url = "/yiXinApp/user/focus";
    String deviceId1;
    String deviceId2;
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"id\":1,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, indexurl, Config_yixinApp.test_deviceKey);
        deviceId1 = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
        deviceId2 = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(1).getString("deviceId");
    }

    @Test(dataProvider = "focus_data_error")
    public void focusTest_error(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] focus_data_error(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头"},   
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":-1}",400,"关注广场页公共摄像头，多次关注"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":3}",200,"关注广场页公共摄像头"},             
            {"{\"deviceId\":\"\",\"focusStatus\":1}",1220028,"关注广场页公共摄像头，多次关注"},
            {"{\"focusStatus\":1}",1220028,"关注广场页公共摄像头,openId不符"},
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":}",400,"关注广场页公共摄像头，,openId不符"},
        };
    }
    
    @Test(dataProvider = "focus_data")
    public void focusTest(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] focus_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头"},   
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头，多次关注"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头"},             
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头，多次关注"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头,openId不符"},
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":1}",200,"关注广场页公共摄像头，,openId不符"},
        };
    }
    
    @Test(dataProvider = "unfocus_data",  dependsOnMethods="focusTest",  enabled = true)
    public void unfocusTest(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] unfocus_data(){
        return new Object[][]{
          {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":0}",200,"取消关注广场页公共摄像头"},  
          {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":0}",400,"取消关注广场页公共摄像头,多次取消"},  
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",200,"取消关注广场页公共摄像头"},    
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",400,"取消关注广场页公共摄像头，多次取消"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",400,"取消关注广场页公共摄像头，,openId不符"},
            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":0}",400,"取消关注广场页公共摄像头，,openId不符"},
        };
    }
    
    @Test(dataProvider = "focus_notlogin",  dependsOnMethods="unfocusTest")
    public void focusTest_notnogin(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] focus_notlogin(){
        return new Object[][]{
   
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",1220030,"取消关注广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",1220030,"取消关注广场页公共摄像头，,openId不符"},
//            {"{\"deviceId\":\""+deviceId1+"\",\"focusStatus\":-1}",400,"取消关注广场页公共摄像头，,openId不符"},
        };
    }
    
    @Test(dataProvider = "focus_logout",  dependsOnMethods="unfocusTest")
    public void focusTest_logout(String entity, int code, String msg) throws Exception{
        
        //先执行登出操作
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"userName\":\"13500000001\"}",openId,"/yiXinApp/user/loginOut", Config_yixinApp.test_deviceKey);
        //在执行关注和取消关注操作
        res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        //返回1220012非法操作
        Assert.assertEquals(res.getInt("code"), 1220012, msg);
      //在执行关注和取消关注操作
        res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, url, Config_yixinApp.test_deviceKey);
        //返回1220030未登录，无法操作
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] focus_logout(){
        return new Object[][]{
            
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":1}",1220030,"取消关注广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId2+"\",\"focusStatus\":0}",1220030,"取消关注广场页公共摄像头"},
     };
    }
}
