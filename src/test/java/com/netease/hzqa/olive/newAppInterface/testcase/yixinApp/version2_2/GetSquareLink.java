package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import java.util.ArrayList;
import java.util.List;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONObject;

public class GetSquareLink {
    String url = "/yiXinApp/system/getSquareLink";
    String switch_url="/private/closeShareSwitch";
    String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test
    public void GetSquareLinkTest() throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("openOrClose", "on"));
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, switch_url, Config_yixinApp.mobile_deviceKey);
        httpResponse = CommonOperation_yixinApp.getRequest(null, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), 200, "获取链接地址"); 
        Assert.assertEquals(httpResponse.getJSONObject("result").getString("isCanShare"), "0", "开关打开"); 
        
        List<Parameter> paras1 = new ArrayList<Parameter>();
        paras1.add(new Parameter("openOrClose", "off"));
        CommonOperation_yixinApp.getRequest(paras1, openId, switch_url, Config_yixinApp.mobile_deviceKey);
        httpResponse = CommonOperation_yixinApp.getRequest_notlogin(null, openId, url, Config_yixinApp.mobile_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), 200, "获取链接地址"); 
        Assert.assertEquals(httpResponse.getJSONObject("result").getString("isCanShare"), "1", "开关关闭"); 
    }
    
}
