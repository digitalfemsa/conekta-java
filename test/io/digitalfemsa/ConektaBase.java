package io.digitalfemsa;

import junit.framework.TestCase;

/**
 *
 * @author mauricio
 */
public class ConektaBase extends TestCase {
    public ConektaBase() {
        setApiKey();
    }

    public ConektaBase(String key) {
        setPublicApiKey();
    }

    public void setApiKey() {
        Digitalfemsa.apiKey = "key_ZLy4aP2szht1HqzkCezDEA";
    }

    public void setPublicApiKey() {
        Digitalfemsa.apiKey = "key_OfWoJc2fw6oEydKspmyr76Q";
    }

    public void unSetApiKey() {
        Digitalfemsa.apiKey = null;
    }
    
    public void setApiVersion(String version) {
        Digitalfemsa.apiVersion = version;
    }
    
    public void setApiBase(String base) {
        Digitalfemsa.apiBase = base;
    }
    
    public void setApiKey(String key) {
        Digitalfemsa.apiKey = key;
    }
}
