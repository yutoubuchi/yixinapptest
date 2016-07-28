package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class CheckAbstract {
    String openId;
    String url = "/yiXinApp/camera/checkAbstract";
    String deviceId = Config_yixinApp.test_deviceId;
    String deviceId1 = Config_yixinApp.test_deviceId1;
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
      //设为公共摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPublic", Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData_pub", alwaysRun = true)
    public void setDeviceAbstract_normal_pub(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData_pub", alwaysRun = true)
    public void setDeviceAbstract_error_pub(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_pub()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+deviceId+"\",\"abstractInfo\":\"set new device abstractInfo\"}", 200, "存在的设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"abstractInfo\":\"中共政圈\"}", 1220032, "包含敏感词"},
            {"{\"deviceId\":\""+deviceId+"\",\"abstractInfo\":\"这是我的中共政圈哈哈哈\"}", 1220032, "包含敏感词"},
            {"{\"deviceId\":\""+deviceId+"\",\"abstractInfo\":\"\"}", 200, "abstractInfo为空"},
        };
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData_pub()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\"\",\"abstractInfo\":\"set new device name\"}", 1220001, "不传设备号"},
            {"{\"abstractInfo\":\"set new device name\"}", 1220001, "不传设备号"},
            {"{\"deviceId\":\"\"}", 1220001, "不传abstractInfo"},
        };
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void setDeviceAbstract_normal(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void setDeviceAbstract_error(String entity, int code, String msg) throws Exception{
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
            {"{\"deviceId\":\""+deviceId1+"\",\"abstractInfo\":\"set new device name\"}", 407, "存在的设备号"},
            {"{\"deviceId\":\""+deviceId1+"\",\"abstractInfo\":\"中共政圈\"}", 407, "包含敏感词"},
            {"{\"deviceId\":\""+deviceId1+"\",\"abstractInfo\":\"这是我的中共政圈哈哈哈\"}", 407, "包含敏感词"},
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
            {"{\"deviceId\":\"918623\",\"abstractInfo\":\"set new device name\"}", 407, "不存在的设备号"},
            {"{\"deviceId\":\"\",\"abstractInfo\":\"set new device name\"}", 1220001, "不传设备号"},
            {"{\"deviceId\":\""+deviceId1+"\",\"abstractInfo\":\"\"}", 407, "设备名为空+设备号为空"},
        };
    }

    @AfterClass
    public void afterClass() throws Exception
    {
        //设为私人摄像头
        CommonOperation_yixinApp.postRequest("{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", openId, "/yiXinApp/camera/setAsPrivate", Config_yixinApp.test_deviceKey);
    }
}
