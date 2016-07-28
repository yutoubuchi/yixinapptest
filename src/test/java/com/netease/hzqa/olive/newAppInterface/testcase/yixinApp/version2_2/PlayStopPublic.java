package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class PlayStopPublic {
    String openId;
    String indexurl = "/yiXinApp/square/index";
    String playurl = "/yiXinApp/square/play";
    String stopurl = "/yiXinApp/square/stop";
    String deviceId;
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, indexurl, Config_yixinApp.test_deviceKey);
        deviceId = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(9).getString("deviceId");
        //"163021507150929";//
    }
    
    @Test(dataProvider = "normal_data")
    public void playStopPublicTest(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, playurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 400, msg);
        res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, stopurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}",200,"播放暂停广场页公共摄像头"},    
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"86/13500000001\"}",200,"播放暂停广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"86/13500000002\"}",200,"播放暂停广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}",200,"播放暂停广场页公共摄像头"},
        };
    }
    
    @Test(dataProvider = "error_data")
    public void playStopPublicTest_error(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, playurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, stopurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 1220028, msg);
    }
    @DataProvider
    public Object[][] error_data(){
        return new Object[][]{
            {"{\"deviceId\":\"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}",1220027,"播放暂停广场页公共摄像头"},    
            {"{\"deviceId\":\"\",\"userToken\":\"\"}",1220027,"播放暂停广场页公共摄像头"},
        };
    }
    
    @Test(dataProvider = "notlogin_data")
    public void playStopPublicTest_notlogin(String entity, int code, String msg) throws Exception{
        
        //JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, playurl, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest_notlogin(entity, "", playurl, "");
        Assert.assertEquals(res.getInt("code"), 400, msg);
        //res = CommonOperation_yixinApp.postRequest(entity, openId, stopurl, Config_yixinApp.test_deviceKey);
        res = CommonOperation_yixinApp.postRequest_notlogin(entity, null, stopurl, null);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] notlogin_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\"}",200,"播放暂停广场页公共摄像头"},    
        };
    }
}
