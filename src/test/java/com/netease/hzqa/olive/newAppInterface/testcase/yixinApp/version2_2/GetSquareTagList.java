package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSquareTagList {
    String openId;
    String url = "/yiXinApp/square/tagList";
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void getTagList_normal(int status, String deviceKey, int code, String msg) throws Exception{
        if(status == 1)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        else if(status == 2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user, Config_yixinApp.mobile_area, Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);
        
        JSONObject httpResponse;
        if(status ==3){
            httpResponse = CommonOperation_yixinApp.getRequest(null, "", url, "");
        }
        else
            httpResponse = CommonOperation_yixinApp.getRequest(null, openId, url, deviceKey);
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
            {1, Config_yixinApp.test_deviceKey,200, "正常获取"},
            {1, Config_yixinApp.mobile_deviceKey,200, "登录失效,未登录也可以获取"},
            {1, Config_yixinApp.test_deviceKey,200, "失效后正常获取"},
            {2, Config_yixinApp.mobile_deviceKey,200, "用户2获取"},
            {3, "", 200, "用户不登录"}
        };
    }
}
