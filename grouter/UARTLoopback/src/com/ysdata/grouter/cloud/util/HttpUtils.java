package com.ysdata.grouter.cloud.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Http 请求工具类 OkHttp 3.2.0
 */
public class HttpUtils {

//	private static OkHttpClient client = new OkHttpClient();
    private static OkHttpClient client = 
    		new OkHttpClient.Builder()
    			.connectTimeout(10, TimeUnit.SECONDS)
    			.retryOnConnectionFailure(false)
    			.build();

    /**
     * GET
     *
     * @param url    请求URL
     * @param header 请求头
     * @return
     */
    public static String get(String url, Map<String, String> header) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(url).build();

        AppUtil.log("GET -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("GET response succeed:" + result);
            } else {
                // 失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("GET response failed:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * POST
     *
     * @param url    请求URL
     * @param header 请求头
     * @param param  请求参数
     * @return
     */
    public static String post(String url, Map<String, String> header, Map<String, String> param) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 请求体
        FormBody.Builder body = new FormBody.Builder();
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                body.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(url).post(body.build()).build();

        AppUtil.log("POST -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("POST response succeed:" + result);
            } else {
                // 失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("POST response failed:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * POST JSON
     *
     * @param url    请求URL
     * @param header 请求头
     * @param json   请求参数
     * @return
     */
    public static String postJson(String url, Map<String, String> header, String json) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 请求体
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = builder.url(url).post(body).build();

        AppUtil.log("POST JSON -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("POST JSON 请求成功:" + result);
            } else {
                // 失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("POST JSON 请求失败:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * PUT
     *
     * @param url    请求URL
     * @param header 请求头
     * @param param  请求参数
     * @return
     */
    public static String put(String url, Map<String, String> header, Map<String, String> param) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 请求体
        FormBody.Builder body = new FormBody.Builder();
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                body.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(url).put(body.build()).build();

        AppUtil.log("PUT -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("PUT 请求成功:" + result);
            } else {
                // 失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("PUT 请求失败:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * UPLOAD
     *
     * @param url    请求URL
     * @param header 请求头
     * @param files  上传文化列表
     * @return
     */
    public static String upload(String url, Map<String, String> header, List<File> files) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 请求体
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                body.addFormDataPart(file.getName(), file.getPath(), RequestBody.create(MediaType.parse("image/jpeg"), file));
            }
        }

        Request request = builder.url(url).post(body.build()).build();

        AppUtil.log("UPLOAD -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("UPLOAD 多文件成功:" + result);
            } else {
                //失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("UPLOAD 多文件失败:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * DELETE
     *
     * @param url    请求URL
     * @param header 请求头
     * @return
     */
    public static String delete(String url, Map<String, String> header) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(url).delete(new FormBody.Builder().build()).build();

        AppUtil.log("DELETE -> " + url);
        AppUtil.log("TICKET -> " + CacheManager.getTicket());

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("DELETE 请求成功:" + result);
            } else {
                //失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("DELETE 请求失败:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * POST FormBody
     *
     * @param url    请求URL
     * @param header 请求头
     * @param param  请求参数
     * @return
     */
    public static String postFormBody(String url, Map<String, String> header, Map<String, String> param) {
        String result = "";
        CacheManager.setNetErrorMsg("");
        // 请求头
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 请求体
        FormBody.Builder body = new FormBody.Builder();
        if (param != null && param.size() > 0) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                body.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(url).post(body.build()).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //成功
                result = response.body().string();
                AppUtil.log("POST FormBody success:" + result);
            } else {
                //失败
            	CacheManager.setNetErrorMsg(response.message());
                AppUtil.log("POST FormBody fail:" + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}