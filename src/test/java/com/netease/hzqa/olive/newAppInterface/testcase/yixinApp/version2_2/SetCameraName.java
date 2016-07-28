package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SetCameraName {
    String openId;
    String url = "/yiXinApp/camera/setCameraName";
    String deviceId = Config_yixinApp.test_deviceId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void setDeviceName_normal(String entity, int code, String msg) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void setDeviceName_error(String entity, int code, String msg) throws Exception{
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
            {"{\"deviceId\":\""+deviceId+"\",\"name\":\"set new device name\"}", 200, "存在的设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"name\":\"中共政圈\"}", 1220032, "包含敏感词"},
            {"{\"deviceId\":\""+deviceId+"\",\"name\":\"这是我的中共政圈哈哈哈\"}", 1220032, "包含敏感词"},
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
            {"{\"deviceId\":\"918623\",\"name\":\"set new device name\"}", 1220027, "不存在的设备号"},
            {"{\"deviceId\":\"\",\"name\":\"set new device name\"}", 1220001, "不传设备号"},
            {"{\"deviceId\":\""+deviceId+"\",\"name\":\"\"}", 1220001, "设备名为空+设备号为空"},
        };
    }
}
