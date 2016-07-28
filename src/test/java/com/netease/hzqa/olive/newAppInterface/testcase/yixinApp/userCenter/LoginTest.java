package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class LoginTest {

    @BeforeClass
    public void beforeClass() throws Exception {

    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void login2_1Test_normal(int status, String userName, String phoneArea, String pwd, String deviceKey, Boolean isAuto, int code, String msg)
            throws Exception
    {
        if (status == 6) {
            Thread.sleep(310000);
            JSONObject res = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
            Assert.assertEquals(res.getInt("code"), code, msg);
        }
        else {
            JSONObject res = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
            Assert.assertEquals(res.getInt("code"), code, msg);
        }
        // Assert.assertEquals(res.getJSONObject("result").getInt("RET_CODE"),
        // code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = false)
    public void login2_1Test_error(int status, String userName, String phoneArea, String pwd, String deviceKey, Boolean isAuto, int code, String msg)
            throws Exception
    {

        if (status == 7) {
            for (int i = 0; i < 7; i++) {
                JSONObject res = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
                // System.out.println("testcase7:"+" -"+i+" :
                // "+res.getInt("code"));
            }
        }

        if (status == 8) {
            for (int i = 0; i < 4; i++) {
                JSONObject res = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
                // System.out.println("testcase8: "+" -"+i+" :
                // "+res.getInt("code"));
            }

            JSONObject res0 = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
            // System.out.println("testcase8-4 :"+res0.getInt("code"));
            Assert.assertEquals(res0.getInt("code"), code, msg);

            for (int i = 0; i < 4; i++) {
                JSONObject res = CommonOperation_yixinApp.login2_1(userName, phoneArea, pwd, deviceKey, isAuto);
                // System.out.println("testcase8:"+" -"+(5+i)+" :
                // "+res.getInt("code"));
            }
        }
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] { 
            { 1, "13067700001", "60", "111111", "8C-89-A5-E9-D9-E2", false, 200, "登录用户1,在设备1，非自动登录，成功" },
                { 2, "13999990012", "60", "123456", "8C-89-A5-E9-D9-E2", false, 200, "非86区号，用户存在" },
                { 3, "13999999030", "355", "123456", "8C-89-A5-E9-D9-E2", false, 1220003, "不传区号，区号默认86，用户" },
                { 4, "13999999031", "60", "123456", "8C-89-A5-E9-D9-E2", false, 200, "登录用户1,在设备1，非自动登录，成功" },
                { 5, "13999999015", "86", "123456", "8C-89-A5-E9-D9-E2", false, 200, "用户不存在" },
                { 6, "13999999015", "86", "123456", "8C-89-A5-E9-D9-E2", true, 200, "用户不存在" }, };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] { { 1, "19900000001", "86", "1234567", Config_yixinApp.deviceKey, 1220005, "密码错误" },
                { 2, "", "86", "1234567", Config_yixinApp.deviceKey, 1220001, "userName为空" },
                { 3, "19900000002", "86", "", Config_yixinApp.deviceKey, 1220001, "密码为空" },
                { 4, "19900000001", "1186", "123456", Config_yixinApp.deviceKey, 1220001, "用户不存在，区号不对" },
                { 5, "19900000111", "86", "1234567", Config_yixinApp.deviceKey, 1220001, "手机用户不存在" },
                { 6, "zhengyinyan21", "86", "1234567", Config_yixinApp.deviceKey, 1220003, "登录用户2,邮箱用户,无后缀" }, };
    }
}
