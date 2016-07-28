package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class AudioSwitch {
    String url = "/yiXinApp/device/openAudioSwitch";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
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
        return new Object[][] { 
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId1
            + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "deviceID为空" }, };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { "{\"userName\":\"" + Config_yixinApp.mobile_user + "\",\"phoneArea\":\"\",\"deviceId\":\"" + Config_yixinApp.test_deviceId1
                        + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "username为空" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId1
                        + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "deviceID为空" }, };
    }
}
