package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class CheckDeviceNameTest {
    String openId = BASE64.encode("oliveApp20");
    String url = "/yiXinApp/device/checkDeviceName";

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void checkDeviceName_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception {
            JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), code, msg);
            if (status <= 3) {
                Assert.assertEquals(httpResponse.getBoolean("result"), false, msg);
            }
            else {
                Assert.assertEquals(httpResponse.getBoolean("result"), true, msg);
            }
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void checkDeviceName_error(String entity, String deviceKey, int code, String msg) throws Exception {
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
        // Assert.assertEquals(httpResponse.getBoolean("result"), true, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { 1, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"中共政圈\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },
                { 2, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"习军急上马\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },
                { 3, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"六四悬案\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },
                { 4, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },
                { 5, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"lalll啦啦啦啦\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },
                { 6, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId2 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\",\"name\":\"六四红红红红红\"}",
                        Config_yixinApp.test_deviceKey, 200, "fuck，fuck，fuck" },

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                // {"{\"deviceId\":\"" + Config_yixinApp.deviceId +
                // "\",\"userToken\":\"" + Config_yixinApp.test_userId +
                // "\",\"name\":\"shit,shit,shit,shit\"}",
                // Config_yixinApp.test_deviceKey, 200, "存在的用户（无区号）+不存在的设备号"},
                // {"{\"deviceId\":\"" + Config_yixinApp.test_deviceId +
                // "\",\"userToken\":\"\",\"name\":\"累死宝宝了\"}",
                // Config_yixinApp.test_deviceKey, 200, "不传用户名"},
                // {"{\"deviceId\":\"\",\"userToken\":\"4916013\",\"name\":\"混蛋混蛋混蛋\"}",
                // Config_yixinApp.test_deviceKey, 200, "不传设备号"},
                // {"{\"deviceId\":\"\",\"userToken\":\"\",\"name\":\"耍流氓耍流氓\"}",
                // Config_yixinApp.test_deviceKey, 200, "设备和用户名都不传"},

        };
    }
}
