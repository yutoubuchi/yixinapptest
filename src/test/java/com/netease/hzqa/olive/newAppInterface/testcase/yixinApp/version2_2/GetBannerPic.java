package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONObject;

public class GetBannerPic {
    String openId;
    String url = "/yiXinApp/square/adList";
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void GetBannerPic_normal(int picType , int code, String msg) throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("picType", picType));
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
        
        httpResponse = CommonOperation_yixinApp.getRequest_notlogin(paras, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void GetBannerPic_error(int picType, int code, String msg) throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("picType", picType));
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest_notlogin(paras, openId, url, Config_yixinApp.mobile_deviceKey);
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
            {0, 200, "wap"},
            {1, 200, "web"},
            {2, 200, "wap"},
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
            {-1, 200, "wap"},
            {100, 200, "web"},
            {1000, 200, "wap"}, 
        };
    }
}
