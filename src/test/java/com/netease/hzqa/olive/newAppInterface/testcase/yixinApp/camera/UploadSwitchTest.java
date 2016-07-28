package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class UploadSwitchTest {
    String url = "/yiXinApp/device/getDeviceUploadSwich";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
        // openId = CommonOperation_yixinApp.getOpenId2_1("13999999002", "355",
        // "123456",
        // Config_yixinApp.test_deviceKey);
        System.out.println(openId);
    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void do_error(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] { { "{\"deviceId\":\"" + Config_yixinApp.test_deviceId
                + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人邀请用户" }, };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { "{\"deviceId\":\"\"}", 404, openId, Config_yixinApp.test_deviceKey, "deviceID为空" }, 
                { "{\"deviceId\":\"123456\"}", 404, openId, Config_yixinApp.test_deviceKey, "不存在的deviceID" }, 
                };
    }
}
