package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONObject;

public class SquareHeart {
    String openId="";
    String indexurl = "/yiXinApp/square/index";
    String url = "/yiXinApp/square/heart";
    String deviceId;
    @BeforeClass
    public void doBefore() throws Exception {
 //       openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest_notlogin("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, indexurl, Config_yixinApp.test_deviceKey);
        deviceId = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
    }

    @Test(dataProvider = "normal_data")
    public void SquareHeartTest(String entity, int code, String msg) throws Exception{
        
        JSONObject res = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\"}",200,"广场页公共摄像头播放心跳"},    
            {"{\"deviceId\":\"\"}",1220001,"广场页公共摄像头播放心跳，设备号为空"},
            {"{\"deviceId\":\"1234556\"}",403,"广场页公共摄像头播放心跳，设备不存在"},
            {"{\"deviceId\":\"163021515123516\"}",403,"广场页公共摄像头播放心跳，设备离线"},
        };
    }
}
