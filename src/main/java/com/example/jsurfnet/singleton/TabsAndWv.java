package com.example.jsurfnet.singleton;

import com.example.jsurfnet.services.PasswordManager;
import javafx.scene.control.TabPane;
import  javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public final class TabsAndWv {

    static private TabPane tabPane;
    static private WebView webView;
    static private WebEngine engine;

    private static TabsAndWv instance = null;

    private static PasswordManager pp = null;

    private TabsAndWv(){}

    public static synchronized TabsAndWv getInstance() {
        if (instance == null) {
            instance = new TabsAndWv();
        }
        return instance;
    }

    public TabPane getTabPane(){
        return tabPane;
    }

    public WebView getWebView(){
        return webView;
    }

    public WebEngine getWebEngine(){
        return engine;
    }

    public void setWebEngine(WebEngine we){
        engine = we;
    }

    public void setWebView(WebView wv){
        webView = wv;
    }
    public void setTabPane(TabPane tp){
        tabPane = tp;
    }

    public static void setPasswordManager(PasswordManager pp) {
        TabsAndWv.pp = pp;
    }

    public PasswordManager getPasswordManager() {
        return pp;
    }



}
