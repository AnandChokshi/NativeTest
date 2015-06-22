package com.inventure.test.nativetest.testUtil;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Anand on 6/16/2015.
 */
public class JsonCreator {
    public static JSONArray getJsonArray() throws JSONException {
        JSONArray jsonArray = new JSONArray("[{'index':0,'question':'Select one of the two','type':1,'options':[{'option':'Mayo Stone'},{'option':'Holcomb Stout'}]},{'index':1,'question':'Select one of the Three','type':1,'options':[{'option':'Goldie Cervantes'},{'option':'Debora Craft'},{'option':'Roy Rogers'}]},{'index':2,'question':'Type Introduction','type':2},{'index':3,'question':'Select from checkboxes','type':3,'options':[{'option':'first'},{'option':'second'},{'option':'third'},{'option':'four'},{'option':'five'}]}]");
        return jsonArray;
    }
}
