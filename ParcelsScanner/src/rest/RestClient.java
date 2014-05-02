/*
 * Code inspired by Lukencode: see "http://lukencode.com/2010/04/27/calling-web-services-in-android-using-httpclient/"
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
import activities.ScanActivity;
import android.os.AsyncTask;

public class RestClient extends AsyncTask<Void, Void, Void>{ // Sorte de thread.

	private ArrayList <NameValuePair> params;
	private ArrayList <NameValuePair> headers;

	private String url;

	private int responseCode;
	private String message;

	private String response;
	private RequestMethod requestType;
	
	/**
	 * Une instance de l'activite appelante, de maniere a pouvoir la notifier et lui envoyer les 
	 * resultats de la requete.
	 * @see #executeRequest(HttpUriRequest, String)
	 */
	private MainActivity mainActivity;
	private ScanActivity scanActivity;

	/**
	 * Les types requetes prises en compte.
	 */
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

	/**
	 * Surcharge du constructeur: dans un cas l'activite appelante est mainActivity, dans l'autre scanActivity.
	 * @param url L'URL du serveur
	 * @param mainActivity L'activite appelante, il faut la notifier a la fin de l'execution
	 */
	public RestClient(String url, MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;
		this.scanActivity = null;
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}
	
	/**
	 * Surcharge du constructeur: dans un cas l'activite appelante est mainActivity, dans l'autre scanActivity.
	 * @param url L'URL du serveur
	 * @param scanActivity L'activite appelante, il faut la notifier a la fin de l'execution
	 */
	public RestClient(String url, ScanActivity scanActivity)
	{
		this.scanActivity = scanActivity;
		this.mainActivity = null;
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

	/**
	 * Methode appelee lors de l'execution du thread.
	 * @param method
	 * @throws Exception
	 */
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

			System.out.println(url + combinedParams); // Affichage dans le LogCat de la requete
			
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
				if(this.mainActivity != null) this.mainActivity.notifyResult(response);
				else if (this.scanActivity != null) this.scanActivity.notifyResult(response);

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
