package io.digitalfemsa;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mauricio
 */
public abstract class Digitalfemsa {

    public static String apiKey;
    public static String apiBase = "https://api.digitalfemsa.io";
    public static String apiVersion = "2.0.0";
    public static final String VERSION = "2.1.1";
    public static String locale = "es";
    

    public static void setApiKey(String apiKey) {
        Digitalfemsa.apiKey = apiKey;
    }
    public static void setApiBase(String apiBase) {
        Digitalfemsa.apiBase = apiBase;
    }

    public static void setApiVerion(String version) {
        Digitalfemsa.apiVersion = version;
    }
    
    public static void setLocale(String locale) {
        Digitalfemsa.locale = locale;
    }
}