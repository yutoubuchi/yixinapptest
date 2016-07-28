package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class VoiceSend {
    String url = "/yiXinApp/video/voiceSend";
    String openId;

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String user, String pwd, String deviceId, int code, String deviceKey, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(user, "86", pwd, deviceKey);

        String entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\"86\",\"deviceId\":\""+deviceId+"\",\"fileKey\":\"www.baidu.com\"}";
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void do_error(String entity, int code, String deviceKey, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { Config_yixinApp.test_user, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId2, 200, Config_yixinApp.test_deviceKey, "主人" },
                { Config_yixinApp.test_user, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, 200, Config_yixinApp.test_deviceKey, "主人+不存在设备" },
                { Config_yixinApp.email_user, Config_yixinApp.email_pwd, Config_yixinApp.test_deviceId1, 405, Config_yixinApp.email_deviceKey, "主人" },
                { Config_yixinApp.test_user, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId1, 200, Config_yixinApp.test_deviceKey, "分享者" },
                { Config_yixinApp.mobile_user, Config_yixinApp.mobile_pwd, Config_yixinApp.test_deviceId1, 405, Config_yixinApp.mobile_deviceKey, "非主人，非分享者播放语音" },
                { Config_yixinApp.video_user, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceId, 200, Config_yixinApp.video_deviceKey, "真实存在设备，语音发送" },
        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                 {"{\"userName\":\"\",\"phoneArea\":\"86\",\"deviceId\":\""+Config_yixinApp.test_deviceId2+"\",\"fileKey\":\"www.baidu.com\"}", 200, Config_yixinApp.test_deviceKey, "用户名为空+不存在的区号，参数不合法"},
                 {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"deviceId\":\""+Config_yixinApp.test_deviceId2+"\"}", 1220001, Config_yixinApp.test_deviceKey, "只传用户名和 deviceid，参数不合法"},
                 {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"deviceId\":\"\",\"fileKey\":\"www.baidu.com\"}",400, Config_yixinApp.test_deviceKey, "deviceid 为空，参数不合法"},
        };
    }
}
