package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class CheckAppVersion {
    String url = "/yiXinApp/user/checkAppVersion";
    String openId;
    @BeforeClass
    public void beforeClass() throws Exception
    {       
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String entity, String deviceKey, int code, String msg) throws Exception{
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void do_error(String entity, String deviceKey, int code, String msg) throws Exception{
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
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
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"1\",\"os\":\"aos\"}", Config_yixinApp.deviceKey, 200, "aos版本号低于最低，升级到最高"},
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"1.1.10\",\"os\":\"aos\"}", Config_yixinApp.deviceKey, 200, "aos版本号等于最低，有最高，升级到最高"},
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"2.2.3\",\"os\":\"aos\"}", Config_yixinApp.deviceKey, 200, "aos版本号等于最高，无最高，不升级"},
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"2.3.1\",\"os\":\"aos\"}", Config_yixinApp.deviceKey, 200, "aos版本号等于中间，有最高，不升级"},

              {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"1\",\"os\":\"ios\"}", Config_yixinApp.deviceKey, 200, "ios版本号低于最低，升级到最高"},
              {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"1.1.0\",\"os\":\"ios\"}", Config_yixinApp.deviceKey, 200, "ios版本号等于最低，有最高，升级到最高"},
              {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"3.1.3\",\"os\":\"ios\"}", Config_yixinApp.deviceKey, 200, "ios版本号等于最高，无最高，不升级"},
              {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"2.0.5\",\"os\":\"ios\"}", Config_yixinApp.deviceKey, 200, "ios版本号等于中间，有最高，不升级"},
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
            {"{\"userName\":\"1350000001\",\"phoneArea\":\"\"}", Config_yixinApp.deviceKey, 1220001, "存在的手机用户+区号为空，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"861\"}", Config_yixinApp.deviceKey, 1220001, "用户名为空+不存在的区号，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"\"}",Config_yixinApp.deviceKey, 1220001, "用户名为空+区号为空，参数不合法"},
            {"{\"userName\":\"123456789\"}", Config_yixinApp.deviceKey, 1220001, "只传用户名，参数不合法"},
            {"{\"userName\":\"13500000001\",\"phoneArea\":\"86\",\"appVersion\":\"1\",\"os\":\"\"}", Config_yixinApp.deviceKey, 1220001, "os参数为空，参数不合法"},
            {"{\"userName\":\"13500000001\",\"phoneArea\":\"86\",\"appVersion\":\"\",\"os\":\"ios\"}", Config_yixinApp.deviceKey, 200, "appVersion参数为空，参数不合法"},
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"appVersion\":\"111\",\"os\":\"Android&ios\"}", Config_yixinApp.deviceKey, 200, "os参数错误"},
        };
    }
}
