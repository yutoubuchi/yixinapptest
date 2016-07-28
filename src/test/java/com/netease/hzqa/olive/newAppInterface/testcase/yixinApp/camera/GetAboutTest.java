package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetAboutTest {
    String openId = BASE64.encode("oliveApp20");
    String url = "/yiXinApp/device/getAbout";
    
    @BeforeClass
    public void doBefore() throws Exception{
            }
    
    @Test(dataProvider = "data_normal")
    public void getAboutTest_normal(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！"); 
    }
    
    @DataProvider
    public Object[][] data_normal(){
        return new Object[][]{
            { "{\"deviceId\":\""+Config_yixinApp.test_deviceId2+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey, 200, "存在的用户+存在的设备号，配套"},
            { "{\"deviceId\":\""+Config_yixinApp.test_deviceId1+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey, 200, "存在的用户+离线的设备号，配套"},
            { "{\"deviceId\":\""+Config_yixinApp.mobile_deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey, 403, "存在的用户+设备号，不配套"},
        };
    }
    
    @Test(dataProvider = "errorData")
    public void getAboutTest_error(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！"); 
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\"918623\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey, 403, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"111111\",\"userToken\":\"86/11900000001\"}", Config_yixinApp.test_deviceKey, 403, "独立APP的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"11900000001\"}", Config_yixinApp.test_deviceKey, 403, "存在的用户（无区号）+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"\"}", Config_yixinApp.test_deviceKey, 403, "不传用户名"},
            {"{\"deviceId\":\"\",\"userToken\":\"86/11900000001\"}", Config_yixinApp.test_deviceKey, 403, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"\"}", Config_yixinApp.test_deviceKey, 403, "不传用户和设备号,参数错误"},
        };
    }
    
    /**
     * 测试环境真实存在设备，执行获取关于设备信息
     */
    @Test(dataProvider = "realData", alwaysRun = true)
    public void do_play(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
   
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");

    }
    /**
     * 真实摄像头测试数据
     */
    @DataProvider
    public Object[][] realData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200, "真实存在设备，进行播放暂停操作"},
        };
    }
}
