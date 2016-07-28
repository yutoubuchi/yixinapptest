package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class RemoveDevice {
    String openId = BASE64.encode("oliveApp91");
    String url = "/yiXinApp/device/removeDevice";
    
    @BeforeClass
    public void doBefore() throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "data_normal",enabled = true)
    public void test_normal(String user, String userToken, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception{
        
        String entity = "{\"userName\":\"" + userToken + "\",\"deviceId\":\""+deviceId+"\"}";
        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    
    @DataProvider
    public Object[][] data_normal(){
        return new Object[][]{
            {  Config_yixinApp.new_user, Config_yixinApp.new_userId, "86", Config_yixinApp.new_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 400, "分享者删除设备"},
            {  Config_yixinApp.test_user, Config_yixinApp.test_userId, "86", Config_yixinApp.test_pwd, Config_yixinApp.deviceId, Config_yixinApp.test_deviceKey,  400, "主人删除非自己的设备"},
       //     {  Config_yixinApp.test_user, Config_yixinApp.test_userId, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey,  200, "主人删除自己的设备"},
        };
    }
    
    @Test(dataProvider = "errorData")
    public void test_error(String user, String userToken, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception{
        String entity = "{\"userName\":\"" + userToken + "\",\"deviceId\":\""+deviceId+"\"}";
        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {  Config_yixinApp.new_user, "", "86", Config_yixinApp.new_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 400, "userToken为空"},
            {  Config_yixinApp.test_user, Config_yixinApp.test_userId, "86", Config_yixinApp.test_pwd, "", Config_yixinApp.test_deviceKey,  400, "设备id为空"},
        };
    }
}
