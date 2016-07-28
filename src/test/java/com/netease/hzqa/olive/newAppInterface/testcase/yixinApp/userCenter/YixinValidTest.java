package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class YixinValidTest {
String url = "/yiXinApp/user/yiXinValid";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void checkUserNameIsExist_normal(String entity, String deviceKey, boolean result, String msg) throws Exception{
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity,"",url, deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, msg); 
        Assert.assertEquals(res.getBoolean("result"), result, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void checkUserNameIsExist_error(String entity, String deviceKey, int code, String msg) throws Exception{
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity,"",url, deviceKey);
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
            {"{\"userName\":\"13500000001\",\"phoneArea\":\"86\"}",Config_yixinApp.test_deviceKey, false, "存在的手机用户+存在的区号"},
            {"{\"userName\":\"13999990012\",\"phoneArea\":\"60\"}",Config_yixinApp.test_deviceKey, false, "存在的手机用户+不存在的区号"},
            {"{\"userName\":\"13999999030\",\"phoneArea\":\"355\"}",Config_yixinApp.test_deviceKey, true, "存在的手机用户+非86区号"},
            {"{\"userName\":\"13999999079\",\"phoneArea\":\"86\"}",Config_yixinApp.test_deviceKey, true, "存在的独立APP用户"},
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
            {"{\"userName\":\"1990000001\",\"phoneArea\":\"\"}",Config_yixinApp.test_deviceKey, 1220001, "存在的手机用户+区号为空，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"861\"}",Config_yixinApp.test_deviceKey, 1220001, "用户名为空+不存在的区号，参数不合法"},
            {"{\"userName\":\"\",\"phoneArea\":\"\"}",Config_yixinApp.test_deviceKey, 1220001, "用户名为空+区号为空，参数不合法"},
            {"{\"userName\":\"123456789\"}",Config_yixinApp.test_deviceKey, 1220001, "只传用户名，参数不合法"},
        };
    }

}
