package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;
/**
 * 主人删除分享
 * @Description 
 * @author hzzhengyinyan
 * @date 2016年3月24日 下午4:03:14
 */
public class InviteCancel {
    String url = "/yiXinApp/share/inviteCancel";
    String inviteurl = "/yiXinApp/share/yiXinShareInvite";
    String openId;

    @BeforeClass
    public void beforeClass() throws Exception
    {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = false)
    public void do_error(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, inviteurl, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    
    @Test(dataProvider = "invite9Data", alwaysRun = true)
    public void do_invite9(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, inviteurl, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    
    @Test(dataProvider = "over9_Data", alwaysRun = true, dependsOnMethods = "do_invite9")
    public void do_cancel9(int status, String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        if(status >=9 ){
            System.out.println(res);
        }
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] invite9Data() {
        return new Object[][] { 
            { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"" + Config_yixinApp.test_deviceId2
                + "\",\"shareList\":[\"8268053\",\"8268054\",\"8268055\",\"8268056\",\"8268057\",\"8268058\",\"8268059\",\"8268060\"]}", 200, openId, Config_yixinApp.test_deviceKey, "主人邀请分享者" }, 
        };
    } 
    /**
     * 正常测试数据,邀请人数大于9个
     */
    @DataProvider
    public Object[][] over9_Data() {
        return new Object[][] {
            {1, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268053\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户1" },
            {2, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268054\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户2" },
            {3, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268055\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户3" },
            {4, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268056\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户4" },
            {5, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268057\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户5" },
            {6, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268058\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户6" },
            {7, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268059\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户7" },
            {8, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123518\",\"inviteUid\":\"8268060\"}", 200, openId, Config_yixinApp.test_deviceKey, "邀请用户8" },
       };
    }
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021505010522\",\"inviteUid\":\""+Config_yixinApp.new_userId+"\"}", 1220027, Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, "主人（手机）邀请邮箱用户" },
                { "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021505010522\",\"inviteUid\":\""+Config_yixinApp.yixin_userId+"\"}", 1220027, Config_yixinApp.test_openId, Config_yixinApp.test_deviceKey, "主人（手机）邀请手机用户" },

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {

        };
    }
}
