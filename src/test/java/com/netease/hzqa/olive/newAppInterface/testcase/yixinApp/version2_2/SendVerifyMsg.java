package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SendVerifyMsg {
    String openId;
    String url = "/yiXinApp/user/sendVerifyMsg";
    //私人摄像头
    String deviceId = Config_yixinApp.test_deviceId;
    //公共摄像机，完全开放
    String deviceId1 = Config_yixinApp.test_deviceId1;
    //公共摄像机，需要验证
    String deviceId2 = Config_yixinApp.test_deviceId2;
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        //设为公共摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        //设置观看需验证
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":2}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void SendVerifyMsg_normal(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void SendVerifyMsg_error(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"set new device name\"}", 1220028, "私人摄像机"},
            {"{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"中共政圈\"}", 202, "完全开放的摄像机"},
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"这是我的中共政圈哈哈哈\"}", 200, "需验证的摄像机"},
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"\"}", 200, "需验证的摄像机，消息为空"},
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}", 200, "需验证的摄像机，消息为空，无message"},
        };
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\"\",\"message\":\"set new device name\"}", 200, "userToken为空"},
            {"{\"deviceId\":\""+deviceId2+"\",\"message\":\"set new device name\"}", 200, "userToken不传"},
            {"{\"deviceId\":\"1234567\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"中共政圈\"}", 1220028, "设备不存在"},
            {"{\"deviceId\":\"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"这是我的中共政圈哈哈哈\"}", 1220028, "设备id为空"},
            {"{\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"\"}", 1220028, "不传设备id参数"},
        };
    }

    @Test(dataProvider = "normalData_afterAccept", dependsOnMethods = "SendVerifyMsg_normal")
    public void SendVerifyMsg_afterAccept(String entity, int code, String msg) throws Exception{
        //主人接受观看验证
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", openId, "/yiXinApp/user/ownerAcceptVerify", Config_yixinApp.test_deviceKey);
        //接收后再次发生观看验证
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
        httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_afterAccept()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"这是我的中共政圈哈哈哈\"}", 202, "需验证的摄像机"},
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"\"}", 202, "需验证的摄像机，消息为空"},
            {"{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}", 202, "需验证的摄像机，消息为空，无message"},
        };
    }
    @AfterClass
    public void afterClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        //设为观看权限完全开放
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":0}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
    }
}
