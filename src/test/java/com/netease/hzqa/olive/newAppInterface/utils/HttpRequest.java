package com.netease.hzqa.olive.newAppInterface.utils;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class HttpRequest {
    private static final Logger logger = Logger.getLogger(HttpRequest.class);
    private static CloseableHttpClient httpClient = null;
    // 创建CookieStore实例
    static CookieStore cookieStore = new BasicCookieStore();
//    static CookieStore testCookie = new BasicCookieStore();
//    static CookieStore mobileCookie = new BasicCookieStore();
//    static CookieStore emailCookie = new BasicCookieStore();
    // static HttpClientContext context = null;

    public static JSONObject search(Http httpRequest, String host, String path, int port) throws Exception {

        // step 1: 创建HttpClient对象
        httpClient = HttpClients.createDefault();
 
        HttpUriRequest request = null;

        // step 2: 创建请求方法的实例，并指定请求URL
//        StringBuilder sb = null;
        String url = null;
        URIBuilder builder = new URIBuilder();

        if (port == 0) {
            builder.setScheme("http").setHost(host).setPath(path);
        }
        else {
            builder.setScheme("http").setHost(host).setPort(port).setPath(path);
        }

        List<Parameter> paras = httpRequest.getParameters();
        if (paras != null) {
            for (Parameter p : paras) {
                builder.setParameter(p.getName(), p.getValue().toString());
            }
        }
        url = builder.build().toString();

        logger.debug("url: " + url);

        // 选择一个请求方法
        if (httpRequest.getConnection().equalsIgnoreCase("post")) {
            logger.info("   [  post  ] " + url);
            request = new HttpPost(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("get")) {
            logger.info("   [  get   ] " + url);
            request = new HttpGet(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("delete")) {
            logger.info("   [ delete ] " + url);
            request = new HttpDelete(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("put")) {
            logger.info("   [  put   ] " + url);
            request = new HttpPut(url);
        }
        else
            Assert.fail("connection donnot support :" + httpRequest.getConnection());

        // step 3: 如果需要设置请求的消息报头，可调用addHeader(String name, String value)方法
        // header builder
        List<Parameter> headers = httpRequest.getHeaders();
        if (headers != null) {
            for (Parameter p : headers) {
                logger.info("   [ Header ] " + String.format("%1$-20s :     %2$s", p.getName(), p.getValue()));
                request.addHeader(p.getName(), p.getValue().toString());
            }
        }

        // step 4: 如果需要设置请求的实体，可调用setEntity(List entity)方法
        // entity builder
        List<Entity> entities = httpRequest.getEntity();
        if (entities != null) {
            for (Entity e : entities) {
                StringEntity se = new StringEntity(e.getValue(), "utf-8");
                if (httpRequest.getConnection().equalsIgnoreCase("post")) {
                    HttpPost post = (HttpPost) request;
                    post.setEntity(se);
                }
                else if (httpRequest.getConnection().equalsIgnoreCase("put")) {
                    HttpPut put = (HttpPut) request;
                    put.setEntity(se);
                }
            }
        }

        // step 5: 调用HttpClient对象的execute
        HttpResponse response = httpClient.execute(request);

        // step 6: 解析请求的返回
        String resString = "";
        if (response.getEntity() != null)
            resString = EntityUtils.toString(response.getEntity(), "UTF-8");

        httpClient.close();
        int code;
        JSONObject responseJson = null;

        // 当没有可返回的JSONObject时，默认打印出Http响应码
        if (resString.isEmpty()) {
            // 获取响应的状态码
            code = response.getStatusLine().getStatusCode();
            logger.info("INFO: Didn't get HTTP return entity, response code is: " + code);
            // return null;
            return JSONObject.fromObject("{}");
        }
        else {
            logger.info("   [Response] " + resString);
            try {
                responseJson = JSONObject.fromObject(resString);
                logger.info("   [Response] " + responseJson);
                return responseJson;
            }
            catch (JSONException e) {
                e.printStackTrace();
                logger.warn("INFO: Http response is NOT JSON format, it is " + resString);
                responseJson = JSONObject.fromObject("{}");
                // return null;
                return JSONObject.fromObject("{}");
            }
        }
    }
    // 返回code和原始json内容
    public static JSONObject search(Http httpRequest, String host, String path, int port, CookieStore cookie) throws Exception {

        // step 1: 创建HttpClient对象
        httpClient = HttpClients.createDefault();
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
 
        HttpUriRequest request = null;

        // step 2: 创建请求方法的实例，并指定请求URL
//        StringBuilder sb = null;
        String url = null;
        URIBuilder builder = new URIBuilder();

        if (port == 0) {
            builder.setScheme("http").setHost(host).setPath(path);
        }
        else {
            builder.setScheme("http").setHost(host).setPort(port).setPath(path);
        }

        List<Parameter> paras = httpRequest.getParameters();
        if (paras != null) {
            for (Parameter p : paras) {
                builder.setParameter(p.getName(), p.getValue().toString());
            }
        }
        url = builder.build().toString();

        logger.debug("url: " + url);

        // 选择一个请求方法
        if (httpRequest.getConnection().equalsIgnoreCase("post")) {
            logger.info("   [  post  ] " + url);
            request = new HttpPost(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("get")) {
            logger.info("   [  get   ] " + url);
            request = new HttpGet(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("delete")) {
            logger.info("   [ delete ] " + url);
            request = new HttpDelete(url);
        }
        else if (httpRequest.getConnection().equalsIgnoreCase("put")) {
            logger.info("   [  put   ] " + url);
            request = new HttpPut(url);
        }
        else
            Assert.fail("connection donnot support :" + httpRequest.getConnection());

        // step 3: 如果需要设置请求的消息报头，可调用addHeader(String name, String value)方法
        // header builder
        List<Parameter> headers = httpRequest.getHeaders();
        if (headers != null) {
            for (Parameter p : headers) {
                logger.info("   [ Header ] " + String.format("%1$-20s :     %2$s", p.getName(), p.getValue()));
                request.addHeader(p.getName(), p.getValue().toString());
            }
        }

        // step 4: 如果需要设置请求的实体，可调用setEntity(List entity)方法
        // entity builder
        List<Entity> entities = httpRequest.getEntity();
        if (entities != null) {
            for (Entity e : entities) {
                StringEntity se = new StringEntity(e.getValue(), "utf-8");
                if (httpRequest.getConnection().equalsIgnoreCase("post")) {
                    HttpPost post = (HttpPost) request;
                    post.setEntity(se);
                }
                else if (httpRequest.getConnection().equalsIgnoreCase("put")) {
                    HttpPut put = (HttpPut) request;
                    put.setEntity(se);
                }
            }
        }

        // step 5: 调用HttpClient对象的execute
        HttpResponse response = httpClient.execute(request);

        // step 6: 解析请求的返回
        String resString = "";
        if (response.getEntity() != null)
            resString = EntityUtils.toString(response.getEntity(), "UTF-8");

        // 调用登录接口，就更新cookie,
        if (path.equals("/oliveApp/user/login") || path.equals("/yiXinApp/user/login")) {
            // cookie store
            setCookieStore(response);
        }
        httpClient.close();
        int code;
        JSONObject responseJson = null;

        // 当没有可返回的JSONObject时，默认打印出Http响应码
        if (resString.isEmpty()) {
            // 获取响应的状态码
            code = response.getStatusLine().getStatusCode();
            logger.info("INFO: Didn't get HTTP return entity, response code is: " + code);
            // return null;
            return JSONObject.fromObject("{}");
        }
        else {
            logger.info("   [Response] " + resString);
            try {
                responseJson = JSONObject.fromObject(resString);
                logger.info("   [Response] " + responseJson);
                return responseJson;
            }
            catch (JSONException e) {
                e.printStackTrace();
                logger.warn("INFO: Http response is NOT JSON format, it is " + resString);
                responseJson = JSONObject.fromObject("{}");
                // return null;
                return JSONObject.fromObject("{}");
            }
        }
    }

    public static void setCookieStore(HttpResponse httpResponse) {
        try{
            System.out.println("----setCookieStore");
            // JSESSIONID
            String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
            String cookieName = setCookie.substring("olive_app_cookies_name_=".length(), setCookie.indexOf(";"));

            // 新建一个Cookie
            BasicClientCookie cookie = new BasicClientCookie("olive_app_cookies_name_", cookieName);
            cookie.setVersion(0);
            cookie.setDomain("apptest.x.163.com");
            cookie.setPath("/");
            cookieStore.clear();
            cookieStore.addCookie(cookie);
        }
        catch (NullPointerException ex){
            System.out.println("login failed, cannot get cookies info!");
        }
    }
    // olive_app_cookies_name_=4b61e383-2238-4101-9f89-ba9a468f784f;
    // Domain=apptest.x.163.com; Expires=Tue, 22-Dec-2015 13:46:46 GMT; Path=/;
    // HttpOnly
}
