package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class LogoutTest {
    String url = "/yiXinApp/user/loginOut";
    String openId = "99268381def0e201";
    String deviceKey = "8C-89-A5-E9-D9-E2";
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1("13999990012", "86", "123456", deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void logoutTest_normal(int status, String entity, int code, String msg) throws Exception{
        JSONObject res = CommonOperation_yixinApp.postRequest(entity,openId,url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true, dependsOnMethods = {"login2_1Test_normal"})
    public void logoutTest_error(int status, String entity, int code, String msg) throws Exception{
        JSONObject res = CommonOperation_yixinApp.postRequest(entity,openId,url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
 //           {1,"{\"userName\":\"13999990012\",\"phoneArea\":\"86\"}", 200, "存在的手机用户+存在的区号"},
            {2,"{\"userName\":\"13999990012\"}", 200, "存在的手机用户"},
        };
    }
    
    @Test(dataProvider = "normalData_login", dependsOnMethods = {"logoutTest_normal"})
    public void login2_1Test_normal(int status, String userName, String phoneArea, String pwd, String deviceKey, Boolean isAuto, int code, String msg) throws Exception{
        JSONObject res = CommonOperation_yixinApp.login2_1( userName, phoneArea, pwd, deviceKey, isAuto);
        Assert.assertEquals(res.getInt("code"), code, msg); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_login()
    {
        return new Object[][]
        {
            {1, "13999990012", "86", "123456", deviceKey, true, 1220005,"退出登录后，自动登录不成功"}
        };
    }
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {1,"{\"userName\":\"\"}", 1220012, "存在的邮箱用户+存在的区号"},
            {2,"{\"userName\":\"zyy.zyy@163.com\",\"phoneArea\":\"86\"}", 1220012, "不存在的邮箱用户"},
            {3,"{\"userName\":}", 1220012, "不传参数"},
        };
    }
}
