package org.sap.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataDeltaFeed;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sap.model.Driver;
import org.sap.model.Offence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSetPart;

public class ODataService {
	
	public static final String HTTP_METHOD_PUT = "PUT";
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	public static final String HTTP_METHOD_PATCH = "PATCH";
	private static final String HTTP_METHOD_DELETE = "DELETE";

	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HTTP_HEADER_ACCEPT = "Accept";

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String METADATA = "$metadata";
	public static final String SEPARATOR = "/";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String CSRF_TOKEN_HEADER = "X-CSRF-Token";
	public static final String CSRF_TOKEN_FETCH = "Fetch";
	public static final String C4C_TENANT = "C4C_TENANT";
	
	public static ArrayList<Driver> readDriverInfoFromOdata() throws IOException, JSONException {

		ArrayList<Driver> drivers = new ArrayList<>();
		String tokenURI = "https://smartcityg8b4e9dc9.jp1.hana.ondemand.com/ltassist/services/ltassist.xsodata/LTA?$format=json";
		URL urlToken = new URL(tokenURI);
		HttpURLConnection request = (HttpURLConnection) urlToken.openConnection();
		request.setRequestMethod("GET");
		request.setDoInput(true);
		request.setRequestProperty("Authorization", "Basic TFRBU1NJU1Q6TFRBc3Npc3QyMDE4");
		request.setRequestProperty("Content-Type", "application/json");
		request.setRequestProperty("cache-control", "no-cache");
		

		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		
		JSONObject odataJson = new JSONObject(response.toString());
		JSONObject dJson = odataJson.getJSONObject("d");
		JSONArray resultsJArray = dJson.getJSONArray("results");
		
		for (int i = 0; i < resultsJArray.length(); i++) {
			JSONObject resultJson = resultsJArray.getJSONObject(i);
			String nric = resultJson.getString("NRIC");
			String name = resultJson.getString("Name");
			String address = resultJson.getString("Address");
			String plateNumber = resultJson.getString("plateNumber.plateNumber");
			String phoneNumber = "" + resultJson.getInt("PhoneNumber");
			
			Driver driver = new Driver();
			driver.setNRIC(nric);
			driver.setName(name);
			driver.setAddress(address);
			driver.setPlateNumber(plateNumber);
			driver.setPhoneNumber(phoneNumber);
			drivers.add(driver);
		}
		
		return drivers;

	}
	
	public static ArrayList<Offence> readOffencesInfoFromOdata() throws IOException, JSONException {

		ArrayList<Offence> offences = new ArrayList<>();
		String tokenURI = "https://smartcityg8b4e9dc9.jp1.hana.ondemand.com/ltassist/services/ltassist.xsodata/https://smartcityg8b4e9dc9.jp1.hana.ondemand.com/ltassist/services/ltassist.xsodata/OffenceCommitted?$format=json";
		URL urlToken = new URL(tokenURI);
		HttpURLConnection request = (HttpURLConnection) urlToken.openConnection();
		request.setRequestMethod("GET");
		request.setDoInput(true);
		request.setRequestProperty("Authorization", "Basic TFRBU1NJU1Q6TFRBc3Npc3QyMDE4");
		request.setRequestProperty("Content-Type", "application/json");
		request.setRequestProperty("cache-control", "no-cache");
		

		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		
		JSONObject odataJson = new JSONObject(response.toString());
		JSONObject dJson = odataJson.getJSONObject("d");
		JSONArray resultsJArray = dJson.getJSONArray("results");
		
		for (int i = 0; i < resultsJArray.length(); i++) {
			JSONObject resultJson = resultsJArray.getJSONObject(i);
			String plateNumber = resultJson.getString("plateNumber");
			String timestamp = resultJson.getString("Timestamp");
			String location = resultJson.getString("Location");
			
			Offence offence = new Offence();
			offence.setPlateNumber(plateNumber);
			offence.setTimestamp(timestamp);
			offence.setLocation(location);
			
			ArrayList<Driver> drivers = readDriverInfoFromOdata();
			for(int j=0; j < drivers.size(); j++) {
				Driver driver = drivers.get(j);
				if(plateNumber.equals(driver.getPlateNumber())) {
					offence.setDriverName(driver.getName());
					j = drivers.size();
				}
			}
			
			offences.add(offence);
			
		}
		
		return offences;

	}
	
	public void createOffenceRecord(String plateNumber, String timestamp, String location) {
		List<BatchPart> batchParts = new ArrayList<>();
		BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
		String contentId = UUID.randomUUID().toString();
		
		Map<String, String> changeSetHeaders = new HashMap<String, String>();
		changeSetHeaders.put(HTTP_HEADER_CONTENT_TYPE, "application/json");
		changeSetHeaders.put("Content-ID", contentId);
		changeSetHeaders.put("Accept", APPLICATION_JSON);
		
		String uriTicket = new StringBuilder("ServiceRequestCollection")
				.toString();

		BatchChangeSetPart changeRequestTicket = BatchChangeSetPart
				.method(HTTP_METHOD_POST).uri(uriTicket)
				.body(serializeOffenceDeepInsert(plateNumber, timestamp, location))
				.headers(changeSetHeaders).contentId(contentId).build();
		
		
	}
	
	
	public static String  serializeOffenceDeepInsert(String plateNumber, String timestamp, String location) {
		//Input fields 
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("plateNumber", plateNumber);
		prop.put("Timestamp", timestamp);
		prop.put("Location", location);
		
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(prop);
	}
	
}
