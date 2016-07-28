package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class PrivatePlayTest {
    String openId = BASE64.encode("oliveApp20");
    String playUrl = "/yiXinApp/private/play";
    String stopUrl = "/yiXinApp/private/stop";

    @BeforeClass
    public void beforeClass() throws Exception {

    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void privatePlayTest_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
        // 私人播放
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, playUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
        // 暂停播放
        JSONObject httpResponse1 = CommonOperation_yixinApp.postRequest(entity, openId, stopUrl, deviceKey);
        if (status == 1) {            
            Assert.assertEquals(httpResponse1.getInt("code"), 200, "检查是否已添加失败！");
        }
        else{            
            Assert.assertEquals(httpResponse1.getInt("code"), code, "检查是否已添加失败！");
        }
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void privatePlayTest_error(String entity, String deviceKey, int code, String msg) throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
        // 私人播放
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, playUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
        // 暂停播放
        JSONObject httpResponse1 = CommonOperation_yixinApp.postRequest(entity, openId, stopUrl, deviceKey);
        Assert.assertEquals(httpResponse1.getInt("code"), code, "检查是否已添加失败！");
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { 1, "{\"deviceId\":\"" + Config_yixinApp.test_deviceId1 + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}",
                        Config_yixinApp.test_deviceKey, 3000, "存在的用户+假的设备" },
                { 2, "{\"deviceId\":\"163021505003970\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", Config_yixinApp.test_deviceKey, 405,
                        "存在的用户+假的设备" }, };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] { { "{\"deviceId\":\"918623\",\"userToken\":\"4916013\"}", Config_yixinApp.test_deviceKey, 404, "存在的用户+不存在的设备号" },
                { "{\"deviceId\":\"918623\",\"userToken\":\"\"}", Config_yixinApp.test_deviceKey, 404, "不传用户名" },
                { "{\"deviceId\":\"\",\"userToken\":\"4916013\"}", Config_yixinApp.test_deviceKey, 404, "不传设备号" }, };
    }
    
    /**
     * 测试环境真实存在设备，执行设备播放
     */
    @Test(dataProvider = "playData", alwaysRun = true)
    public void do_play(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
     // 私人播放
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, playUrl, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
        // 暂停播放
        JSONObject httpResponse1 = CommonOperation_yixinApp.postRequest(entity, openId, stopUrl, deviceKey);
        Assert.assertEquals(httpResponse1.getInt("code"), code, "检查是否已添加失败！");
    }
    /**
     * 真实摄像头测试数据
     */
    @DataProvider
    public Object[][] playData()
    {
        return new Object[][]
        {
            {"{\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"userToken\":\""+Config_yixinApp.video_userId+"\"}", Config_yixinApp.video_deviceKey, 200, "真实存在设备，进行播放暂停操作"},
        };
    }
}
