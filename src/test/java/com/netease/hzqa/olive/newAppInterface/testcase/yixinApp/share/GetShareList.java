package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetShareList {
    String url = "/yiXinApp/share/getShareList";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception {

    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String user, String phoneArea, String pwd, String deviceId, int code, String deviceKey, String msg) throws Exception {
        //
        String entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\"" + phoneArea + "\",\"deviceId\":\"" + deviceId + "\"}";

        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        Assert.assertEquals(res.getJSONObject("result").getJSONArray("list").size(), 3, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void do_error(String user, String phoneArea, String pwd, String deviceId, int code, String deviceKey, String msg) throws Exception {
        //
        String entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\"" + phoneArea + "\",\"deviceId\":\"" + deviceId + "\"}";

        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, 200,
                        Config_yixinApp.test_deviceKey, "主人获取分享者列表" },
                { Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceId, 200,
                        Config_yixinApp.mobile_deviceKey, "分享者获取分享者列表" }, };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { Config_yixinApp.new_user, Config_yixinApp.new_area, Config_yixinApp.new_pwd, Config_yixinApp.test_deviceId1, 405,
                        Config_yixinApp.new_deviceKey, "非分享者非主人获取分享者列表" },
                { Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceId2, 405,
                        Config_yixinApp.mobile_deviceKey, "获取不被分享的设备好友列表" }, };
    }
}
