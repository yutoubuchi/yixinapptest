package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetDevListTest {
    String openId = BASE64.encode("oliveApp20");
    String pwd = "Uik6n5ba4wW19FpMtLfRKmmKjBdgUo6CTQZtI9dLiPL/hoqrS+RjDo3G21wqTpSwEu13vJBZNZstLTaD0OipM/2jUDhfrJqAuNIEucEkBN8=";
    String loginToken = "d7be2113-bbad-43bf-8b7c-15580f8adbf8";
    String url = "/yiXinApp/user/devList";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
  //      openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void getDevList_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception{
        if(status == 1)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        else if(status == 2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
        
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！"); 
    }
    
    //利用openId和deviceKey判断，是否可以获取设备列表，错误用例废弃
    @Test(dataProvider = "errorData", alwaysRun = true, enabled = false)
    public void getDevList_error(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！"); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {1,"{\"userToken\":\""+Config_yixinApp.test_userId+"\"}", Config_yixinApp.test_deviceKey,200, "手机用户获取设备列表失败"},  
            {2,"{\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}", Config_yixinApp.mobile_deviceKey,200, "邮箱用户获取设备列表失败"},
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
            {"{\"userToken\":\"15906618\"}", Config_yixinApp.mobile_deviceKey, 1220012, "不存在的用户"},
            {"{\"userToken\":\"\"}", Config_yixinApp.mobile_deviceKey, 1220012, "不传用户名"},
            {"{\"userToken\":\"linhaivs\"}", Config_yixinApp.mobile_deviceKey, 1220012, "邮箱账户不传后缀"},
        };
    }
}
