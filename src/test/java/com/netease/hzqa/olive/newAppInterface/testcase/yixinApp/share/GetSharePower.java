package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSharePower {
    String url = "/yiXinApp/share/getSharePower";
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

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = true)
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
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"inviteUid\":\"" + Config_yixinApp.mobile_userId + "\"}", 200,
                        openId, Config_yixinApp.test_deviceKey, "主人获取分享者开关" }, 
                {"{\"userName\":\"" + Config_yixinApp.test_user + "\",\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"inviteUid\":\"" + Config_yixinApp.email_userId + "\"}", 200,
                openId, Config_yixinApp.test_deviceKey, "主人获取分享者开关" },
                {"{\"userName\":\"" + Config_yixinApp.test_user + "\",\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"inviteUid\":\"" + Config_yixinApp.new_userId + "\"}", 400,
                    openId, Config_yixinApp.test_deviceKey, "主人获取非分享者开关" },
                {"{\"userName\":\"" + Config_yixinApp.test_user + "\",\"deviceId\":\"" + Config_yixinApp.test_deviceId + "\",\"inviteUid\":\"" + Config_yixinApp.yixin_userId + "\"}", 400,
                        openId, Config_yixinApp.test_deviceKey, "主人获取非分享者开关" },
                };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { "{\"userName\":\"86/1190000001\",\"phoneArea\":\"86\",\"deviceId\":\"163021505010522\"}", 1220001, openId, Config_yixinApp.test_deviceKey,
                        "存在的手机用户+区号为空，参数不合法" },
                { "{\"userName\":\"\",\"phoneArea\":\"861\",\"deviceId\":\"163021505010522\"}", 1220001, openId, Config_yixinApp.test_deviceKey,
                        "用户名为空+不存在的区号，参数不合法" },
                { "{\"userName\":\"\",\"phoneArea\":\"\",\"deviceId\":\"163021505010522\"}", 1220001, openId, Config_yixinApp.test_deviceKey,
                        "用户名为空+区号为空，参数不合法" },
                { "{\"userName\":\"123456789\",\"deviceId\":\"\"}", 1220001, openId, Config_yixinApp.test_deviceKey, "只传用户名，参数不合法" },
                { "{\"userName\":\"191900000001\",\"phoneArea\":\"86\"}", 1220001, openId, Config_yixinApp.test_deviceKey, "os参数为空，参数不合法" }, };
    }
}
