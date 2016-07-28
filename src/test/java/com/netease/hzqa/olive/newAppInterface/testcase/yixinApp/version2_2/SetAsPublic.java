package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SetAsPublic {
    String openId;
    String url = "/yiXinApp/camera/setAsPublic";
    String urlPrivate = "/yiXinApp/camera/setAsPrivate";
    String playurl = "/yiXinApp/square/play";
    String stopurl = "/yiXinApp/square/stop";
    String deviceId = Config_yixinApp.video_deviceId;
    String deviceId1;
    
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
    }
 
    //设为公共摄像头，普通设置
    @Test(dataProvider = "public_data")
    public void setAsPublicTest(int status, String entity, String deviceKey, int code, String msg) throws Exception {
        if (status == 2)
            openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd,
                    Config_yixinApp.video_deviceKey);
        //设为公共摄像头
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        //获取广场页最新tab第一个摄像机
        res = CommonOperation_yixinApp.postRequest("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, "/yiXinApp/square/index", Config_yixinApp.test_deviceKey);
        deviceId1 = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
        Assert.assertEquals(deviceId, deviceId1, msg);
        if (status == 1) {
            // 公共播放
            JSONObject httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, playurl, deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), 200, "播放公共摄像头失败！");
            // 暂停播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, stopurl, deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), 200, "暂停播放公共摄像头失败！");
            
         // 私人播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, "/yiXinApp/private/play", deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), code, "播放私人摄像头失败！");
            // 暂停播放
            httpResponse = CommonOperation_yixinApp.postRequest(entity, openId, "/yiXinApp/private/stop", deviceKey);
            Assert.assertEquals(httpResponse.getInt("code"), code, "暂停播放私人摄像头失败！");
        }
    }

    @DataProvider
    public Object[][] public_data() {
        return new Object[][] {
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\"}", Config_yixinApp.video_deviceKey, 200,
                        "主人设置公共摄像头" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13067700006\"}", Config_yixinApp.video_deviceKey, 200, "主人设置公共摄像头，多次设置" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"\"}", Config_yixinApp.video_deviceKey, 200, "主人设置公共摄像头，多次设置" },
                { 3, "{\"deviceId\":\"\",\"userToken\":\"\"}", Config_yixinApp.video_deviceKey, 404, "主人设置公共摄像头，摄像机不存在" },
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\"}", Config_yixinApp.test_deviceKey, 1220020,
                        "主人设置公共摄像头" },
                // 非主人操作
                { 2, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.test_userId + "\"}", Config_yixinApp.video_deviceKey, 403,
                        "非主人设置公共摄像头,openid不符" },
                { 2, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13500000001\",\"focusStatus\":0}", Config_yixinApp.video_deviceKey, 403,
                        "非主人设置公共摄像头，openid不符" }, };
    }
    
  //设为公共摄像头，传验证，简介等其他参数
    @Test(dataProvider = "public_paras", dependsOnMethods = "setAsPublicTest")
    public void setAsPublicParas(int status, String entity, String deviceKey, int code, String msg) throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
      //设为公共摄像头
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        //获取最新tab的第一台摄像机
        res = CommonOperation_yixinApp.postRequest("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, "/yiXinApp/square/index", Config_yixinApp.test_deviceKey);
        deviceId1 = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
 //       if(status <= 3){//开放到广场            
            Assert.assertEquals(deviceId, deviceId1, "开放到广场页");          
//        }
//        else{
//            Assert.assertNotEquals(deviceId, deviceId1, "不开放到广场页");
//        }
        
        // 公共播放
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, playurl, deviceKey);
 //       if(status <= 3){//观看无需验证            
            Assert.assertEquals(httpResponse.getInt("code"), 200, "播放公共摄像头失败！");        
//        }
//        else{
//            Assert.assertEquals(httpResponse.getInt("code"), 1220031, "播放公共摄像头失败，需要验证！"); 
//        }
//        
        // 暂停播放
        httpResponse = CommonOperation_yixinApp.postRequest_notlogin(entity, openId, stopurl, deviceKey);
//        if(status <= 3){//观看无需验证            
            Assert.assertEquals(httpResponse.getInt("code"), 200, "暂停播放公共摄像头失败！");        
//        }
//        else{
//            Assert.assertEquals(httpResponse.getInt("code"), 1220031, "暂停播放公共摄像头失败，需要验证！"); 
//        }       
    }
    @DataProvider
    public Object[][] public_paras() {
        return new Object[][] {
                { 1, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\",\"abstractInfo\":\"调用接口修改摄像机简介，这是公共摄像头简介\"}", Config_yixinApp.video_deviceKey, 200,
                        "主人设置公共摄像头" },
                { 2, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\",\"canView\":1}", Config_yixinApp.test_deviceKey, 1220020,
                        "主人设置公共摄像头,开放到广场" }, 
                { 3, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\",\"canView\":1,\"needVerify\":1}", Config_yixinApp.test_deviceKey, 1220020,
                "主人设置公共摄像头,开放到广场，观看需验证" }, 
                { 4, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"\",\"canView\":0}", Config_yixinApp.video_deviceKey, 200, "主人设置公共摄像头，不开放到广场" },
                { 5, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"" + Config_yixinApp.video_userId + "\",\"canView\":0,\"needVerify\":1}", Config_yixinApp.test_deviceKey, 1220020,
                "主人设置公共摄像头,不开放到广场，观看需验证" }, 
                
                { 6, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13067700006\",\"recoverFans\":true}", Config_yixinApp.video_deviceKey, 200, "主人设置公共摄像头，恢复粉丝" },
                { 7, "{\"deviceId\":\"" + deviceId + "\",\"userToken\":\"86/13067700006\",\"recoverFans\":false}", Config_yixinApp.video_deviceKey, 200, "主人设置公共摄像头，不恢复粉丝" },
                       
        };
    }
}
