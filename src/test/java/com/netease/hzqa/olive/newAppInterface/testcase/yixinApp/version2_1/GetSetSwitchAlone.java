package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_1;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class GetSetSwitchAlone {
    String openId;
    String geturl = "/yiXinApp/device/getSwitch";
    String seturl = "/yiXinApp/device/setSwitch";
    String deviceId = Config_yixinApp.video_deviceId;
    
    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.video_user, Config_yixinApp.video_area, Config_yixinApp.video_pwd, Config_yixinApp.video_deviceKey);
    }
    
    @Test(dataProvider = "normal_data")
    public void GetSetSwitchTest(int status, String entity_set, String entity_get, int switchValue,  int code, String msg) throws Exception{
        JSONObject res = CommonOperation_yixinApp.postRequest(entity_set, openId, seturl, Config_yixinApp.video_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        Thread.sleep(3000);
        res = CommonOperation_yixinApp.postRequest(entity_get, openId, geturl, Config_yixinApp.video_deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        if(status == 3)//只有云录像接口生效
            Assert.assertEquals(res.getInt("switchValue"), switchValue, msg);
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {1,"{\"deviceId\":\""+deviceId+"\",\"switchType\":1,\"switchValue\":0}", "{\"deviceId\":\""+deviceId+"\",\"switchType\":1}",0,200,"关闭，获取设备开关"},    
            {1,"{\"deviceId\":\""+deviceId+"\",\"switchType\":1,\"switchValue\":1}", "{\"deviceId\":\""+deviceId+"\",\"switchType\":1}",1,200,"打开，获取设备开关"},
            
            {2,"{\"deviceId\":\""+deviceId+"\",\"switchType\":2,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":2}",0,200,"关闭，获取告警开关"},
            {2,"{\"deviceId\":\""+deviceId+"\",\"switchType\":2,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":2}",1,200,"打开，获取告警开关"},

            {3,"{\"deviceId\":\""+deviceId+"\",\"switchType\":3,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":3}",0,200,"关闭，获取云录像开关"},
            {3,"{\"deviceId\":\""+deviceId+"\",\"switchType\":3,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":3}",1,200,"打开，获取云录像开关"},

            {4,"{\"deviceId\":\""+deviceId+"\",\"switchType\":4,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":4}",0,200,"关闭，获取状态灯开关"},
            {4,"{\"deviceId\":\""+deviceId+"\",\"switchType\":4,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":4}",1,200,"打开，获取状态灯开关"},

            {5,"{\"deviceId\":\""+deviceId+"\",\"switchType\":5,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":5}",0,200,"关闭，获取音频开关"},
            {5,"{\"deviceId\":\""+deviceId+"\",\"switchType\":5,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":5}",1,200,"打开，获取音频开关"},

            {6,"{\"deviceId\":\""+deviceId+"\",\"switchType\":6,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":6}",0,200,"关闭，获取图像旋转开关"},
            {6,"{\"deviceId\":\""+deviceId+"\",\"switchType\":6,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":6}",1,200,"打开，获取图像旋转开关"},

            {7,"{\"deviceId\":\""+deviceId+"\",\"switchType\":7,\"switchValue\":0}","{\"deviceId\":\""+deviceId+"\",\"switchType\":7}",0,200,"关闭，获取夜视开关"},
            {7,"{\"deviceId\":\""+deviceId+"\",\"switchType\":7,\"switchValue\":1}","{\"deviceId\":\""+deviceId+"\",\"switchType\":7}",1,200,"自动切换，获取夜视开关"},
            {7,"{\"deviceId\":\""+deviceId+"\",\"switchType\":7,\"switchValue\":2}","{\"deviceId\":\""+deviceId+"\",\"switchType\":7}",2,200,"打开，获取夜视开关"},

        };
    }
}
