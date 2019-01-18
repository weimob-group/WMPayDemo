package com.web.wmpay.wmwebpaydemo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.web.wmpay.wmwebpaydemo.util.AnalysisURLUtil;
import com.web.wmpay.wmwebpaydemo.util.Util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWebView();
        loadUrl();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack(); 
        } else {
            finish();
        }
    }


    private void initWebView() {
        webView = findViewById(R.id.webView);
        initWebSettings();
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("nosdkwebview", "[" + consoleMessage.messageLevel() + "] " + consoleMessage.message() + "(" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")");
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    private void initWebSettings() {
        WebSettings webSettings = webView.getSettings();

        // JSEngine Enable
        webSettings.setJavaScriptEnabled(true);

        webSettings.setUseWideViewPort(true);

        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 设置可以使用localStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);

        // 应用可以有缓存
        webSettings.setAppCacheEnabled(true);
        String appCaceDir = this.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE)
                .getPath();
        webSettings.setAppCachePath(appCaceDir);

        // 可以读取文件缓存(manifest生效)
        webSettings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        initWebClient();
    }

    private void loadUrl() {
        // 测试链接
        webView.loadUrl("http://3400.retail.n.saas.weimobqa.com/saas/retail/3400/12556400/goods/list");
    }

    private void initWebClient() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("nosdk===>shouldO ", url);
                boolean need = false;
                if (Util.isEmpty(url)) {
                    return need;
                }
                String redirectURLKey = "redirect_url";
                if (url.startsWith("https://wx.tenpay.com/")) {
                    need = true;
                    Map<String, String> urlParam = AnalysisURLUtil.getUrlParam(url, false);
                    String oldReicUrl = urlParam.get(redirectURLKey);
                    if (urlParam != null) {
                        try {
                            Map header = new HashMap<String, String>();
                            header.put("Referer", URLDecoder.decode(oldReicUrl, "utf-8"));
                            view.loadUrl(url, header);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (url.startsWith("weixin://")) {
                    need = true;
                    Intent intent = null;
                    try {

                        intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // forbid launching activities without BROWSABLE category
                        intent.addCategory("android.intent.category.BROWSABLE");
                        // forbid explicit call
                        intent.setComponent(null);
                        // forbid intent with selector intent
                        intent.setSelector(null);
                        MainActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (need) {
                    return need;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }
        });
    }

}
