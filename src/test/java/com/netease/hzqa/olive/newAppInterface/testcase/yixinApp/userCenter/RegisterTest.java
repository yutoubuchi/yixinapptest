package com.netease.hzqa.olive.newAppInterface.testcase.yixinApp.userCenter;

import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.netease.hzqa.olive.newAppInterface.common.CommonOperation_yixinApp;
import com.netease.hzqa.olive.newAppInterface.common.Config_yixinApp;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.AESUtils;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.EnDecryptionUtil;
import com.netease.hzqa.olive.newAppInterface.utils.encrypt.RSAUtils;

import net.sf.json.JSONObject;

public class RegisterTest {

    String encryptPassword = "123456";
    String verifyCode = "1234";
    String secKey = "";
    String loginToken = "";

    @BeforeClass
    public void beforeClass() throws Exception {
        // 获取公钥和loginToken
        JSONObject res0 = CommonOperation_yixinApp.getEncryptData("{}");
        loginToken = res0.getJSONObject("result").getString("loginToken");

        // 生成客户端增益值
        String clientNonce = EnDecryptionUtil.createRandomString(16, System.currentTimeMillis());
        // MD5加密
        String mdPass = DigestUtils.md5Hex(encryptPassword);

        // 用服务器加密值第一次AES加密
        encryptPassword = AESUtils.encrypt(mdPass, res0.getJSONObject("result").getString("nonce"));
        // 用客户端自己生成的clientOnece第二次AES加密。得到最终密文
        encryptPassword = AESUtils.encrypt(encryptPassword, clientNonce);

        // 用拼接的公钥RSA加密
        RSAPublicKey rsaPublicKey = RSAUtils.getRSAPublidKey(res0.getJSONObject("result").getString("modulus"),
                res0.getJSONObject("result").getString("pubKey"));
        // 加密反转客户端增益值
        secKey = RSAUtils.encryptStringForPkcs(rsaPublicKey, StringUtils.reverse(clientNonce));
    }

    @Test(dataProvider = "normalData", alwaysRun = true)
    public void registerTest_normal(int status, String entity, String userName, String deviceKey, int code, String msg) throws Exception {

        if (status == 3) {// 不传验证码，注册失败
            JSONObject res = CommonOperation_yixinApp.postRequest(entity, "", "/yiXinApp/user/register", deviceKey);
            Assert.assertEquals(res.getInt("code"), code, msg);
        }
        else {
            // 获取手机验证码
            CommonOperation_yixinApp.postRequest("{\"userName\":\"" + userName + "\",\"phoneArea\":\"86\",\"language\":\"ch\"}", "",
                    "/yiXinApp/user/sendVerifyCode", deviceKey);
            // JSONObject res1 =
            // CommonOperation_yixinApp.postRequest("{\"userName\":\""+userName+"\",\"phoneArea\":\"86\",\"language\":\"ch\"}",
            // "", "/yiXinApp/user/getVerifyCode");
            verifyCode = "1233";// res1.getString("verify");

            JSONObject res = CommonOperation_yixinApp.postRequest(entity + ",\"verifyCode\":\"" + verifyCode + "\"}", "", "/yiXinApp/user/register", deviceKey);
            Assert.assertEquals(res.getInt("code"), code, msg);
        }

    }

    @Test(dataProvider = "errorData", enabled = true)
    public void registerTest_error(int status, String entity, String userName, String deviceKey, int code, String msg) throws Exception {

        // 获取手机验证码
        CommonOperation_yixinApp.postRequest("{\"userName\":\"" + userName + "\",\"phoneArea\":\"86\",\"language\":\"ch\"}", "",
                "/yiXinApp/user/sendVerifyCode", deviceKey);
//        JSONObject res1 = CommonOperation_yixinApp.postRequest("{\"userName\":\"" + userName + "\",\"phoneArea\":\"86\",\"language\":\"ch\"}", "",
//                "/yiXinApp/user/getVerifyCode", deviceKey);
//        verifyCode = res1.getString("verify");
        if (status == 5) {
            CommonOperation_yixinApp.postRequest("{\"userName\":\"" + userName + "\",\"phoneArea\":\"86\",\"language\":\"ch\"}", "",
                    "/yiXinApp/user/sendVerifyCode", deviceKey);
            CommonOperation_yixinApp.postRequest("{\"userName\":\"" + userName + "\",\"phoneArea\":\"86\",\"language\":\"ch\"}", "",
                    "/yiXinApp/user/getVerifyCode", deviceKey);
        }
        JSONObject res;
        if (status == 9) {
            res = CommonOperation_yixinApp.postRequest(entity + "}", "", "/yiXinApp/user/register", deviceKey);
        }
        else if (status == 7)
            res = CommonOperation_yixinApp.postRequest(entity + ",\"verifyCode\":\"\"}", "", "/yiXinApp/user/register", deviceKey);
        else if (status == 4) {
            res = CommonOperation_yixinApp.postRequest(entity + ",\"verifyCode\":\"1546\"}", "", "/yiXinApp/user/register", deviceKey);
        }
        else
            res = CommonOperation_yixinApp.postRequest(entity + ",\"verifyCode\":\"" + verifyCode + "\"}", "", "/yiXinApp/user/register", deviceKey);
        Assert.assertEquals(res.getInt("code"), code, msg);
    }

