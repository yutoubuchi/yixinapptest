package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetFocusList {
    String openId;
    String url = "/yiXinApp/user/focusList";
    //私人摄像头
    String deviceId = Config_yixinApp.test_deviceId;
    String deviceId1 = Config_yixinApp.test_deviceId1;
    String deviceId2 = Config_yixinApp.test_deviceId2;

    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        //设为公共摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
        //第一个关注者
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceKey);   
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        //第二个关注者
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.email_user, Config_yixinApp.mobile_area, Config_yixinApp.email_pwd, Config_yixinApp.test_deviceKey);   
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"userToken\":\""+Config_yixinApp.email_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"userToken\":\""+Config_yixinApp.email_userId+"\",\"focusStatus\":1}", openId, "/yiXinApp/user/focus", Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "关注失败");
        //主人登录
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);       
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void GetFocusList_normal(int status, String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
        if(status == 3)
            Assert.assertEquals(httpResponse.getBoolean("hasMore"), true, msg);
        else if(status == 2 || status == 4 || status == 5)
            Assert.assertEquals(httpResponse.getBoolean("hasMore"), false, msg);
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void GetFocusList_error(String entity, int code, String msg) throws Exception{
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
            {1,"{\"deviceId\":\""+deviceId+"\",\"offset\":0,\"limit\":5}", 407, "私人摄像机"},
            {2,"{\"deviceId\":\""+deviceId1+"\",\"offset\":0,\"limit\":5}", 200, "完全开放的摄像机"},
            {3,"{\"deviceId\":\""+deviceId2+"\",\"offset\":0,\"limit\":1}", 200, "第一页"},
            {4,"{\"deviceId\":\""+deviceId2+"\",\"offset\":1,\"limit\":1}", 200, "第二页"},
            {5,"{\"deviceId\":\""+deviceId2+"\",\"offset\":0,\"limit\":2}", 200, "第一页"},
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
            {"{\"deviceId\":\"\",\"offset\":0,\"limit\":5}", 407, "摄像机id为空"},
            {"{\"deviceId\":\"123456\",\"offset\":0,\"limit\":5}", 407, "不存在摄像机"},
            {"{\"deviceId\":\""+deviceId2+"\",\"offset\":0}", 200, "limit丢失"},
            {"{\"deviceId\":\""+deviceId2+"\",\"limit\":5}", 200, "offset丢失"},
            {"{\"offset\":0,\"limit\":5}", 407, "deviceId丢失"},
        };
    }
    
    @AfterClass
    public void afterClass() throws Exception
    {
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId1+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"},{\"userToken\":\""+Config_yixinApp.email_userId+"\"}]}", openId, "/yiXinApp/user/removeFocus", Config_yixinApp.test_deviceKey);
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId2+"\",\"list\":[{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"},{\"userToken\":\""+Config_yixinApp.email_userId+"\"}]}", openId, "/yiXinApp/user/removeFocus", Config_yixinApp.test_deviceKey);
    }
}
