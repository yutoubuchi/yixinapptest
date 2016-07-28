package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

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

public class GetRecordListTest {
    String openId = "1234576";
    String recordsUrl = "/yiXinApp/video/records";
    private long time_now = 0;
    private long time_1h_early = 0;
    private long time_10day_early = 0;
    private long time_7day_early = 0;
    private long time_2day_later = 0;
    private long time_3day_early = 0;
    long special_time_start = 0;
    long special_time_end = 0;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        time_now = System.currentTimeMillis();
        time_1h_early = time_now - 1 * 60 * 60 * 1000L;
        time_10day_early = time_now - 10 * 24 * 60 * 60 * 1000L;
        time_7day_early = time_now - 7 * 24 * 60 * 60 * 1000L;
        time_2day_later = time_now + 2 * 24 * 60 * 60 * 1000L;
        time_3day_early = time_now - 3 * 24 * 60 * 60 * 1000L;      
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = false)
    public void getRecordListTest_normal(long from, long to, int code, String msg) throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("deviceId", Config_yixinApp.video_deviceId));
        paras.add(new Parameter("userToken", Config_yixinApp.video_userId));
        paras.add(new Parameter("from", from));
        paras.add(new Parameter("to", to));
        
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, recordsUrl, Config_yixinApp.video_deviceKey);
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
            {time_1h_early, time_now, 200, "获取1小时前录像"},
            {time_10day_early, time_now, 200, "获取10天前录像"},
            {time_3day_early, time_now, 200, "获取3天前录像"},
            {time_now, time_2day_later, 200, "获取2天后的录像"},
            {time_7day_early, time_now, 200, "获取7天前录像"},
        };
    }
    
}
