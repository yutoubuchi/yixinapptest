package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_1;

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

public class VideoGo {
    String openId;
    String indexurl = "/yiXinApp/square/index";
    String url = "/yiXinApp/video/go";
    String playurl = "/yiXinApp/square/play";
    String stopurl = "/yiXinApp/square/stop";
    String deviceId;
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = getSquareIndex(2, 6, 0, Config_yixinApp.test_deviceKey);
        deviceId = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
    }
    /*
     * 获取广场首页的一个标签页下的摄像机列表
     */
    public JSONObject getSquareIndex(int tagId, int limit, int offset, String deviceKey) throws Exception{
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("id",tagId));
        paras.add(new Parameter("limit",limit));
        paras.add(new Parameter("offset",offset));
     
        JSONObject httpResponse = CommonOperation_yixinApp.getRequest(paras, openId, indexurl, deviceKey);
        return httpResponse;
    }
    
    @Test(dataProvider = "normal_data")
    public void VideoGoTest(String entity, int code, String msg) throws Exception{
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"deviceId\":\""+deviceId+"\"}", openId, url, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        Assert.assertEquals(res.getBoolean("alreadyGo"), true, msg);
        res = CommonOperation_yixinApp.postRequest(entity, openId, playurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        res = CommonOperation_yixinApp.postRequest(entity, openId, stopurl, Config_yixinApp.test_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.test_userId+"\"}",200,"播放暂停广场页公共摄像头"},    
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"86/13500000001\"}",200,"播放暂停广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\"86/13500000002\"}",200,"播放暂停广场页公共摄像头"},
            {"{\"deviceId\":\""+deviceId+"\",\"userToken\":\""+Config_yixinApp.mobile_userId+"\"}",200,"播放暂停广场页公共摄像头"},
        };
    }
}
