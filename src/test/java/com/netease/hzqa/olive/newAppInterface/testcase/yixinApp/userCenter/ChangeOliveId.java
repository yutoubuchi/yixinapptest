package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class ChangeOliveId {
    String url = "/yiXinApp/user/changeOliveId";
    String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String entity, int code, String openId, String deviceKey, String msg) throws Exception{
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void do_error(String entity, int code,  String openId, String deviceKey, String msg) throws Exception{
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
            {"{\"userName\":\""+Config_yixinApp.mobile_user+"\",\"phoneArea\":\"86\",\"oliveId\":\""+Config_yixinApp.test_user+"\"}", 200, openId, Config_yixinApp.test_deviceKey, "id改成和他的手机相同"},
            {"{\"userName\":\""+Config_yixinApp.test_user+"\",\"phoneArea\":\"86\",\"oliveId\":\"zyy123456\"}", 200, openId, Config_yixinApp.test_deviceKey, "存在的oliveId号"},
//            //邮箱用户，不判断区号
            {"{\"userName\":\"13999990003\",\"phoneArea\":\"\",\"oliveId\":\"zyy1234567\"}", 200, openId, Config_yixinApp.test_deviceKey, "存在的邮箱用户"},
            {"{\"userName\":\"13999999920\",\"oliveId\":\"zyy5306\"}", 200, openId, Config_yixinApp.test_deviceKey, "不传区号,默认86,存在的oliveId号"},
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
            {"{\"userName\":\"1990000001\",\"phoneArea\":\"\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "存在的手机用户+区号为空，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"861\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "用户名为空+不存在的区号，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "用户名为空+区号为空，参数不合法"},
            {"{\"userName\":\"123456789\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "只传用户名，参数不合法"},
            {"{\"userName\":\"19900000001\",\"phoneArea\":\"86\",\"oliveId\":\"\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "os参数为空，参数不合法"},
        };
    }
}
