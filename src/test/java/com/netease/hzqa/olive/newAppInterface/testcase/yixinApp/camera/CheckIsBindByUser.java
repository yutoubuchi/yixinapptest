package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class CheckIsBindByUser {
    
    String openId;
    String url = "/yiXinApp/device/checkIsBindByUser";
    String deviceId = Config_yixinApp.deviceId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = false)
    public void checkIsBindByUser_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
        if(status == 1)
            Assert.assertEquals(httpResponse.getBoolean("result"), true, msg);
        else
            Assert.assertEquals(httpResponse.getBoolean("result"), false, msg);
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void checkIsBindByUser_error(String entity, String deviceKey, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
//        Assert.assertEquals(httpResponse.getBoolean("result"), false, "检查是否已添加失败！");
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {          
            {1,"{\"deviceId\":\""+Config_yixinApp.test_deviceId1+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}",Config_yixinApp.test_deviceKey, 200, "存在的用户+绑定的设备号"},
            {2,"{\"deviceId\":\""+Config_yixinApp.mobile_deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}",Config_yixinApp.test_deviceKey, 200, "存在的用户+存在的设备号"},
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
            {"{\"deviceId\":\"1111000\",\"userToken\":\"86/"+Config_yixinApp.test_user+"\"}",Config_yixinApp.test_deviceKey, 200, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"1111\",\"userToken\":\"86/15000000001\"}",Config_yixinApp.test_deviceKey, 200, "不存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"1111111\",\"userToken\":\""+Config_yixinApp.test_user+"\"}",Config_yixinApp.test_deviceKey, 200, "存在的用户（无区号）+存在的设备号"},
            {"{\"deviceId\":\"1111111\",\"userToken\":\"\"}",Config_yixinApp.test_deviceKey, 200, "不传用户名"},
            {"{\"deviceId\":\"\",\"userToken\":86/\""+Config_yixinApp.test_user+"\"}",Config_yixinApp.test_deviceKey, 400, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"1\"}",Config_yixinApp.test_deviceKey, 200, "不传用户（无区号）+设备号"},
        };
    }
}
