package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

/**
 * 主人发起分享邀请
 * 
 * @Description
 * @author hzzhengyinyan
 * @date 2016年3月25日 上午11:46:08
 */
public class ShareInvite {
    String url = "/yiXinApp/share/yiXinShareInvite";
    String openId;
    String openId1 = "oliveApp190";
    String openId2 = "oliveApp191";

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
    }

    @Test(dataProvider = "normalData", alwaysRun = true, enabled = true)
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
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                + "\",\"shareList\":[\"" + Config_yixinApp.mobile_userId + "\",\"" + Config_yixinApp.email_userId + "\"]}", 200, openId, Config_yixinApp.test_deviceKey, "主人邀请分享者" }, 
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                + "\",\"shareList\":[\"" + Config_yixinApp.new_userId + "\",\"" + Config_yixinApp.yixin_userId + "\"]}", 200, openId, Config_yixinApp.test_deviceKey, "主人邀请非分享者" }, 
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                    + "\",\"shareList\":[\"" + Config_yixinApp.new_userId + "\",\"" + Config_yixinApp.yixin_userId + "\"]}", 200, openId, Config_yixinApp.test_deviceKey, "主人再次邀请非分享者" }, 

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"shareList\":[\"b2xpdmVBcHAyNDQ=\",\"b2xpdmVBcHAyMDI=\"]}", 400, openId, Config_yixinApp.test_deviceKey, "主人邀请用户，独立APP用户" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId
                        + "\",\"shareList\":[]}", 200, openId, Config_yixinApp.test_deviceKey, "主人邀请用户，用户列表为空" }, };
    }
}
