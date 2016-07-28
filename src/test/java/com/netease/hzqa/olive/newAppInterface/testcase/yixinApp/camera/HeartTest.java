package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class HeartTest {
    String openId = BASE64.encode("oliveApp20");
    String url = "/yiXinApp/private/heart";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
            }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void heartTest_normal(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查心跳失败！"); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void heartTest_error(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查心跳失败！"); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {  "{\"deviceId\":\"163021505003970\",\"userToken\":\"4916013\"}", Config_yixinApp.test_deviceKey, 405, "存在的用户+存在的设备号，配套"},
            {  "{\"deviceId\":\"" + Config_yixinApp.test_deviceId1+"\",\"userToken\":\"4916013\"}", Config_yixinApp.test_deviceKey, 200, "存在的用户+存在的设备号，配套"},
            {  "{\"deviceId\":\"" + Config_yixinApp.test_deviceId1+"\",\"userToken\":\"4921001\"}", Config_yixinApp.test_deviceKey, 200, "存在的用户+存在的设备号，但是不配套"},
            {  "{\"deviceId\":\"163021505003970\",\"userToken\":\"\"}", Config_yixinApp.test_deviceKey, 405, "存在的用户+存在的设备号，但是不配套"},
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
            {"{\"deviceId\":\"918623\",\"userToken\":\"4916013\",\"os\":\"android\",\"playType\":1}", Config_yixinApp.test_deviceKey, 404, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"86/15906610010\"}",Config_yixinApp.test_deviceKey, 404, "不存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"\"}",Config_yixinApp.test_deviceKey, 404, "不传用户名"},
            {"{\"deviceId\":\"\",\"userToken\":\"86/15906610010\"}",Config_yixinApp.test_deviceKey, 404, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"1\"}", Config_yixinApp.test_deviceKey,404, "不传用户（无区号）+设备号"},
        };
    }
    
    /**
     * 测试环境真实存在设备，获取设备心跳
     */
    @Test(dataProvider = "heartData", alwaysRun = true)
    public void do_heart(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);

        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
    }
    /**
     * 真实摄像头测试数据
     */
    @DataProvider
    public Object[][] heartData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200, "真实存在设备，进行获取设备心跳操作"},
        };
    }
}
