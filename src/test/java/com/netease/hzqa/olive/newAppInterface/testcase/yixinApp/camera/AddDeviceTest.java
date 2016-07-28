package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.camera;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;

import net.sf.json.JSONObject;

/**
 * 添加设备测试
 * @Description 
 * @author hzzhengyinyan
 * @date 2016年5月18日 上午10:09:45
 */
public class AddDeviceTest {
    String openId = "";
    String preSendUrl = "/yiXinApp/init/preSendInfo";
    String addCheckUrl = "/yiXinApp/init/check";
    String accessUrl = "/init/access";
    String location = "120.190915,30.188134";
    String userName = "86/13500000001";
    String yixinId =Config_yixinApp.test_userId ;//"4921001" ;
//    String userName = Config_yixinApp.test_userId;//Config_yixinApp.email_userId;//"zhengyinyan21@163.com";
    
    @BeforeClass
    public void beforeClass() throws Exception
    {
 //       openId = Config_yixinApp.test_openId;
//        openId = Config_yixinApp.email_openId;
        openId = CommonOperation_yixinApp.getOpenId2_1(Config_yixinApp.test_user, Config_yixinApp.test_area, Config_yixinApp.test_pwd, Config_yixinApp.test_deviceKey);
    }
    
    @Test(dataProvider = "normalData", alwaysRun = true)
    public void addDeviceTest_normal(int status, String preSendEntity, String accessEntity, String addCheckEntity , int code, String key) throws Exception{
        //预配置发送
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(preSendEntity, openId, preSendUrl,Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code, "预添加失败！"); 
         
        JSONObject addCheckRes=CommonOperation_yixinApp.postRequest(addCheckEntity, openId, addCheckUrl, Config_yixinApp.test_deviceKey);
        //设备接入服务器前，开始轮询检查是否添加成功
        for(int i=0; i<5; i++){
            addCheckRes = CommonOperation_yixinApp.postRequest(addCheckEntity, openId, addCheckUrl, Config_yixinApp.test_deviceKey);            
            Thread.sleep(300);
        }
        Assert.assertEquals(addCheckRes.getInt("devStatus"), 0, "设备还未添加！");
        //设备接入服务器
        JSONObject httpResponse1 = CommonOperation_yixinApp.accessNew(accessEntity, key);
        Assert.assertEquals(httpResponse1.getInt("result"), 0, "设备接入失败！");
        
      //设备接入服务器后，轮询检查是否添加成功
        for(int i=0; i<100; i++){
            addCheckRes =  CommonOperation_yixinApp.postRequest(addCheckEntity, openId, addCheckUrl, Config_yixinApp.test_deviceKey);            
            if(addCheckRes.getInt("devStatus")==1){
                System.out.println(i);
                break;
            }
            Thread.sleep(300);
        }
//        addCheckRes =  CommonOperation_yixinApp.postRequest(addCheckEntity, openId, addCheckUrl, Config_yixinApp.test_deviceKey); 
//        Assert.assertEquals(addCheckRes.getInt("devStatus"), 1, "设备还未添加！");
    }
    
