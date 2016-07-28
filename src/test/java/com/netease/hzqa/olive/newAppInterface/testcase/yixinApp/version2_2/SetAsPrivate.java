package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SetAsPrivate {
    String openId;
    String url = "/yiXinApp/camera/setAsPrivate";
    String urlPublic = "/yiXinApp/camera/setAsPublic";
    String playUrl = "/yiXinApp/private/play";
    String stopUrl = "/yiXinApp/private/stop";
    String deviceId = Config_yixinApp.video_deviceId;

    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd,
                Config_yixinApp.video_deviceKey);
    }

    @Test(dataProvider = "private_data")
    public void setAsPrivateTest(int status, String entity, String deviceKey, int code, String msg) throws Exception {
        if (status == 2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                    Config_yixinApp.video_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        if (status == 1) {
            // 私人播放
            JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, playUrl, deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), code, "播放私人摄像头失败！");
            // 暂停播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, stopUrl, deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), code, "暂停播放私人摄像头失败！");
            
         // 公共播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, "/yiXinApp/square/play", deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), 1220028, "播放公共摄像头失败！");
            // 暂停播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, "/yiXinApp/square/stop", deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), 1220028, "暂停播放公共摄像头失败！");
        }
    }

    @DataProvider
    public Object[][] private_data() {
        return new Object[][] {
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\"}", Config_yixinApp.video_deviceKey, 200,
                        "主人设置私人摄像头" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13067700006\"}", Config_yixinApp.video_deviceKey, 200, "主人设置私人摄像头，多次设置" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"\"}", Config_yixinApp.video_deviceKey, 200, "主人设置私人摄像头，多次设置" },
                { 3, "{\"deviceId\":\"\",\"userToken\":\"\"}", Config_yixinApp.video_deviceKey, 404, "主人设置私人摄像头，摄像机不存在" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\"}", Config_yixinApp.test_deviceKey, 1220020,
                        "主人设置私人摄像头" },
                // 非主人操作
                { 2, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", Config_yixinApp.video_deviceKey, 403,
                        "非主人设置私人摄像头,openid不符" },
                { 2, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13500000001\",\"focusStatus\":0}", Config_yixinApp.video_deviceKey, 403,
                        "非主人设置私人摄像头，openid不符" }, };
    }
}
