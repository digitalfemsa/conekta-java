package io.digitalfemsa;

/**
 *
 * @author mauricio
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConektaObject extends ArrayList {

    private HashMap values;
    public String id;

    public ConektaObject(String id) {
        this.values = new HashMap();
        this.id = id;
    }

    public ConektaObject() {
        this.values = new HashMap();
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getVal(String key) {
        return this.values.get(key);
    }

    public void setVal(String key, Object value) {
        this.values.put(key, value);
    }

    public void loadFromArray(JSONArray jsonArray) throws Error {
        loadFromArray(jsonArray, null);
    }

    public void loadFromArray(JSONArray jsonArray, String className) throws Error {
        for (int i = 0; i < jsonArray.length(); i++) {
            ConektaObject conektaObject = aux(jsonArray, className, i);
            this.add(conektaObject);
        }
    }

    protected static ConektaObject aux(JSONArray jsonArray, String className, Integer i) throws Error {
        String key;
        JSONObject jsonObject;
        try {
            if (className != null) {
                key = className;
            }
            else if (jsonArray.getJSONObject(i).has("type") &&
                    jsonArray.getJSONObject(i).getString("type").equals("card")) {
                key = "card";
            } else if (jsonArray.getJSONObject(i).has("type") &&
                    jsonArray.getJSONObject(i).getString("type").equals("oxxo_recurrent")) {
                key = "offline_recurrent_reference";
            } else {
                key = jsonArray.getJSONObject(i).getString("object");
            }
            jsonObject = jsonArray.getJSONObject(i);
        } catch (Exception e) {
            throw new Error(e.toString(), null, null, null, null);
        }
        ConektaObject conektaObject = ConektaObjectFromJSONFactory.ConektaObjectFactory(jsonObject, key);
        try {
            conektaObject.loadFromObject(jsonObject);
        } catch (Exception e) {
            throw new Error(e.toString(), null, null, null, null);
        }
        return conektaObject;
    }

    public void loadFromObject(JSONObject jsonObject) throws Exception {
        
        Iterator itr = jsonObject.keys();
        while (itr.hasNext()) {
            String key = itr.next().toString();
            
            Object obj = jsonObject.get(key);
            
            try {
                Field field = this.getClass().getField(key);
                field.setAccessible(true);
                Boolean isConektaObject = field.getType().getPackage().getName().equals("io.conekta");
                if (obj instanceof JSONObject && ((JSONObject) obj).has("object")) {
                    ConektaObject conektaObject = ConektaObjectFromJSONFactory.ConektaObjectFactory((JSONObject) obj, key);
                    field.set(this, conektaObject);
                    this.setVal(key, conektaObject);
                } else if (obj instanceof JSONArray || !obj.equals(null)) {
                    if (obj instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) obj;
                        if (jsonArray.length() > 0) {
                            String className = jsonArray.get(0).getClass().getCanonicalName();
                            if (className == "java.lang.String"){
                                List<String> conektaObject = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    conektaObject.add(jsonArray.getString(i));
                                }
                                field.set(this, conektaObject);
                                this.setVal(key, conektaObject);
                            } else if (className == "java.lang.Integer"){
                                List<Integer> conektaObject = new ArrayList<Integer>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    conektaObject.add(jsonArray.getInt(i));
                                }
                                field.set(this, conektaObject);
                                this.setVal(key, conektaObject);
                            } else {
                                ConektaObject conektaObject = new ConektaObject();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if ((jsonArray.getJSONObject(0).has("object"))) {
                                        conektaObject.add(ConektaObjectFromJSONFactory.ConektaObjectFactory(jsonArray.getJSONObject(i), jsonArray.getJSONObject(i).getString("object")));
                                    } else {
                                        conektaObject.add(ConektaObjectFromJSONFactory.ConektaObjectFactory(jsonArray.getJSONObject(i), key));
                                    }
                                }
                                field.set(this, conektaObject);
                                this.setVal(key, conektaObject);
                            }
                        }
                    } else {
                        if (isConektaObject) {
                            Constructor c;
                            c = Class.forName(field.getType().getCanonicalName()).getConstructor();
                            ConektaObject attr = (ConektaObject) c.newInstance();
                            attr.loadFromObject((JSONObject) obj);
                            field.set(this, attr);
                            this.setVal(key, attr);
                        } else if (obj instanceof JSONObject){
                            JSONObject hashObject = jsonObject.getJSONObject(key);
                            
                            Iterator jsonKeys = hashObject.keys();
                            HashMap map = new HashMap();
                            while(jsonKeys.hasNext()){
                                String k = jsonKeys.next().toString();
                                Object value = hashObject.get(k);

                                map.put(k, value);
                            }    

                            field.set(this, map);
                            this.setVal(key, map);
                        } else {
                            field.set(this, obj);
                            this.setVal(key, obj);
                        }
                    }
                }
            } catch (Exception e) {
                // No field found
                if (this.getClass().getCanonicalName().equals("io.conekta.LineItems")) {
                    // Vertical related fields
                    ((LineItems)this).addVerticalRelatedField(key, obj.toString());
                    this.setVal(key, obj);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (this.getClass().getSimpleName().equals("ConektaObject")) {
            StringBuilder result = new StringBuilder();
            result.append("[");
            for (int i = 0; i < this.size(); i++) {
                result.append(((ConektaObject) this.get(i)).values.toString());
                if ((i + 1) < this.size()) {
                    result.append(",");
                }
            }
            result.append("]");
            return result.toString();
        } else {
            return this.values.toString();
        }
    }

    protected static String toCamelCase(String s) {
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    protected static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
