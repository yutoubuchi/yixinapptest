package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class AskForWatchCamera {
    String url = "/yiXinApp/share/askForWatchCamera";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception
    {
//        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, "86", Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String user, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception {
        //
        String entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\"}";
        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);

    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = true)
    public void do_error(String entity, String deviceKey, int code, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey); 
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 1220026, "主人请求查看自己设备" },
                { Config_yixinApp.mobile_user, "86", Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.mobile_deviceKey, 200,"老易信用户查看设备" },
                { Config_yixinApp.email_user, "86", Config_yixinApp.email_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.email_deviceKey, 200,"老易信用户查看设备" },
                { Config_yixinApp.new_user, "86", Config_yixinApp.new_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.new_deviceKey, 200,"易信青果APP新注册用户查看设备" },
                { Config_yixinApp.yixin_user, "86", Config_yixinApp.new_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.new_deviceKey, 200,"易信青果APP新注册用户查看设备" },
        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
            {"{\"userName\":\"\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\""+Config_yixinApp.test_deviceId+"\"}", Config_yixinApp.test_deviceKey, 1220026, "用户名为空"},
            {"{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\"\"}", Config_yixinApp.test_deviceKey, 1220001, "deviceId为空"},
            {"{\"userName\":\"\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\"\"}", Config_yixinApp.test_deviceKey, 1220001, "用户名和deviceId为空"},
        };
    }
}
