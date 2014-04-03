/*
 * Code inspired from Lukencode: see "http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/"
 */

package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import rest.RestClient.RequestMethod;
import activities.MainActivity;
import android.os.AsyncTask;

public class RestClient extends AsyncTask<Void, Void, Void>{

	private ArrayList <NameValuePair> params;
	private ArrayList <NameValuePair> headers;

	private String url;

	private int responseCode;
	private String message;

	private String response;
	private RequestMethod requestType;
	
	private MainActivity mainActivity;

	public enum RequestMethod {
		GET, POST
	}

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RestClient(String url, MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value)
	{
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value)
	{
		headers.add(new BasicNameValuePair(name, value));
	}

	private void Execute(RequestMethod method) throws Exception
	{
		switch(method) {
		case GET:
		{
			//add parameters
			String combinedParams = "";
			if(!params.isEmpty()){
				for(NameValuePair p : params)
				{
					combinedParams += "/" + URLEncoder.encode(p.getValue(),"UTF-8");
				}
			}

			System.out.println(url + combinedParams);
			
			HttpGet request = new HttpGet(url + combinedParams);

			//add headers
			for(NameValuePair h : headers)
			{
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, url);
			break;
		}
		case POST:
		{
			HttpPost request = new HttpPost(url);

			//add headers
			for(NameValuePair h : headers)
			{
				request.addHeader(h.getName(), h.getValue());
			}

			if(!params.isEmpty()){
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(HttpUriRequest request, String url)
	{
		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				this.mainActivity.notifyResult(response, this.requestType);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e)  {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (this.requestType == RequestMethod.GET)
			try {
				this.Execute(RequestMethod.GET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else if (this.requestType == RequestMethod.POST)
			try {
				this.Execute(RequestMethod.POST);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	public void setRequestType(RequestMethod type) {
		this.requestType = type;	
	}

}
