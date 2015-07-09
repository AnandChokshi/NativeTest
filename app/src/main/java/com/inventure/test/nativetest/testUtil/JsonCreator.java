package com.inventure.test.nativetest.testUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anand on 6/16/2015.
 */
public class JsonCreator {
    public static JSONArray getJsonArray() throws JSONException {
        JSONObject jsonObject = new JSONObject("{'pages':[{'page_order':'1','condition':{'qid':'','answer':''},'questions':[{'question_server_id':'1','type':'textbox','label':'Please enter your name','placeholder':'e.g. Bob Letsdo','default':[],'validation':{'required':'1','validation_type':'alphanumeric','regex':'','error_message':''}},{'question_server_id':'2','type':'spinner','label':'What is your favourite colour?','default':[{'value':'green'}],'validation':{'required':'0','validation_type':'','regex':'','error_message':''},'options':[{'option':'red'},{'option':'green'},{'option':'blue'},{'option':'yellow'}]}]},{'page_order':'2','condition':{'qid':'2','answer':'yellow'},'questions':[{'question_server_id':'4','type':'checkbox','label':'Do you have any pet? (Check all that applies)','default':[{'value':'dog'}],'validation':{'required':'0','validation_type':'','regex':'','error_message':''},'options':[{'option':'rabbit'},{'option':'cat'},{'option':'dog'},{'option':'scottish unicorn'}]}]},{'page_order':'3','condition':{'qid':'','answer':''},'questions':[{'question_server_id':'3','type':'datepicker','label':'What is your date of birth?','default':[],'validation':{'required':'1','validation_type':'regex','regex':'**We will add something here later for age validation > 17**','error_message':''}}]},{'page_order':'4','condition':{'qid':'','answer':''},'questions':[{'question_server_id':'14','type':'textbox','label':'Please enter your phone','placeholder':'e.g. 254712345678','default':[],'validation':{'required':'1','validation_type':'regex','regex':'**','error_message':''}},{'question_server_id':'10','type':'textbox','label':'Please enter your email','placeholder':'e.g. bob@gmail.com','default':[],'validation':{'required':'1','validation_type':'email','regex':'','error_message':''}}]}]}");

        JSONArray jsonArray = jsonObject.getJSONArray("pages");
        return jsonArray;
    }
}