package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.version2_2;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.Parameter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SquareChangeRelates {
    String deviceId;
    String openId;
    String lastRelates = "\"";
    String tagurl = "/yiXinApp/square/tagList";
    String indexurl = "/yiXinApp/square/index";
    String detailurl = "/yiXinApp/square/cameraDetail";
    String url = "/yiXinApp/square/changeRelates";

    @BeforeClass
    public void doBefore() throws Exception {
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
        JSONObject res = CommonOperation_yixinApp.postRequest("{\"id\":2,\"limit\":10,\"offset\":0,\"location\":\"30,120\"}", openId, indexurl, Config_yixinApp.test_deviceKey);
        deviceId = res.getJSONObject("result").getJSONArray("resultList").getJSONObject(0).getString("deviceId");
    }

    public void getLastRelates() throws Exception {
        List<Parameter> paras = new ArrayList<Parameter>();
        paras.add(new Parameter("deviceId", deviceId));
        paras.add(new Parameter("relateNum", 6));
        paras.add(new Parameter("location", "120.222798,30.212248"));

        JSONObject res0 = CommonOperation_yixinApp.getRequest(paras, openId, detailurl, Config_yixinApp.test_deviceKey);
        int count = res0.getJSONObject("result").getInt("relateCount");
        for (int i = 0; i < count; i++) {
            String id = res0.getJSONObject("result").getJSONArray("relateList").getJSONObject(i).getString("deviceId");
            lastRelates += id.toString() + ",";
        }
        lastRelates += "\"";
    }

    @Test(dataProvider = "normal_data")
    public void changeRelates(int status, int relateNum, int offset, int code, String msg) throws Exception {
        for (int i = 0; i < 5; i++, offset++) {
            List<Parameter> paras = new ArrayList<Parameter>();
            paras.add(new Parameter("deviceId", deviceId));
            paras.add(new Parameter("relateNum", relateNum));
            paras.add(new Parameter("offset", offset));
            paras.add(new Parameter("lastRelates", lastRelates));
            JSONObject res = CommonOperation_yixinApp.getRequest(paras, openId, url, Config_yixinApp.test_deviceKey);
            Assert.assertEquals(res.getInt("code"), code, msg);
            JSONArray resultList = res.getJSONArray("result");
            int count = resultList.size();
            Assert.assertEquals(count, relateNum, msg);
            lastRelates = "\"";
            for (int j = 0; j < count; j++) {
                String id = resultList.getJSONObject(j).getString("deviceId");
                lastRelates += id.toString() + ",";
            }
            lastRelates += "\"";
        }
    }
    @DataProvider
    public Object[][] normal_data(){
        return new Object[][]{
            {1, 6, 0, 200, "用例1：offset为0"},
            {2, 6, 6, 200, "用例2：offset为1"},
            {3, 6, 12, 200, "用例3：offset为2"},
            {4, 6, 18, 200, "用例4：offset为3"},
            {5, 6, 24, 200, "用例5：offset为4"},
        };
    }
}
