package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.commontemplate.util.BASE64;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class DeleteRecords {
    private long time_now = 0;
    private long time_1h_early = 0;
    private long time_10day_early = 0;
    private long time_9day_early = 0;
    private long time_2day_later = 0;
    private long time_3day_later = 0;
    long special_time_start = 0;
    long special_time_end = 0;
    String url = "/yiXinApp/video/deleteRecords";
    String deviceId = Config_yixinApp.test_deviceId;
    String userToken = Config_yixinApp.test_userId;
    String openId = BASE64.encode("oliveApp20");
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
        time_now = System.currentTimeMillis();
        time_1h_early = time_now - 1 * 60 * 60 * 1000L;
        time_10day_early = time_now - 10 * 24 * 60 * 60 * 1000L;
        time_9day_early = time_now - 9 * 24 * 60 * 60 * 1000L;
        time_2day_later = time_now + 2 * 24 * 60 * 60 * 1000L;
        time_3day_later = time_now + 3 * 24 * 60 * 60 * 1000L;
        special_time_start = CommonOperation_yixinApp.getTimeStemp("2016-05-21 14:47:16");
        special_time_end = CommonOperation_yixinApp.getTimeStemp("2016-05-21 14:48:24");
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void test_normal(int status, String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
    }
    
    @Test(dataProvider = "errorData", alwaysRun = true)
    public void test_error(int status, String entity, String deviceKey, int code, String msg) throws Exception{
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
            {1,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_10day_early+"\",\"endDate\":\""+time_9day_early+"\"}", Config_yixinApp.test_deviceKey, 200, "10天前--9天前"},
            {2,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_3day_later+"\",\"endDate\":\""+time_2day_later+"\"}", Config_yixinApp.test_deviceKey, 200, "3天后--2天后"},
            {3,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_9day_early+"\",\"endDate\":\""+time_10day_early+"\"}",Config_yixinApp.test_deviceKey,  200, "9天前--10天前"},
            {4,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_2day_later+"\",\"endDate\":\""+time_3day_later+"\"}", Config_yixinApp.test_deviceKey, 200, "2天后--3天后"},
            {5,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+special_time_start+"\",\"endDate\":\""+special_time_end+"\"}", Config_yixinApp.test_deviceKey, 200, "指定的时间区间"},
            {6,"{\"deviceId\":\"163291505010450\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_1h_early+"\",\"endDate\":\""+time_now+"\"}", Config_yixinApp.test_deviceKey, 404, "从现在开始的一小时内"},
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
            {1,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"\",\"startDate\":\""+time_10day_early+"\",\"endDate\":\""+time_9day_early+"\"}", Config_yixinApp.test_deviceKey, 200, "userToken为空，10天前--9天前"},
            {2,"{\"deviceId\":\"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_3day_later+"\",\"endDate\":\""+time_2day_later+"\"}", Config_yixinApp.test_deviceKey, 404, "deviceId为空，3天后--2天后"},
            {3,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\"\",\"endDate\":\""+time_10day_early+"\"}", Config_yixinApp.test_deviceKey, 200, "startTime为空"},
            {4,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\""+time_2day_later+"\",\"endDate\":\"\"}", Config_yixinApp.test_deviceKey, 200, "endDate为空"},
            {5,"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+userToken+"\",\"startDate\":\"\",\"endDate\":\"\"}", Config_yixinApp.test_deviceKey, 200, "startTime为空，endDate为空"},
        };
    }
    
    /**
     * 测试环境真实存在设备，执行录像删除操作
     */
    @Test(dataProvider = "deleteData", alwaysRun = true)
    public void do_delete(int status, String entity, String deviceKey, int code, String msg) throws Exception{
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "检查是否已添加失败！");
    }
    /**
     * 真实摄像头测试数据
     */
    @DataProvider
    public Object[][] deleteData()
    {
        return new Object[][]
        {
            {1,"{\"deviceId\":\""+Config_yixinApp.video_deviceId+"\",\"userToken\":\""+Config_yixinApp.video_userId+"\",\"startDate\":\""+time_1h_early+"\",\"endDate\":\""+time_now+"\"}", Config_yixinApp.video_deviceKey, 200, "从现在开始的一小时内"},
        };
    }
}