    /**
     * 正常测试数据
     */
    @DataProvider
    public Object[][] normalData() {
        return new Object[][] { { 1, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"13999990012\",\"userPass\":\"" + encryptPassword
                + "\",\"clientNonce\":\"" + secKey + "\"", "13999990012", Config_yixinApp.new_deviceKey, 1220006, "已注册手机用户1,无区号，默认86" },

                { 2, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"13999999030\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "\",\"phoneArea\":\"86\"", "13999999030", Config_yixinApp.new_deviceKey, 1220006, "注册手机用户1,不同区号" }, 

        };
    }

    /**
     * 异常测试数据
     */
    @DataProvider
    public Object[][] errorData() {
        return new Object[][] {
                { 1, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"19900000001\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "19900000001", Config_yixinApp.new_deviceKey, 1220029, "注册用户1,该用户已注册！" },
                { 2, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"18900000002\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "a911dfd226868c82c9f6151499a3ff8e9110120cda5b7c5524e4d27329a8cd180cfe14296d404eeeb7ed6211e1c214ba40f568d0b79d2e97312f15715530bc3964351be00ab1877ea7dc688435b12ce04643347a0200d86bfa4b51538af975c932c8754a0bcf3d040f8b24cff1d414665bdf9c598d57d6566e2312bb1b8fb03a"
                        + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "18900000002", Config_yixinApp.new_deviceKey, 1220006,
                        "注册用户2,clientNonce密码错误或登录会话超时" },
                { 3, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"17900000002\",\"userPass\":\""
                        + "R75sOC64tWyaoS21v26ruTTyHdPcFXKyWpk8eEfRXfJq0pKAKpe3rYkAlpJACLEswjILA85xRIH2hCm8OXwmI8w1d3iWhaP6cbh0n9FwWwI="
                        + "\",\"clientNonce\":\"" + secKey + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "17900000002", Config_yixinApp.new_deviceKey, 1220006,
                        "注册用户3,userPass密码错误或登录会话超时" },
                { 4, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"16900000001\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "16900000001", Config_yixinApp.new_deviceKey, 1220029, "注册用户4,短信验证码错误！" },
                { 5, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"15900000002\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "15900000001", Config_yixinApp.new_deviceKey, 1220006, "注册用户5,短信验证码已过期！" },
                { 6, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"18900000003\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "18900000001", Config_yixinApp.new_deviceKey, 1220006, "注册用户5,短信验证码发到其他手机，验证码错误！" },
                { 7, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"18900000004\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "123" + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "18900000004", Config_yixinApp.new_deviceKey, 1220001, "注册用户5,短信验证码为空！" },
                { 8, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"zhengyinyan21@163.com\",\"userPass\":\"" + encryptPassword + "\",\"verifyCode\":\""
                        + "\",\"clientNonce\":\"" + secKey + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "zhengyinyan21@163.com", Config_yixinApp.new_deviceKey, 1220001,
                        "注册邮箱用户1,该用户已注册！" },
                { 9, "{\"loginToken\":\"" + loginToken + "\",\"userName\":\"18900000004\",\"userPass\":\"" + encryptPassword + "\",\"clientNonce\":\"" + secKey
                        + "123" + "\",\"phoneArea\":\"86\",\"language\":\"ch\"", "18900000004", Config_yixinApp.new_deviceKey, 1220001, "注册用户5,不传短信验证码！" }, };
    }
}