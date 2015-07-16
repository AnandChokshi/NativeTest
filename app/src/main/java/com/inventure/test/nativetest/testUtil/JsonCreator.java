package com.inventure.test.nativetest.testUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anand on 6/16/2015.
 */
public class JsonCreator {
    public static JSONArray getJsonArray() throws JSONException {
        JSONObject jsonObject = new JSONObject("{'errors':[],'data':{'section':[{'condition':[],'showconfirmation':1,'pages':[{'condition':[],'questions':[{'question_server_id':'phone_owner','type':'radio','label':'Does this phone belong to you?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Relationship To Phone Owner cannot be blank.'},'options':[{'option':'Yes, this phone is mine'},{'option':'No, someone else owns this phone'}]},{'question_server_id':'shared_phone','type':'radio','label':'Do you share this phone with anyone?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Shared cannot be blank.'},'options':[{'option':'Yes'},{'option':'No'}]},{'question_server_id':'true_answers','type':'checkbox','label':'','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Please check the statement above to proceed.'},'options':[{'option':'I confirm that the answer I selected above is true and understand that answering dishonestly will disqualify me from Mkopo Rahisi loans. '}]}]},{'condition':[{'qid':'phone_owner','answer':'Yes, this phone is mine'}],'questions':[{'question_server_id':'mpesa_name','type':'textbox','label':'M-Pesa Registered Name','placeholder':'M-Pesa Registered Name','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':''},'options':[]},{'question_server_id':'phone','type':'textbox','label':'M-Pesa Phone','placeholder':'M-Pesa Phone','default':[''],'validation':{'required':1,'type':'phone','regex':'','error_message':''},'options':[]},{'question_server_id':'email','type':'textbox','label':'Email Address','placeholder':'Email Address','default':[''],'validation':{'required':1,'type':'email','regex':'','error_message':''},'options':[]},{'question_server_id':'dob','type':'datepicker','label':'Date of Birth','placeholder':'Date of Birth','default':[''],'validation':{'required':1,'type':'dob','regex':'','error_message':''},'options':[]},{'question_server_id':'nid','type':'textbox','label':'National ID','placeholder':'National ID','default':[''],'validation':{'required':1,'type':'nid','regex':'','error_message':''},'options':[]}]}]},{'condition':[{'qid':'phone_owner','answer':'Yes, this phone is mine'}],'showconfirmation':1,'pages':[{'condition':[],'questions':[{'question_server_id':'heard_about_us','type':'radio','label':'How did you hear about Mkopo Rahisi?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Referral Source cannot be blank.'},'options':[{'option':'Facebook Ad'},{'option':'Twitter'},{'option':'Yahoo'},{'option':'Friend'},{'option':'Family'},{'option':'Flyer'},{'option':'Google Play Store'},{'option':'Epepea.com'},{'option':'Other'}]},{'question_server_id':'ref_code','type':'textbox','label':'Referral Code (Optional)','placeholder':'Enter Code Here','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]}]},{'condition':[],'questions':[{'question_server_id':'loan_purpose','type':'radio','label':'What do you need a loan for?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Loan Purpose cannot be blank.'},'options':[{'option':'Transport Expenses'},{'option':'Start or Grow my Business'},{'option':'A Special Occasion'},{'option':'Other'}]}]},{'condition':[{'qid':'loan_purpose','answer':'Transport Expenses'}],'questions':[{'question_server_id':'destination','type':'textarea','label':'Where are you traveling?','placeholder':'Example: Mombasa','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Destination cannot be blank.'},'options':[]},{'question_server_id':'travel_reason','type':'textarea','label':'Why are you taking the trip?','placeholder':'Example: I want to visit my family','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Travel Reason cannot be blank'},'options':[]}]},{'condition':[{'qid':'loan_purpose','answer':'Start or Grow my Business'}],'questions':[{'question_server_id':'business_type','type':'textarea','label':'What kind of business do you have?','placeholder':'Example: I have a book store','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Business Type cannot be blank'},'options':[]},{'question_server_id':'business_loan_purpose','type':'textarea','label':'How will you be using the loan?','placeholder':'Example: I want to buy stationery to sell in my shop','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Business Loan Purpose cannot be blank'},'options':[]}]},{'condition':[{'qid':'loan_purpose','answer':'A Special Occasion'}],'questions':[{'question_server_id':'special_ocassion','type':'textarea','label':'What is the special occasion?','placeholder':'Example: My son's birthday','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Special Ocassion cannot be blank'},'options':[]},{'question_server_id':'special_ocassion_goods','type':'textarea','label':'What will you be purchasing with the loan?','placeholder':'Example: Birthday cake','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Goods to Buy cannot be blank'},'options':[]}]},{'condition':[{'qid':'loan_purpose','answer':'Other'}],'questions':[{'question_server_id':'other_loan_purpose','type':'textarea','label':'What will you be using the loan for?','placeholder':'Example: I need to pay school fees for my daughter','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Loan Details cannot be blank.'},'options':[]}]},{'condition':[],'questions':[{'question_server_id':'loan_amount','type':'radio','label':'How much do you need to borrow?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Loan Amount cannot be blank.'},'options':[{'option':'2000 ksh'}]}]},{'condition':[],'questions':[{'question_server_id':'outstanding_loan','type':'radio','label':'Do you have any outstanding loans?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Outstanding Loan cannot be blank.'},'options':[{'option':'Yes'},{'option':'No'}]}]},{'condition':[],'questions':[{'question_server_id':'mpesa_how_often','type':'radio','label':'How often do you use M-Pesa?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Mpesa How Often cannot be blank.'},'options':[{'option':'Daily'},{'option':'Few Times A Week'},{'option':'Few Times A Month'}]}]},{'condition':[],'questions':[{'question_server_id':'living_situation','type':'radio','label':'What is your living situation?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Living Situation cannot be blank.'},'options':[{'option':'Own'},{'option':'Rent'},{'option':'Live with family (don't own or rent)'},{'option':'Student housing hostel'},{'option':'Other'}]},{'question_server_id':'living_years','type':'spinner','label':'How long have you been living there?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Select an option'},'options':[{'option':''},{'option':'0'},{'option':'1'},{'option':'2'},{'option':'3'},{'option':'4'},{'option':'5+'}]},{'question_server_id':'living_months','type':'spinner','label':'','placeholder':'','default':['0'],'validation':{'required':0,'type':'','regex':'','error_message':'Select an option'},'options':[{'option':'0'},{'option':'1'},{'option':'2'},{'option':'3'},{'option':'4'},{'option':'6'},{'option':'7'},{'option':'8'},{'option':'9'},{'option':'10'},{'option':'11'}]}]},{'condition':[],'questions':[{'question_server_id':'relationship_status','type':'radio','label':'What is your relationship status?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Relationship Status cannot be blank.'},'options':[{'option':'Single'},{'option':'Long-term relationship'},{'option':'Married'},{'option':'Other'}]}]},{'condition':[],'questions':[{'question_server_id':'education','type':'radio','label':'What is your highest level of education completed?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Education cannot be blank.'},'options':[{'option':'None'},{'option':'Primary'},{'option':'Secondary High School'},{'option':'College University'},{'option':'Masters'}]},{'question_server_id':'major','type':'textbox','label':'Major Concentration (Optional)','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]}]},{'condition':[],'questions':[{'question_server_id':'employment','type':'radio','label':'Are you currently employed ?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Employment cannot be blank.'},'options':[{'option':'Yes'},{'option':'No'}]},{'question_server_id':'income_amount','type':'textbox','label':'How much do you get paid?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Income Amount cannot be blank.'},'options':[{'option':'Yes'},{'option':'No'}]},{'question_server_id':'income_frequency','type':'spinner','label':'How often do you get paid?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Income Frequency cannot be blank.'},'options':[{'option':''},{'option':'Day'},{'option':'Week'},{'option':'Month'}]}]},{'condition':[{'qid':'employment','answer':'Yes'}],'questions':[{'question_server_id':'job_title','type':'textbox','label':'What is your job title ?','placeholder':'Example: Marketing Assistant','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Job Title cannot be blank.'},'options':[]},{'question_server_id':'job_title','type':'textbox','label':'What industry do you work in ?','placeholder':'Example: Retail','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Industry cannot be blank.'},'options':[]},{'question_server_id':'job_years','type':'spinner','label':'How long have you been at your current job?','placeholder':'','default':[''],'validation':{'required':1,'type':'','regex':'','error_message':'Select an option'},'options':[{'option':''},{'option':'0'},{'option':'1'},{'option':'2'},{'option':'3'},{'option':'4'},{'option':'5+'}]},{'question_server_id':'job_months','type':'spinner','label':'','placeholder':'','default':['0'],'validation':{'required':0,'type':'','regex':'','error_message':'Select an option'},'options':[{'option':'0'},{'option':'1'},{'option':'2'},{'option':'3'},{'option':'4'},{'option':'6'},{'option':'7'},{'option':'8'},{'option':'9'},{'option':'10'},{'option':'11'}]}]},{'condition':[],'questions':[{'question_server_id':'missing_number','type':'textbox','label':'Fill in the missing number for X 1, 6, 4, 9, 7, 12, 10, X, 13','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]}]},{'condition':[],'questions':[{'type':'plainText','content':'Optional:Please provide contact info for two people who can verify the information in your loan application.Providing references may improve your chances of being approved.Note: These people will be used as referees but not as guarantors of your loan.Referee 1'},{'question_server_id':'ref1_name','type':'textbox','label':'Name','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]},{'question_server_id':'ref1_phone','type':'textbox','label':'Name','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]},{'question_server_id':'ref1_relationship','type':'textbox','label':'Name','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]},{'type':'plainText','content':'Reference 2'},{'question_server_id':'ref2_name','type':'textbox','label':'Name','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]},{'question_server_id':'ref2_phone','type':'textbox','label':'Name','placeholder':'','default':[''],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]},{'question_server_id':'ref2_relationship','type':'textbox','label':'Name','placeholder':'','default':[],'validation':{'required':0,'type':'','regex':'','error_message':''},'options':[]}]}]}]},'notices':[]}");

        JSONArray jsonArray = jsonObject.getJSONArray("section");
        return jsonArray;
    }
}