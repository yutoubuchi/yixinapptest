package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import java.util.ArrayList;
import java.util.List;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONObject;

public class GetTimeLineTest {
    String openId = BASE64.encode("oliveApp20");
    String userName = Config_yixinApp.test_user;
    String url = "/yiXinApp/video/timeline";
    List<Parameter> paras = new ArrayList<Parameter>();
    String deviceId = Config_yixinApp.test_deviceId1;
    String userToken = Config_yixinApp.test_userId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {      
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void getTimeLineTest_normal(String userName, String deviceId, String deviceKey, int code, String msg) throws Exception{
        paras.add(new Parameter("deviceId", deviceId));
        paras.add(new Parameter("userToken", userName)); 
        
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void getTimeLineTest_error(String userName, String deviceId, String deviceKey, int code, String msg) throws Exception{
        paras.add(new Parameter("deviceId", deviceId));
        paras.add(new Parameter("userToken", userName)); 
        
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, url, deviceKey);
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
            {userName, deviceId, Config_yixinApp.test_deviceKey, 200, "存在的用户名+实际存在的设备号"},
            {userName, Config_yixinApp.mobile_deviceId, Config_yixinApp.test_deviceKey, 405, "非主人非分享者+设备号"},
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
          //  {userName, deviceId, 404, "存在的用户名+实际不存在的设备号"},
            {userName, "1234567890", Config_yixinApp.test_deviceKey, 404, "存在的用户名+实际不存在的设备号"},
            {"", deviceId, Config_yixinApp.test_deviceKey, 200, "存在的用户名+实际不存在的设备号"},
            {userName, "", Config_yixinApp.test_deviceKey, 404, "存在的用户名+实际不存在的设备号"},
            {"", "", Config_yixinApp.test_deviceKey, 404, "存在的用户名+实际不存在的设备号"},
           
        };
    }
}
