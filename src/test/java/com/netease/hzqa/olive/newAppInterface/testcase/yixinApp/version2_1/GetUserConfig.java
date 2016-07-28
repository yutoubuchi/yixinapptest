package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_1;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetUserConfig {
    String openId;
    String url = "/yiXinApp/user/getUserConfig";
    
    @BeforeClass
    public void doBefore() throws Exception {
            }
    
    @Test
    public void GetUserConfigTest() throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.dianxin_user, Config_yixinApp.dianxin_area, Config_yixinApp.dianxin_pwd, Config_yixinApp.dianxin_deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest("{}", openId, url, Config_yixinApp.dianxin_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "获取用户设置");
        Assert.assertEquals(res.getJSONObject("result").getInt("isDataFree"), 1, "已开启电信免流量");
        
        
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        res = CommonOperation_yixinApp.postRequest("{}", openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "获取用户设置");
        Assert.assertEquals(res.getJSONObject("result").getInt("isDataFree"), 0, "未开启电信免流量");
        
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.hz_user, Config_yixinApp.hz_area, Config_yixinApp.hz_pwd, Config_yixinApp.hz_deviceKey);
        res = CommonOperation_yixinApp.postRequest("{}", openId, url, Config_yixinApp.hz_deviceKey);
        Assert.assertEquals(res.getInt("code"), 200, "获取用户设置");
        Assert.assertEquals(res.getJSONObject("result").getInt("isDataFree"), 0, "已开启电信免流量");
    }
}
