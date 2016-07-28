package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.share;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

public class SearchShareUser {
    String url = "/yiXinApp/share/searchYixinUser";
    String openId = "";

    @BeforeClass
    public void beforeClass() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, "86", Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        // openId =
        // CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.mobile_user,
        // "86", Config_yixinApp.mobile_pwd, Config_yixinApp.mobile_deviceKey);

    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void do_normal(int status, String entity, int code, String openId, String deviceKey, String msg) throws Exception {

        JSONObject res = CommonOperation_yixinApp.postRequest(entity, openId, url, deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
        int size = res.getJSONArray("result").size();
        if (status <= 3) {// 搜索结果只有一条的情况
            Assert.assertEquals(size, 1, msg);
        }
        else if (status <= 9) {// 搜索结果为空的情况
            Assert.assertEquals(size, 0, msg);
        }
        else {// 搜索结果大于1的情况
            Assert.assertEquals(size, 1, msg);
        }
    }

    @Test(dataProvider = "errorData", alwaysRun = true, enabled = true)
    public void do_error(int status, String entity, int code, String openId, String deviceKey, String msg) throws Exception {
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
                // 以下关键字用户，存在，搜索列表只能搜到一条
                { 1, "{\"userName\":\"" + Config_yixinApp.test_user
                        + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"+355-13999990012\"}", 200, openId,
                        Config_yixinApp.test_deviceKey, "非86区号易信号" },
                { 2, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"8268020\"}", 200,
                        openId, Config_yixinApp.test_deviceKey, "搜索oliveId" },
                { 3, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"13500000002\"}",
                        200, openId, Config_yixinApp.test_deviceKey, "用户的手机号与yixinId号一样" },
                { 0, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"13900009999\"}", 200,
                            openId, Config_yixinApp.test_deviceKey, "易信注册且未登录app的用户" },
                // 以下关键字用户，不存在，搜索列表应该为空
                { 4, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"4916013\"}", 200,
                        openId, Config_yixinApp.test_deviceKey, "搜索自己的yixinId" },
                { 5, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"zyy1234555678\"}",
                        200, openId, Config_yixinApp.test_deviceKey, "搜索不存在的keyword" },
                { 6, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"oliveApp192\"}",
                        200, openId, Config_yixinApp.test_deviceKey, "独立APP的keyword" },
                { 7, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"4921001\"}", 200,
                        openId, Config_yixinApp.test_deviceKey, "更改前的yixinId" },
                { 8, "{\"userName\":\"" + Config_yixinApp.test_user
                        + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"zhengyinyan21@163.com\"}", 200, openId,
                        Config_yixinApp.test_deviceKey, "搜索邮箱用户" },
                { 9, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"8420009\"}", 200,
                            openId, Config_yixinApp.test_deviceKey, "搜索未在app登录的易信id" },
                // 以下多用户，搜索结果大于1
                { 10, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"13500000003\"}",
                        200, openId, Config_yixinApp.test_deviceKey, "搜索手机号" },
            };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { 1, "{\"userName\":\"" + Config_yixinApp.mobile_user
                        + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"+355-13999990012\"}", 200, openId,
                Config_yixinApp.test_deviceKey, "非主人搜索" },
                { 2, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"\"}", 1220001,
                        openId, Config_yixinApp.test_deviceKey, "搜索关键字为空" },
                { 3, "{\"userName\":\"" + Config_yixinApp.test_user + "\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"13500000002\"}",
                        1220020, openId, Config_yixinApp.mobile_deviceKey, "在别的设备搜索" },
                { 4, "{\"userName\":\"" + Config_yixinApp.mobile_user
                            + "\",\"phoneArea\":\"86\",\"deviceId\":\"\",\"keyword\":\"+355-13999990012\"}", 1220001, openId,
                    Config_yixinApp.test_deviceKey, "设备号为空搜索" },
                { 5, "{\"userName\":\"\",\"phoneArea\":\"86\",\"deviceId\":\"163021515123516\",\"keyword\":\"+355-13999990012\"}", 200, openId,
                Config_yixinApp.test_deviceKey, "主人为空搜索" },};
                        
    }
}
