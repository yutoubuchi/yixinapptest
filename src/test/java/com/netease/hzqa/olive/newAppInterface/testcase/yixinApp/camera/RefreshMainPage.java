package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class RefreshMainPage {
    String openId = BASE64.encode("Q609");
    String url = "/yiXinApp/device/refresh";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
           }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void refreshMainPage_normal(String entity, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", enabled = true)
    public void refreshMainPage_error(String entity, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
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
            {  "{\"deviceId\":\""+Config_yixinApp.test_deviceId1+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", 200, "存在的用户+存在的设备号"},
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
            {  "{\"deviceId\":\"918623\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", 404, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"10010\"}", 404, "不存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"13500000001\"}", 404, "存在的用户（无区号）+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"\"}", 404, "不传用户名"},
            {"{\"deviceId\":\"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", 404, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"\"}", 404, "不传用户（无区号）+设备号"},
        };
    }
    
    /**
     * 测试环境真实存在设备，刷新摄像机主页
     */
    @Test(dataProvider = "playData", alwaysRun = true)
    public void do_play(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
     // 私人播放
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
    }
    /**
     * 真实摄像头测试数据
     */
    @DataProvider
    public Object[][] playData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200, "真实存在设备，进行播放暂停操作"},
        };
    }
}
