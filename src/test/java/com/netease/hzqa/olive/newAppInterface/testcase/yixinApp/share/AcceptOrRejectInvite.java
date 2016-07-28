package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class AcceptOrRejectInvite {
    String url = "/yiXinApp/share/acceptOrReject";
    String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    { }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String userName, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception {
        //获取用户的openId
        String openId = CommonOperation_yixinApp.getOpenId2_1(userName, phoneArea, pwd, deviceKey);
        
        String entity = "{\"userName\":\""+userName+"\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":true}";
        if(status == 2 || status == 4 || status == 5){
            entity = "{\"userName\":\""+userName+"\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\",\"acceptOrReject\":false}";
        }
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = true)
    public void do_error(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
            {1, Config_yixinApp.mobile_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 1220023, "已经是分享者，接受邀请"},
            {2, Config_yixinApp.email_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 1220023, "已经是分享者，拒绝邀请"},
            {3, Config_yixinApp.new_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 200, "非分享者，接受邀请"},
            {4, Config_yixinApp.yixin_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 200, "非分享者，拒绝邀请"},
            {5, Config_yixinApp.new_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 1220023, "接受邀请后再次拒绝邀请"},
            {6, Config_yixinApp.yixin_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 1220022, "拒绝邀请后再次接受邀请"},
        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                 {"{\"userName\":\"86/1190000001\",\"phoneArea\":\"86\",\"deviceId\":\"163021505010522\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "存在的手机用户+区号为空，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"861\",\"deviceId\":\"163021505010522\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "用户名为空+不存在的区号，参数不合法"},
                 {"{\"userName\":\"\",\"phoneArea\":\"\",\"deviceId\":\"163021505010522\"}", 1220001, openId,Config_yixinApp.test_deviceKey, "用户名为空+区号为空，参数不合法"},
                 {"{\"userName\":\"123456789\",\"deviceId\":\"\"}", 1220001,openId,Config_yixinApp.test_deviceKey, "只传用户名，参数不合法"},
                 {"{\"userName\":\"11900000001\",\"phoneArea\":\"86\"}",1220001, openId,Config_yixinApp.test_deviceKey, "os参数为空，参数不合法"},
        };
    }
}
