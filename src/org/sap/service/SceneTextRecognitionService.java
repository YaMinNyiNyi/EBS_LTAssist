package org.sap.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SceneTextRecognitionService {

	public static void readFromOdata() throws IOException {
		// https://smartcityg8b4e9dc9.jp1.hana.ondemand.com/ltassist/services/ltassist.xsodata/LTA
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

	}

	public static String getAuthorizationHeader() {
		String temp = new StringBuilder("LTASSIST").append(":").append("LTAssist2018").toString();
		String result = "Basic " + new String(Base64.encodeBase64(temp.getBytes()));
		System.out.println("Result: " + result);
		return result;
	}

	public static String generateToken() throws IOException, JSONException {

		String token = "";
		String tokenURI = "https://pdl.authentication.us10.hana.ondemand.com/oauth/token?grant_type=client_credentials";
		URL urlToken = new URL(tokenURI);
		HttpURLConnection request = (HttpURLConnection) urlToken.openConnection();
		request.setDoInput(true);
		request.setRequestProperty("Authorization",
				"Basic c2ItNDkyZTJmNTItMmJhNi00ZjAyLTk2MDAtZWViYTM3YmYyYjAwIWIxMDM5fGZvdW5kYXRpb24tc3RkLW1sZnByZXByb2R1Y3Rpb24hYjE2NDpGS0FCMlEyK3EveTVFNDEvOGVyYUtpZjNyR2c9");
		request.setRequestProperty("Content-Type", "application/json");
		request.setRequestMethod("POST");

		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());

		JSONObject jsonObj = new JSONObject(response.toString());

		System.out.println("JSON Object: " + jsonObj.toString());
		token = "Bearer " + jsonObj.getString("access_token");
		System.out.println("Access Token: " + token);

		return token;
	}

	public static String readLicencePlate(String token, byte[] contentByte, String fileName) {
		DataOutputStream dataOut = null;
		BufferedReader in = null;
		ArrayList<String> texts = new ArrayList<>();
		/*
		 * BufferedReader reader = null; String content = ""; File file = new
		 * File("C:\\Users\\Ya Min Nyi Nyi\\Desktop\\bus.jpg");
		 * 
		 * BufferedImage bufferimage; try { bufferimage = ImageIO.read(file);
		 * ByteArrayOutputStream output = new ByteArrayOutputStream();
		 * ImageIO.write(bufferimage, "jpg", output ); contentByte =
		 * output.toByteArray(); } catch (IOException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); }
		 * 
		 * FileOutputStream stream; try { stream = new
		 * FileOutputStream("C:\\Users\\Ya Min Nyi Nyi\\Desktop\\bus2.jpg");
		 * stream.write(contentByte); stream.close(); } catch (Exception e1) { // TODO
		 * Auto-generated catch block e1.printStackTrace(); }
		 * 
		 * content = new String(contentByte);
		 */

		try {

			// API endpoint for API sandbox
			String url = "https://mlfproduction-scene-text-recognition.cfapps.us10.hana.ondemand.com/api/v2/image/scene-text-recognition";

			// Create URL and set up connection
			URL urlObj = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
			// setting request method
			connection.setRequestMethod("POST");

			// adding headers
			connection.setRequestProperty("content-type", "multipart/form-data; boundary=---011000010111000001101001");
			// API Key for API Sandbox
			connection.setRequestProperty("Authorization", token);

			connection.setDoInput(true);

			// sending POST request
			connection.setDoOutput(true);
			dataOut = new DataOutputStream(connection.getOutputStream());
			dataOut.writeBytes(
					"-----011000010111000001101001\r\nContent-Disposition: form-data; name=\"files\"; filename=\""
							+ fileName + "\"\r\nContent-Type: image/jpeg\r\n\r\n");
			dataOut.write(contentByte);
			dataOut.writeBytes("\r\n-----011000010111000001101001--");
			dataOut.flush();

			System.out.println(connection.getResponseCode());
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			// printing response
			System.out.println(response.toString());
			JSONObject imageResultJson = new JSONObject(response.toString());
			JSONArray predictionsJson = imageResultJson.getJSONArray("predictions");
			for (int i = 0; i < predictionsJson.length(); i++) {
				JSONObject predictionJson = predictionsJson.getJSONObject(i);
				JSONArray resultsJson = predictionJson.getJSONArray("results");
				for (int j = 0; j < resultsJson.length(); j++) {
					String text = resultsJson.getJSONObject(j).getString("text");

					texts.add(text);
					System.out.println("Texts: ");
					System.out.println(text);
				}
			}

			String plate = "";
			if (texts.size() == 1) {
				plate = texts.get(0);
				System.out.println(plate);
				if (plate.length() == 8
						&& (!(plate.startsWith("SBS") || plate.startsWith("SMB") || plate.startsWith("QX")))) {
					return plate;
				}
			} else {
				String letters = "";
				String nums = "";
				for (int k = 0; k < texts.size(); k++) {

					plate = texts.get(k);
					System.out.println(plate);
					if (!(plate.equals("SBS") || plate.equals("SMB") || plate.equals("QX"))
							&& (plate.length() == 2 || plate.length() == 3)) {
						letters = plate;
					} else {
						if (plate.length() == 5) {
							if (plate.charAt(0) >= '0' && plate.charAt(0) <= '9') {
								nums = plate;
							}
						}
					}
				}
				plate = letters + nums;
				return plate;
			}

		} catch (Exception e) {
			// do something with exception
			// texts = new ArrayList<>();
			// texts.add("Error found by LTAssist T3: " + e.getLocalizedMessage());
			return ("Error found by LTAssist T3:" + e.getLocalizedMessage());
		} finally {
			try {
				if (dataOut != null) {
					dataOut.close();
				}
				if (in != null) {
					in.close();
				}

			} catch (IOException e) {
				// texts = new ArrayList<>();
				// texts.add("Error found by LTAssist T3: " + e.getLocalizedMessage());
				return ("Error found by LTAssist T3:" + e.getLocalizedMessage());
			}
		}
		return "no vehicle found";
	}

}