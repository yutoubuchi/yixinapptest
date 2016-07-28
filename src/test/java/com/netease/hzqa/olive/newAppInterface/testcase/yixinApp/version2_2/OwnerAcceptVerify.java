package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class OwnerAcceptVerify {
    String openId;
    String url = "/yiXinApp/user/ownerAcceptVerify";
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
        //设为公共摄像头，部分公开且观看需验证
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"recoverFans\":false}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":2}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        //设置全部公开，观看需验证
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"recoverFans\":false}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":3}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"哈哈\"}", openId, "/yiXinApp/user/sendVerifyMsg", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), 200, "发送观看验证失败1"); 
        httpResponse = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"message\":\"哈哈\"}", openId, "/yiXinApp/user/sendVerifyMsg", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), 200, "发送观看验证失败2"); openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);        
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void OwnerAcceptVerify_normal(int status, String entity, int code, String msg) throws Exception{
        if(status == 5){
            //设为私人摄像机
            CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
          //设为公共摄像机
            CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);            
        }
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void OwnerAcceptVerify_error(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
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
            {1,"{\"deviceId\":\""+deviceId+"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 1220028, "私人摄像机"},
            {2,"{\"deviceId\":\""+deviceId1+"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 200, "接受发送验证请求的用户"},
            {3,"{\"deviceId\":\""+deviceId1+"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 400, "已接受，再次接受"},
            {4,"{\"deviceId\":\""+deviceId2+"\",\"inviteUid\":\""+Config_yixinApp.email_userId+"\"}", 400, "接受未发验证消息的用户"},
            {5,"{\"deviceId\":\""+deviceId2+"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 400, "设为私有，再设为公有，接受之前的验证消息"},
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
            {"{\"deviceId\":\""+deviceId2+"\",\"inviteUid\":\"\"}", 1220001, "userToken为空"},
            {"{\"deviceId\":\""+deviceId2+"\"}", 1220001, "inviteUid不传"},
            {"{\"deviceId\":\"1234567\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 400, "设备不存在"},
            {"{\"deviceId\":\"\",\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 1220001, "设备id为空"},
            {"{\"inviteUid\":\""+Config_yixinApp.mobile_userId+"\"}", 1220001, "不传设备id参数"},
        };
    }

    @AfterClass
    public void afterClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        //设为观看权限完全开放
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":0}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":0}", openId, "/yiXinApp/camera/setPublicType", Config_yixinApp.test_deviceKey);
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
    }
}
