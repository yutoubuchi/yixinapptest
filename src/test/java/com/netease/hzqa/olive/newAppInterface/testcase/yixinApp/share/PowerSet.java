package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class PowerSet {
    String url = "/yiXinApp/share/powerSet";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
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
    public Object[][] normalData() {//
        return new Object[][] {
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":1,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.mobile_userId + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人设置语音关闭" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":2,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.mobile_userId + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人设置告警关闭" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":0,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.email_userId + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人设置录像关闭" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":0,\"setOpen\":1,\"targetUserId\":\"" + Config_yixinApp.email_userId + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人设置录像打开" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":2,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.new_userId + "\"}", 401, openId, Config_yixinApp.test_deviceKey, "主人操作非分享者" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"powerType\":0,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.yixin_userId + "\"}", 401, openId, Config_yixinApp.test_deviceKey, "主人操作非分享者" },
        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId2
                + "\",\"powerType\":1,\"setOpen\":0,\"targetUserId\":\"\"}", 401, openId, Config_yixinApp.test_deviceKey, "分享者为空" },
        { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" 
                + "\",\"powerType\":2,\"setOpen\":0,\"targetUserId\":\"" + Config_yixinApp.mobile_userId + "\"}", 1220027, openId, Config_yixinApp.test_deviceKey, "设备号为空" },
        { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                + "\"targetUserId\":\"" + Config_yixinApp.email_userId + "\"}", 400, openId, Config_yixinApp.test_deviceKey, "开关不传" },
        { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                    + "\",\"powerType\":0,\"setOpen\":4,\"targetUserId\":\"" + Config_yixinApp.email_userId + "\"}", 200, openId, Config_yixinApp.test_deviceKey, "开关异常值" },

        };
    }
}
