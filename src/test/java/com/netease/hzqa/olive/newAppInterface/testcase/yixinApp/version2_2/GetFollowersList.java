package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetFollowersList {
    String openId;
    String url = "/yiXinApp/user/followersList";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void getFollowersList_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception{
        if(status == 1)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, deviceKey);
        else if(status == 2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, deviceKey);

        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }

    @Test(dataProvider = "errorData", alwaysRun = true)
    public void getFollowersList_error(String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, deviceKey);
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
            {1,"{\"limit\":5,\"offset\":0}", Config_yixinApp.test_deviceKey,200, "第一页"},
            {1,"{\"limit\":5,\"offset\":5}", Config_yixinApp.test_deviceKey,200, "第二页"},
            {1,"{\"limit\":10,\"offset\":0}", Config_yixinApp.test_deviceKey,200, "第一页"},
            {1,"{\"limit\":10,\"offset\":10}", Config_yixinApp.test_deviceKey,200, "第二页"},
            {1,"{\"limit\":10,\"offset\":20}", Config_yixinApp.test_deviceKey,200, "第三页"},
            {2,"{\"limit\":100,\"offset\":0}", Config_yixinApp.video_deviceKey,200, "第一页，limit：100"},
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
            {"{\"limit\":,\"offset\":0}", Config_yixinApp.test_deviceKey,400, "limit空"},
            {"{\"limit\":10,\"offset\":}", Config_yixinApp.test_deviceKey,400, "offset空"},
            {"{\"offset\":10}", Config_yixinApp.test_deviceKey,200, "不传limit"},
            {"{\"limit\":10}", Config_yixinApp.test_deviceKey,200, "不传offset"},
            {"{}", Config_yixinApp.test_deviceKey,200, "不传offset和limit"},
        };
    }
}
