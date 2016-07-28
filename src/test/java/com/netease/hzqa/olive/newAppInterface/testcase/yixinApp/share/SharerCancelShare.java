package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;
/**
 * 分享者主动删除分享
 * @Description 
 * @author hzzhengyinyan
 * @date 2016年3月24日 下午4:06:05
 */
public class SharerCancelShare {
    String url = "/yiXinApp/share/cancelShare";
  //  String openId;
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
//        openId = CommonOperation_yixinApp.getOpenId2_1(Config.mobile_user, Config.mobile_area, Config.mobile_pwd, Config.mobile_deviceKey);   
    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String userName, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception {
        //
        String entity = "{\"userName\":\""+userName+"\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\"}";
      //获取用户的openId
        String openId = CommonOperation_yixinApp.getOpenId2_1(userName, phoneArea, pwd, deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = false)
    public void do_error(String entity, int code, String openId, String deviceKey, String msg) throws Exception {
        //
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] {
            {1, Config_yixinApp.mobile_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 200, "分享者删除分享"},
            {2, Config_yixinApp.email_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 200, "共享者删除分享"},
            {3, Config_yixinApp.new_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 200, "共享者删除分享"},
        };
    }

    /**
     * 二次取消分享，即非分享者退出分享
      * @Title: do_normal_again
      * @Description: TODO
    	* @Author: hzzhengyinyan
      * @param status
      * @param userName
      * @param phoneArea
      * @param pwd
      * @param deviceId
      * @param deviceKey
      * @param code
      * @param msg
      * @throws Exception
     */
    @Test(dataProvider = "normalData_doagain", alwaysRun = true, dependsOnMethods="do_normal")
    public void do_normal_again(int status, String userName, String phoneArea, String pwd, String deviceId, String deviceKey, int code, String msg) throws Exception {
        //
        String entity = "{\"userName\":\""+userName+"\",\"phoneArea\":\""+phoneArea+"\",\"deviceId\":\""+deviceId+"\"}";
      //获取用户的openId
        String openId = CommonOperation_yixinApp.getOpenId2_1(userName, phoneArea, pwd, deviceKey);
        
        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData_doagain() {
        return new Object[][] {
            {1, Config_yixinApp.mobile_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 405, "已删除后，再次删除，无权限"},
            {2, Config_yixinApp.email_user,"86","123456",Config_yixinApp.test_deviceId, Config_yixinApp.test_deviceKey, 405, "共享者删除分享,无权限"},
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
