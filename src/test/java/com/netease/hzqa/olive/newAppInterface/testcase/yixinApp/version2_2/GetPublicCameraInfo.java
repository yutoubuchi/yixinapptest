package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetPublicCameraInfo {
    String openId;
    String url = "/yiXinApp/camera/getPublicCameraInfo";
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
    }
    @Test(dataProvider = "normalData")
    public void getPubCamInfo_normal(int status,String entity, int code, String msg) throws Exception{
        if(status == 4)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceKey);
        
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
            {1,"{\"deviceId\":\"" + deviceId + "\"}", 1220028, "私人摄像机"},
            {2,"{\"deviceId\":\"" + deviceId1 + "\"}", 200, "完全开放的摄像机"},
            {3,"{\"deviceId\":\"" + deviceId2 + "\"}", 200, "观看需验证摄像机"},
            {4,"{\"deviceId\":\"" + deviceId + "\"}", 1220028, "私人摄像机"},
            {6,"{\"deviceId\":\"" + deviceId1 + "\"}", 403, "完全开放的摄像机"},
            {7,"{\"deviceId\":\"" + deviceId2 + "\"}", 403, "观看需验证摄像机，非主人，非关注者"},
            {8,"{\"deviceId\":\"" + deviceId2 + "\"}", 403, "观看需验证摄像机，关注者"},
        };
    }
    
    @Test(dataProvider = "errorData")
    public void getPubCamInfo_error(int status,String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
    }
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {1,"{\"deviceId\":\"\"}", 1220001, "空的deviceId"},
            {2,"{\"deviceId\":\"123123\"}", 1220028, "不存在deviceId"},
            {3,"{}", 1220001, "无参数"},
        };
    }
}
