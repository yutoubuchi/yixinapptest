package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetAppUserByDeviceId {
    String url = "/yiXinApp/share/getAppUserByDeviceId";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception {

    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        if(status <=2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
        else if(status <=3)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.email_user, Config_yixinApp.mobile_area, Config_yixinApp.email_pwd,
                Config_yixinApp.email_deviceKey);
        else
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd,
                Config_yixinApp.mobile_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        if(status <= 2){
            Assert.assertEquals(res.getJSONObject("result").getBoolean("shared"), false, msg);
        }
        else{
            Assert.assertEquals(res.getJSONObject("result").getBoolean("shared"), true, msg);
        }
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = true)
    public void do_error(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                {1, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\"}", 200, Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, "主人获取信息" },
                {2, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"deviceId\":\"163021515123517\"}", 200, Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, "主人获取信息" },
                {3, "{\"userName\":\"" + Config_yixinApp.email_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, Config_yixinApp.email_openId, Config_yixinApp.email_deviceKey, "非分享者非主人获取信息" },
                {4, "{\"userName\":\"" + Config_yixinApp.mobile_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, Config_yixinApp.mobile_openId, Config_yixinApp.mobile_deviceKey, "分享者非主人获取信息" },
        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                 {"{\"userName\":\"86/1190000001\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123517\"}", 200, openId, Config_yixinApp.test_deviceKey, "存在的手机用户+区号为空，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"861\",\"deviceId\":\"163021515123517\"}",   200, openId, Config_yixinApp.test_deviceKey, "用户名为空+不存在的区号，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"\",\"deviceId\":\"163021515123517\"}", 200, openId, Config_yixinApp.test_deviceKey, "用户名为空+区号为空，参数不合法"},
                 {"{\"userName\":\"123456789\",\"deviceId\":\"\"}", 1220001,openId, Config_yixinApp.test_deviceKey, "只传用户名，参数不合法"},
                 {"{\"userName\":\"11900000001\",\"phoneArea\":\"86\"}",1220001, openId, Config_yixinApp.test_deviceKey, "os参数为空，参数不合法"},
        };
    }
}
