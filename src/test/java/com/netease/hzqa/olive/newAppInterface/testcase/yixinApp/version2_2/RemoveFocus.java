package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class RemoveFocus {
    String openId;
    String url = "/yiXinApp/user/removeFocus";
    //私人摄像头
    String deviceId = Config_yixinApp.test_deviceId;
    String deviceId1 = Config_yixinApp.test_deviceId1;
    String deviceId2 = Config_yixinApp.test_deviceId2;

    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        //设为公共摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        //第一个关注者
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceKey);   
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        //第二个关注者
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.email_user, Config_yixinApp.mobile_area, Config_yixinApp.email_pwd, Config_yixinApp.test_deviceKey);   
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.email_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.email_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.email_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        //主人登录
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);       
    }
    
    @Test(dataProvider = "normal_data")
    public void focusTest(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}]}",200,"删除单个关注者"},    
            {"{\"deviceId\":\""+deviceId+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}]}",200,"删除单个关注者，再删一次"},  
            {"{\"deviceId\":\""+deviceId+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"},{\"userToken\":\""+Config_yixinApp.email_userId+"\"}]}",200,"删除两个关注者，包含一个已删除"},    
            {"{\"deviceId\":\""+deviceId1+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"},{\"userToken\":\""+Config_yixinApp.email_userId+"\"}]}",200,"删除2个关注者"},
            {"{\"deviceId\":\""+deviceId1+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"},{\"userToken\":\""+Config_yixinApp.email_userId+"\"}]}",200,"删除2个关注者，再删一次"},
            {"{\"deviceId\":\""+deviceId2+"\",\"list\":[]}",200,"删除关注者，列表为空"},
               
        };
    }
    
    @Test(dataProvider = "error_data")
    public void focusTest_error(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] error_data(){
        return new Object[][]{
            {"{\"deviceId\":\"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}]}",1220028,"deviceId为空"},    
            {"{\"deviceId\":\"123456\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}]}",1220028,"deviceId不存在"},  
            {"{\"deviceId\":\""+deviceId2+"\"}",400,"删除关注者，列表不传"},              
        };
    }
    
    @AfterClass
    public void afterClass() throws Exception
    {
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
         //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
    }
}
