package it.sysmanagement.taggalo.monitoring.jira;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class JiraManager {
	
	private static String getBase64Auth(){
		String authString = Constants.JIRA_USER+ ":"+Constants.JIRA_PWD;
		byte[] encodedBytes = Base64.encodeBase64(authString.getBytes());
		String base64AuthString = new String(encodedBytes);
		System.out.println("encodedBytes " + base64AuthString);
		return base64AuthString;
	}
	
	public static int openNewSensorIssue(String idSensor,String alarmType, String alarm) throws ClientProtocolException, IOException{
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(Constants.JIRA_HOST + "issue");
		StringEntity entity = new StringEntity(buildNewIssueObject(idSensor, alarmType, alarm).toString());
		httpPost.setEntity(entity);
	    httpPost.setHeader("Content-type", "application/json");
	    httpPost.setHeader("Authorization", "Basic " + getBase64Auth());
		int statusCode = -1;
	    try{
			HttpResponse response = client.execute(httpPost);
			statusCode = response.getStatusLine() .getStatusCode();
			System.out.println("Status code: " + statusCode);
			HttpEntity responseEntity = response.getEntity();
			InputStream inStream = responseEntity.getContent();
			System.out.println("Response: " + Util.convertStreamToString(inStream));
		}catch(Exception e){
			e.printStackTrace();
		}
		return statusCode;
	}
	
	//https://alexdellarocca.atlassian.net/rest/api/2/issue/SEN-1/comment
	//return 201 if OK, -1 if failed
	public static int editOpenedSensorIssue(String issueKey, String alarm) throws UnsupportedEncodingException{
		int statusCode = -1;
		
		HttpClient client = HttpClientBuilder.create().build();
		
		JSONObject commentObject  = buildNewCommentObject(alarm);
	
		HttpPost httpPost = new HttpPost(Constants.JIRA_HOST + "issue/" + issueKey + "/comment");
		StringEntity entity = new StringEntity(commentObject.toString());
		httpPost.setEntity(entity);
	    httpPost.setHeader("Content-type", "application/json");
	    httpPost.setHeader("Authorization", "Basic " + getBase64Auth());
		
	    try{
	    	System.out.println("Running " + Constants.JIRA_HOST + "issue/" + issueKey + "/comment");
			HttpResponse response = client.execute(httpPost);
			statusCode = response.getStatusLine() .getStatusCode();
			System.out.println("Status code: " + statusCode);
			HttpEntity responseEntity = response.getEntity();
			InputStream inStream = responseEntity.getContent();
			String queryResult = Util.convertStreamToString(inStream);
			System.out.println("Response: " + queryResult );
			if(statusCode == 201){
				System.out.println("Update succesfully executed");
				return statusCode;
			}else{
				System.out.println("Update failed");
				return statusCode;

			}
		}catch(Exception e){
			e.printStackTrace();
			return statusCode;
		}
	}
	
	//Return key of task if found otherwise return null
	public static String searchAlreadyOpenedSensorIssue(String idSensor, String alarmType) throws ClientProtocolException, IOException{
		
		String taskKey = null;
		
		HttpClient client = HttpClientBuilder.create().build();
		
		JSONObject queryObject  = buildIssueQueryObject(idSensor, alarmType);
	
		HttpPost httpPost = new HttpPost(Constants.JIRA_HOST + "search");
		StringEntity entity = new StringEntity(queryObject.toString());
		httpPost.setEntity(entity);
	    httpPost.setHeader("Content-type", "application/json");
	    httpPost.setHeader("Authorization", "Basic " + getBase64Auth());
		
	    try{
			HttpResponse response = client.execute(httpPost);
			int statusCode = response.getStatusLine() .getStatusCode();
			System.out.println("Status code: " + statusCode);
			HttpEntity responseEntity = response.getEntity();
			InputStream inStream = responseEntity.getContent();
			String queryResult = Util.convertStreamToString(inStream);
			System.out.println("Response: " + queryResult );
			
			JSONObject queryResultObj = new JSONObject(queryResult);
			if(queryResultObj.getInt("total") == 0){
				System.out.println("No issue founded");
				return taskKey;
			}else{
				System.out.println(queryResultObj.getInt("total") + " issues founded");
				JSONArray issues = queryResultObj.getJSONArray("issues");
				taskKey = issues.getJSONObject(0).getString("key");
				System.out.println("Returning key " + taskKey);
				return taskKey;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return taskKey;
		}
	    
	}
	
	//Build query string in jql
	//{"jql":"project=SEN AND summary~\"Sensor 500 traffic\"AND status=\"Done\"", "fields":["id","key"]}
	private static JSONObject buildIssueQueryObject(String idSensor, String alarmType){
		JSONObject queryObj = new JSONObject();
		StringBuilder jqlQueryStringBuilder = new StringBuilder();
		jqlQueryStringBuilder.append("project=").append(Constants.JIRA_PROJECT_KEY).append(" AND summary~")
		.append("\"Sensor ").append(idSensor).append(" ").append(alarmType).append("\"").append("AND status!=")
		.append("\"Done\"");
		System.out.println("JQL Query generated: " + jqlQueryStringBuilder.toString());
		queryObj.put("jql", jqlQueryStringBuilder.toString());
		JSONArray fieldsArray = new JSONArray();
		fieldsArray.put("id");
		fieldsArray.put("key");
		queryObj.put("fields", fieldsArray);
		System.out.println("Object builded: " + queryObj.toString());
		return queryObj;
	}
	
	//build new issue object
	private static JSONObject buildNewIssueObject(String idSensor, String alarmType, String alarm){
		JSONObject issueObject = new JSONObject();
		JSONObject fieldsObject = new JSONObject();
		JSONObject projectObject = new JSONObject();
		projectObject.put("key", Constants.JIRA_PROJECT_KEY);
		fieldsObject.put("project", projectObject);
		//es: Summary: Sensor 500 Traffic Alarm
		fieldsObject.put("summary", "Sensor " + idSensor + " " + alarmType + " Alarm");
		fieldsObject.put("description", alarm);
		JSONObject issueTypeObject = new JSONObject();
		issueTypeObject.put("name", "Bug");
		fieldsObject.put("issuetype", issueTypeObject);
		issueObject.put("fields", fieldsObject);
		System.out.println("Object builded: " +issueObject.toString());
		return issueObject;
	}
	
	private static JSONObject buildNewCommentObject(String alarm){
		JSONObject commentObject = new JSONObject();
		//TODO build real object and add date
		commentObject.put("body", "New alarm verified ");
		return commentObject;
	}
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
		//openNewSensorIssue("503", "traffic", "alarm");
		//buildNewIssueObject("502", "traffic", "alarm");
		//buildIssueQueryObject("503", "Traffic");
		String key = searchAlreadyOpenedSensorIssue("501", "Traffic");
		if(key != null){
			editOpenedSensorIssue(key, "NEW UPDATE");
		}
	}

}
