package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class OwnerAcceptOrReject {
    String url = "/yiXinApp/share/ownerAcceptOrReject";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception
    {
//        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String user, String phoneArea, String pwd, String deviceId, String inviteUserId, String deviceKey, int code, String msg) throws Exception {
        //获取用户openId；
        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        String entity;
        if(status <= 2){//接受查看请求
            entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":true,\"inviteUid\":\""+inviteUserId+"\"}";
        }
        else{//拒绝查看请求
            entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":false,\"inviteUid\":\""+inviteUserId+"\"}";    
        }
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    
    @Test(dataProvider = "normalData_agian", alwaysRun = true, dependsOnMethods="do_normal")
    public void do_normal_again(int status, String user, String phoneArea, String pwd, String deviceId, String inviteUserId, String deviceKey, int code, String msg) throws Exception {
        //获取用户openId；
        String openId = CommonOperation_yixinApp.getOpenId2_1(user, phoneArea, pwd, deviceKey);
        String entity;
        if(status <= 2){//接受查看请求
            entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":true,\"inviteUid\":\""+inviteUserId+"\"}";
        }
        else{//拒绝查看请求
            entity = "{\"userName\":\"" + user + "\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":false,\"inviteUid\":\""+inviteUserId+"\"}";    
        }
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true, dependsOnMethods="do_normal_again")
    public void do_error(String entity, String deviceKey, int code, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey); 
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据,主人再次处理接受或拒绝数据
     */
    @DataProvider
    public Object[][] normalData_agian() {
        return new Object[][] {
                {1, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.mobile_userId, Config_yixinApp.test_deviceKey, 1220023, "已经是分享者" },
                {2, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.email_userId, Config_yixinApp.test_deviceKey, 1220023, "已经是分享者" },
                {3, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.new_userId, Config_yixinApp.test_deviceKey, 1220022, "未请求观看" },
                {4, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.yixin_userId, Config_yixinApp.test_deviceKey, 1220022, "未请求观看" },

        };
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                {1, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.mobile_userId, Config_yixinApp.test_deviceKey, 200, "主人接受查看自己设备" },
                {2, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.email_userId, Config_yixinApp.test_deviceKey, 200, "主人接受查看自己设备" },
                {3, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.new_userId, Config_yixinApp.test_deviceKey, 200, "主人拒绝查看自己设备" },
                {4, Config_yixinApp.test_user, Config_yixinApp.test_area,Config_yixinApp.test_pwd, Config_yixinApp.test_deviceId, Config_yixinApp.yixin_userId, Config_yixinApp.test_deviceKey, 200, "主人拒绝查看自己设备" },

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
            {"{\"userName\":\"\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\""+Config_yixinApp.test_deviceId+"\",\"acceptOrReject\":false,\"inviteUid\":\"\"}", Config_yixinApp.test_deviceKey, 1220001, "用户名为空"},
            {"{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\"\",\"inviteUid\":\""+Config_yixinApp.email_userId+"\"}", Config_yixinApp.test_deviceKey, 1220001, "deviceId为空"},
            {"{\"userName\":\"\",\"phoneArea\":\""+Config_yixinApp.test_area+"\",\"deviceId\":\"\",\"acceptOrReject\":false,\"inviteUid\":\"\"}", Config_yixinApp.test_deviceKey, 1220001, "用户名和deviceId为空"},
        };
    }
}
