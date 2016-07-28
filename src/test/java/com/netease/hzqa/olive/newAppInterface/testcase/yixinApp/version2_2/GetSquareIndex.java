package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSquareIndex {
    String openId;
    String url = "/yiXinApp/square/index";
    
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void getSquareIndex_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception{
//        if(status == 1)
//            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
//        else if(status == 2)
//            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
        
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {1, "{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "limit:10,offset:0"},
            {1, "{\"id\":2,\"limit\":10,\"offset\":10,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "limit:10,offset:10"},
            {1, "{\"id\":2,\"limit\":10,\"offset\":87,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "limit:10,offset:20"},
            {1, "{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"\"}",Config_yixinApp.mobile_deviceKey,200, "不传location"},
            {1, "{\"id\":3,\"limit\":100,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "limit:100,offset:0"},
            {2, "{\"id\":1,\"limit\":15,\"offset\":5,\"location\":\"30,120\"}",Config_yixinApp.mobile_deviceKey,200, "limit:15,offset:5"},
        };
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void getSquareIndex_error(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
                  
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {"{\"id\":1,\"limit\":,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,400, "limit:null,offset:0"},
            {"{\"id\":1,\"limit\":10,\"offset\":,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,400, "limit:10,offset:null"},
            {"{\"id\":,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,400, "tagId:null,limit:10,offset:20"},
            {"{\"id\":12345,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "tagId不存在，limit:100,offset:0"},
            {"{\"id\":3902848,\"limit\":0,\"offset\":0,\"location\":\"30,120\"}",Config_yixinApp.test_deviceKey,200, "limit:0,offset:0"},
        };
    }
}
