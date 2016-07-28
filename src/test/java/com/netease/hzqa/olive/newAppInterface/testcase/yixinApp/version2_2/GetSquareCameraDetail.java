package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSquareCameraDetail {
    String openId;
    String tagurl = "/yiXinApp/square/tagList";
    String indexurl = "/yiXinApp/square/index";
    String detailurl = "/yiXinApp/square/cameraDetail";
    String deviceId;
    
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, indexurl, Config_yixinApp.test_deviceKey);
        deviceId = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
    }
    
    
    @Test(dataProvider = "normal_data")
    public void getCameraDetail(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, detailurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\",\"relateNum\":0,\"location\":\"120.222798,30.212248\"}", 200, "用例1：相关推荐数为0"},
            {"{\"deviceId\":\""+deviceId+"\",\"relateNum\":1,\"location\":\"110.222798,60.212248\"}", 200, "用例2：相关推荐数为1，用户有另一台公有设备"},
            {"{\"deviceId\":\""+deviceId+"\",\"relateNum\":2,\"location\":\"90.222798,30.212248\"}", 200, "用例3：相关推荐数为2，用户另一台公有设备+推荐列表一台"},
            {"{\"deviceId\":\""+deviceId+"\",\"relateNum\":5,\"location\":\"20.222798,30.212248\"}", 200, "用例4：相关推荐数为5，等于用户公有设备+推荐列表数，返回5台相关推荐"},
            {"{\"deviceId\":\""+deviceId+"\",\"relateNum\":6,\"location\":\"80.222798,30.212248\"}", 200, "用例5：相关推荐数为6，等于用户公有设备+推荐列表数，返回6台相关推荐"},
        };
    }
    
    @Test(dataProvider = "error_data")
    public void getCameraDetail_error(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, detailurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] error_data(){
        return new Object[][]{
            { "{\"deviceId\":\""+deviceId+"\",\"relateNum\":0,\"location\":\"80.222798,30.212248\"}", 200, "用例1：相关推荐数为0"},
            { "{\"deviceId\":\""+deviceId+"\",\"relateNum\":,\"location\":\"\"}", 400, "用例2：相关推荐数为空"},
            { "{\"deviceId\":\""+deviceId+"\",\"relateNum\":-2,\"location\":\"80.222798,30.212248\"}", 1220001, "用例3：相关推荐数为-2"},
            { "{\"relateNum\":6,\"location\":\"80.222798,30.212248\"}", 1220001, "设备号不传"},
            { "{\"deviceId\":\"\",\"relateNum\":6,\"location\":\"80.222798,30.212248\"}", 1220001, "设备号为空"},
        };
    }
}
