package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class CreateUserQrPic {
    String url = "/yiXinApp/share/createUserQrPic";
    String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String entity, int code, String openId, String deviceKey, String msg) throws Exception {
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
                {1, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "主人（易信用户）生成分享二维码" },
                {2, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "非主人(独立app用户)生成分享二维码" },
                {3, "{\"userName\":\"" + Config_yixinApp.mobile_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "非主人(易信用户)生成分享二维码" },

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                 {"{\"userName\":\"13500000001\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "存在的手机用户+区号为空，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "用户名为空+不存在的区号，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"\",\"deviceId\":\"163021515123516\"}", 200, openId, Config_yixinApp.test_deviceKey, "用户名为空+区号为空，参数不合法"},
                 {"{\"userName\":\"123456789\",\"deviceId\":\"\"}", 1220001,openId, Config_yixinApp.test_deviceKey, "只传用户名，参数不合法"},
                 {"{\"userName\":\"11900000001\",\"phoneArea\":\"86\"}",1220001, openId, Config_yixinApp.test_deviceKey, "os参数为空，参数不合法"},
        };
    }
}
