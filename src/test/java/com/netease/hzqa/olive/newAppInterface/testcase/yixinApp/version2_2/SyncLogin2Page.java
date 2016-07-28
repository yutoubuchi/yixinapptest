package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import java.util.ArrayList;
import java.util.List;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONObject;

public class SyncLogin2Page {
    String openId;
    String url = "/yiXinApp/system/syncLogin2Page";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void SyncLogin2Page_normal(int status, String redirectUrl, int code, String msg) throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        redirectUrl = BASE64.encode(redirectUrl);
        paras.add(new Parameter("redirectUrl", redirectUrl));
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, url, Config_yixinApp.mobile_deviceKey);
 //       Assert.assertEquals(httpResponse.getInt("code"), code, msg); 
    }
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {1,"https://x.163.com/live/wap/index.html#/m/square", 1220028, "广场首页"},
            {2,"https://x.163.com/live/square", 200, "广场首页"},
            {3,"https://x.163.com/live/pc/index.html#/m/channel?id=163281605300000", 200, "广场直播页"},
            {4,"https://x.163.com/live/channel?id=163281605300000", 1220028, "广场直播页"},
            {6,"https://x.163.com/live/pc/index.html#/m/mycamera?id=163161606902316", 403, "私人播放页"},
            {7,"https://x.163.com/live/pc/index.html#/m/mycameralist", 403, "私人播放列表"},
        };
    }
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void SyncLogin2Page_error(int status, String redirectUrl, int code, String msg) throws Exception{
        redirectUrl = BASE64.encode(redirectUrl);
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("redirectUrl", redirectUrl));
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, url, Config_yixinApp.mobile_deviceKey);
//        Assert.assertEquals(httpResponse.getInt("code"), code, msg);
    }
    
    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData()
    {
        return new Object[][]
        {
            {1,"https://x.163.com/live/pc/index.html#/m/channel?id=", 200, "空的deviceId"},
            {2,"https://x.163.com/private/list", 200, "跳转后的url"},
            {3,"", 200, "无参数"},
        };
    }
}
