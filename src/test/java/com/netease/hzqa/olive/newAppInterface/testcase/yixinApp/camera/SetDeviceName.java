package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SetDeviceName {
    String openId = BASE64.encode("oliveApp20");
    String pwd = "Uik6n5ba4wW19FpMtLfRKmmKjBdgUo6CTQZtI9dLiPL/hoqrS+RjDo3G21wqTpSwEu13vJBZNZstLTaD0OipM/2jUDhfrJqAuNIEucEkBN8=";
    String loginToken = "d7be2113-bbad-43bf-8b7c-15580f8adbf8";
    String url = "/yiXinApp/device/setCameraName";
    String deviceId = Config_yixinApp.test_deviceId1;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = false)
    public void setDeviceName_normal(String entity, String deviceKey, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！"); 
//        Assert.assertEquals(httpResponse.getBoolean("result"), true, "检查是否已添加失败！");
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void setDeviceName_error(String entity, String deviceKey, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
//        Assert.assertEquals(httpResponse.getBoolean("result"), false, "检查是否已添加失败！");
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 200, "存在的用户+存在的设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\",\"name\":\"\"}", Config_yixinApp.test_deviceKey, 200, "不传用户（无区号）+设备号"},
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
            {"{\"deviceId\":\"918623\",\"userToken\":\""+Config_yixinApp.test_userId+"\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 404, "存在的用户+不存在的设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"86/11900000001\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 200, "不存在的用户+存在的设备号,检验openId和cookie"},
            {"{\"deviceId\":\""+918623+"\",\"userToken\":\"13500000001\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 404, "存在的用户（无区号）+不存在的设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 200, "不传用户名,检验openId和cookie"},
            {"{\"deviceId\":\"\",\"userToken\":\""+Config_yixinApp.test_userId+"\",\"name\":\"set new device name\"}", Config_yixinApp.test_deviceKey, 404, "不传设备号"},
            {"{\"deviceId\":\"\",\"userToken\":\"\",\"name\":\"\"}", Config_yixinApp.test_deviceKey, 404, "不传用户（无区号）+设备号"},
        };
    }
}
