package com.web.wmpay.wmwebpaydemo.util;

import android.text.TextUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 解析特殊的URL
 * Created by East.K on 2018/1/4.
 */

public class AnalysisURLUtil {

    /**
     * 获取Url字符串的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParam(String url) {
        return getUrlParam(url, true);
    }

    /**
     * 获取Url字符串的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParam(String url, boolean isDecode) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (Util.isEmpty(url)) {
            return paramMap;
        }
        try {
            URI uri = new URI(url);
            String params = isDecode ? uri.getQuery() : uri.getRawQuery();
            if (params != null && !"".equals(params)) {
                String paramArray[] = params.split("&");
                if (paramArray != null && paramArray.length != 0) {
                    for (String pams : paramArray) {
                        String keyValuePams[] = pams.split("=");
                        if (keyValuePams != null && keyValuePams.length == 2) {
                            if (isDecode) {
                                paramMap.put(keyValuePams[0], URLDecoder.decode(keyValuePams[1], "UTF-8"));
                            } else {
                                paramMap.put(keyValuePams[0], keyValuePams[1]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramMap;
    }

    public static String getUrlHost(String url) {
        String host = "";
        if (Util.isEmpty(url)) {
            return host;
        }
        try {
            URI uri = new URI(url);
            host = uri.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return host;
    }

    public static String getUrlPath(String url) {
        String path = "";
        if (Util.isEmpty(url)) {
            return path;
        }
        try {
            URI uri = new URI(url);
            path = uri.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * 返回图片的宽高比
     */
    public static float getBitmapStaio(String url) {
        if (TextUtils.isEmpty(url)) {
            return 0;
        }
        Map<String, String> urlParam = getUrlParam(url);
        //mdw=264&mdh=220
        if (urlParam == null || urlParam.isEmpty()) {
            return 0;
        } else {
            String width = urlParam.get("mdw");
            String height = urlParam.get("mdh");
            try {
                return Float.parseFloat(height) / Float.parseFloat(width);
            } catch (Exception e) {
                return 0;
            }

        }
    }

    /**
     * @param data
     * @return
     */
    public static String insertParam(Map<String, String> data) {
        StringBuffer buffer = new StringBuffer();
        if (data != null) {
            Set<String> strings = data.keySet();
            for (String key : strings) {
                if (!Util.isEmpty(key)) {
                    String value = data.get(key);
                    String temParam = key + "=" + value + "&";
                    buffer.append(temParam);
                }
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        return buffer.toString();
    }

}
