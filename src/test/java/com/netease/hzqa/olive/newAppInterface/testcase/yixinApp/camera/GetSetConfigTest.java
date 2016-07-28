package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSetConfigTest {
    String getConfig_yixinAppUrl = "/yiXinApp/device/getConfig";
    String setConfig_yixinAppUrl = "/yiXinApp/device/setConfig";
    String deviceId = Config_yixinApp.test_deviceId;
    String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData_get", alwaysRun = true)
    public void getConfig_yixinAppTest_normal(int status,String getEntity, String openId, String deviceKey, int code, String msg) throws Exception{
        if(status == 1){
            CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        }
        else if(status >= 2){
            CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
        }
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(getEntity, openId, getConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData_get", alwaysRun = true, enabled = true)
    public void getConfig_yixinAppTest_error(String getEntity, String openId, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(getEntity, openId, getConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "normalData_set", alwaysRun = true)
    public void setConfig_yixinAppTest_normal(int status, String setEntity, String getEntity, String deviceKey, int code, String msg) throws Exception{
        if(status == 1){
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        }
        else if(status == 2){
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
            Thread.sleep(1000);
        }
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(setEntity, openId, setConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
        
        httpResponse = CommonOperation_yixinApp.postRequest(getEntity, openId, getConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_get()
    {
        return new Object[][]
        {
            {1,"{\"deviceId\":\""+ deviceId +"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 200,"主人获取并设置"},
            {2,"{\"deviceId\":\""+ deviceId +"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}", Config_yixinApp.mobile_openId, Config_yixinApp.mobile_deviceKey, 405,"非主任获取并设置"},
        };
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData_get()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\"1111111\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"12345678\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "不存在的用户+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "存在的用户（无区号）+不存在的设备号"},
            {"{\"deviceId\":\"918623\",\"userToken\":\"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "不传用户名"},
            {"{\"deviceId\":\"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"\"}", Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, 404, "不传用户（无区号）+设备号"},
        };
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_set()
    {
        return new Object[][]
        {
          //定时开关,定时打开,未设置时间段， 打开，关闭，定时状态切换
            {1,"{\"userToken\":\""+Config_yixinApp.test_userId+"\",\"deviceId\":\""+deviceId+"\",\"onlineStatus\":0,\"name\":\"我的摄像头10\","
                    + "\"deviceSwitch\":1,\"uploadSwitch\":1,\"lightSwitch\":1,\"rotateSwitch\":1,\"alertSensitivity\":1,"
                    + "\"alarmSwitch\":1,\"nightVisionSwitch\":1,\"audioSwitch\":1, \"clarity\":1}",
                    "{\"deviceId\":"+ deviceId +",\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey,
                    200 , "定时开关,定时打开,未设置时间段！"},
            //定时开关,定时打开,设置时间段(start<end)
            {2,"{\"userToken\":\""+Config_yixinApp.video_userId+"\",\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"onlineStatus\":0,\"name\":\"被接口测试修改\","
                    + "\"deviceSwitch\":2,\"uploadSwitch\":1,\"lightSwitch\":1,\"rotateSwitch\":1,\"alertSensitivity\":1,"
                    + "\"alarmSwitch\":1,\"nightVisionSwitch\":1,\"audioSwitch\":1, \"clarity\":1,"
                    + "\"deviceonofftime\":[{\"isEnable\":1,\"days\":[0,1,1,1,1,1,0],"
                    + "\"from\":{\"hour\":\"11\", \"min\":\"21\"},\"to\":{\"hour\":\"22\",\"min\":\"21\"}}]}",
                "{\"deviceId\":"+ Config_yixinApp.video_deviceId +",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200 , "定时开关,定时打开,设置时间段(start<end)！"},
            //定时开关,定时打开,设置多个时间段
            {3,"{\"userToken\":\""+Config_yixinApp.video_userId+"\",\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"onlineStatus\":0,\"name\":\"我的摄像头10\","
                    + "\"deviceSwitch\":2,\"uploadSwitch\":1,\"lightSwitch\":1,\"rotateSwitch\":1,\"alertSensitivity\":1,"
                    + "\"alarmSwitch\":1,\"nightVisionSwitch\":1,\"audioSwitch\":1, \"clarity\":1,"
                    + "\"deviceonofftime\":[{\"isEnable\":1,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"01\", \"min\":\"21\"},\"to\":{\"hour\":\"09\",\"min\":\"21\"}},"
                    + "{\"isEnable\":0,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"21\", \"min\":\"21\"},\"to\":{\"hour\":\"23\",\"min\":\"21\"}},"
                    + "{\"isEnable\":1,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"21\", \"min\":\"50\"},\"to\":{\"hour\":\"23\",\"min\":\"21\"}}]}",
                    "{\"deviceId\":"+ Config_yixinApp.video_deviceId +",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200 , "定时开关,定时打开,设置多个时间段！"},
            //定时开关,定时打开,设置重复时间段
            {4,"{\"userToken\":\""+Config_yixinApp.video_userId+"\",\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"onlineStatus\":0,\"name\":\"我的摄像头10\","
                    + "\"deviceSwitch\":2,\"uploadSwitch\":1,\"lightSwitch\":1,\"rotateSwitch\":1,\"alertSensitivity\":1,"
                    + "\"alarmSwitch\":1,\"nightVisionSwitch\":1,\"audioSwitch\":1, \"clarity\":1,"
                    + "\"deviceonofftime\":[{\"isEnable\":1,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"01\", \"min\":\"21\"},\"to\":{\"hour\":\"09\",\"min\":\"21\"}},"
                    + "{\"isEnable\":0,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"01\", \"min\":\"21\"},\"to\":{\"hour\":\"09\",\"min\":\"21\"}},"
                    + "{\"isEnable\":1,\"days\":[0,1,0,0,0,0,0],"
                    + "\"from\":{\"hour\":\"01\", \"min\":\"21\"},\"to\":{\"hour\":\"09\",\"min\":\"21\"}}]}",
                    "{\"deviceId\":"+ Config_yixinApp.video_deviceId +",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200 , "定时开关,定时打开,设置多个时间段(有重复)！"},
        };
    }
    
    /**
     * 将测试环境设备设置项退回开关全部打开状态
     */
    @Test(dataProvider = "normalData_return", alwaysRun = true, dependsOnMethods = "setConfig_yixinAppTest_normal")
    public void setConfig_yixinAppTest_return(int status, String setEntity, String getEntity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
        
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(setEntity, openId, setConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
        
        JSONObject httpResponse1 = CommonOperation_yixinApp.postRequest(getEntity, openId, getConfig_yixinAppUrl, deviceKey);
        Assert.assertEquals(httpResponse1.getInt("code"), code, msg); 
    }
    @DataProvider
    public Object[][] normalData_return()
    {
        return new Object[][]
        {
          //开关均打开
            {1,"{\"userToken\":\""+Config_yixinApp.video_userId+"\",\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"onlineStatus\":0,\"name\":\"被接口测试修改\","
                    + "\"deviceSwitch\":1,\"uploadSwitch\":1,\"lightSwitch\":1,\"rotateSwitch\":1,\"alertSensitivity\":1,"
                    + "\"alarmSwitch\":1,\"nightVisionSwitch\":1,\"audioSwitch\":1, \"clarity\":1}",
                "{\"deviceId\":"+ Config_yixinApp.video_deviceId +",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200 , "定时开关,定时打开,设置时间段(start<end)！"},

        };
    }
}
