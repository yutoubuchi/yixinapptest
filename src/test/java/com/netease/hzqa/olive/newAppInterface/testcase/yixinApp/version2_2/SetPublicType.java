package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SetPublicType {
    String openId;
    String url = "/yiXinApp/camera/setPublicType";
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
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
      }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void SetPublicType_normal(int status, String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void SetPublicType_error(int status,String entity, int code, String msg) throws Exception{
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
            {1,"{\"deviceId\":\"" + deviceId + "\",\"publicType\":2}", 1220028, "私人摄像机"},
            {2,"{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":0}", 200, "完全开放的摄像机"},
            {3,"{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":1}", 200, "第一页"},
            {4,"{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":2}", 200, "第二页"},
            {6,"{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":3}", 200, "完全开放的摄像机"},
            {7,"{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":2}", 200, "第一页"},
            {8,"{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":4}", 200, "第二页"},
            {9,"{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":0}", 200, "第一页"},
           {5,"{\"deviceId\":\"" + deviceId1 + "\",\"publicType\":0}", 200, "第一页"},
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
            {1,"{\"deviceId\":\"" + deviceId1 + "\"}", 200, "不传publicType"},
            {2,"{\"deviceId\":\"" + deviceId2 + "\",\"publicType\":-1}", 200, "publicType=-1"},
            {3,"{\"deviceId\":\"\",\"publicType\":1}", 1220001, "deviceId为空"},
            {4,"{\"deviceId\":\"123456\",\"publicType\":2}", 1220028, "deviceId不存在"},
            {5,"{\"publicType\":0}", 1220001, "不传deviceId"},
        };
    }
}