    @Test(dataProvider = "errorData", alwaysRun = false, enabled = false)
    public void addDeviceTest_error(int status, String preSendEntity, String accessEntity, String addCheckEntity , int code, String key) throws Exception{
        JSONObject httpResponse = CommonOperation_yixinApp.postRequest(preSendEntity, openId, preSendUrl,Config_yixinApp.test_deviceKey);
        Assert.assertEquals(httpResponse.getInt("code"), code,  "预添加失败！"); 
    }
    
    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData()
    {
        return new Object[][]
        {
            {1,"{\"deviceId\":\"163021515123516\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yixin1\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                "cmdCode=1002&devID=163021515123516&devName=yixin1&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+yixinId,
                "{\"deviceId\":\"163021515123516\",\"userToken\":\""+userName+"\"}",
                200,"123456"},
            {2,"{\"deviceId\":\"163021515123517\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yixin1\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                    "cmdCode=1002&devID=163021515123517&devName=yixin1&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+yixinId,
                    "{\"deviceId\":\"163021515123517\",\"userToken\":\""+userName+"\"}",
                    200,"123456"},
            {3,"{\"deviceId\":\"163021515123518\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yixin1\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                        "cmdCode=1002&devID=163021515123518&devName=yixin1&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+yixinId,
                        "{\"deviceId\":\"163021515123518\",\"userToken\":\""+userName+"\"}",
                        200,"123456"},
//          //非首次接入
//            {3,"{\"deviceId\":\"1111111311\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
//                "cmdCode=1002&devID=1111111311&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
//                "{\"deviceId\":\"1111111311\",\"userToken\":\""+userName+"\"}",
//                200,"123456"},
//            //同一个摄像头接入到其他易信用户中，非首次接入
//            {4,"{\"deviceId\":\"111111411\",\"userToken\":\""+userName+"\",\"key\":\"123456789\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
//                "cmdCode=1002&devID=111111411&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
//                "{\"deviceId\":\"111111411\",\"userToken\":\""+userName+"\"}",
//                200,"123456789"},
//            //同一个摄像头接入到其他易信用户中，首次接入
//            {5,"{\"deviceId\":\"11111111\",\"userToken\":\""+userName+"\",\"key\":\"123456789\",\"name\":\"zyy1234567890\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
//                "cmdCode=1002&devID=11111111&devName=zyy1234567890&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
//                "{\"deviceId\":\"11111111\",\"userToken\":\""+userName+"\"}",
//                200,"123456789"},
//            //首次接入，不接入,轮询结果失败
//            {6,"{\"deviceId\":\"22222211\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
//                "cmdCode=1002&devID=22222211&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
//                "{\"deviceId\":\"22222211\",\"userToken\":\""+userName+"\"}",
//                200,"123456"},
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
          //首次接入+key不同
            {1,"{\"deviceId\":\"1111111111\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                "cmdCode=1002&devID=1111111111&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
                "{\"deviceId\":\"1111111111\",\"userToken\":\""+userName+"\"}",
                200,"123456789"},  
            //首次接入+key为空  ==》接入成功
            {2,"{\"deviceId\":\"2222222211\",\"userToken\":\""+userName+"\",\"key\":\"\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                "cmdCode=1002&devID=2222222211&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
                "{\"deviceId\":\"2222222211\",\"userToken\":\""+userName+"\"}",
                200,""},
            //首次接入+deviceId为空；预发送，接入返回成功，轮询结果：未接入
            {3,"{\"deviceId\":\"\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
                "cmdCode=1002&devID=&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
                "{\"deviceId\":\"\",\"userToken\":\""+userName+"\"}",
                200,"123456"},
            //首次接入+userToken为空
//            {4,"{\"deviceId\":\"44444444\",\"userToken\":\"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
//                "cmdCode=1002&devID=44444444&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId=",
//                "{\"deviceId\":\"44444444\",\"userToken\":\"\"}",
//                1220001,"123456"},
            //首次接入+app和设备的userToken不一致 ，目前接入成功
            {5,"{\"deviceId\":\"111111115611\",\"userToken\":\""+userName+"\",\"key\":\"123456\",\"name\":\"yy\",\"location\":\"123.2,63.1\",\"timeZone\":\"\"}",
            "cmdCode=1002&devID=111111115611&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
            "{\"deviceId\":\"111111115611\",\"userToken\":\""+userName+"\"}",
            200,"123456"},
            //首次接入+其他用户强行接入设备 （未进行预发送操作），目前接入成功
            {6,"",
            "cmdCode=1002&devID=111111115611&devName=yy&expireTime=123&initFlag=1&location=123.2,63.1&token=123&yiXinId="+userName,
            "{\"deviceId\":\"111111115611\",\"userToken\":\""+userName+"\"}",
            400,"123456"},
            //非首次接入+其他用户强行接入设备（未进行预发送操作） ，目前接入成功
            {7,"",
            "cmdCode=1002&devID=111111115611&devName=yy&expireTime=123&initFlag=0&location=123.2,63.1&token=123&yiXinId="+userName,
            "{\"deviceId\":\"111111115611\",\"userToken\":\""+userName+"\"}",
            400,"123456"},
        };
    }
}
