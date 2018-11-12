package org.sap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.sap.service.SceneTextRecognitionService;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;



public class SceneTextRecognitionController extends BaseController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void post(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		//JSONObject jsonObj = new JSONObject();
		String token = "";
		try {
			MultipartFormDataRequest multiPartRequest = new MultipartFormDataRequest(req);
			Hashtable files = multiPartRequest.getFiles();		
			
			// Get the photo from raspberry pi
			UploadFile	uploadedFile = (UploadFile) files.get("files");
			if(uploadedFile == null) {
				putDataWithKey("message", "Unsuccessful");
				putDataWithKey("texts", "No file found");
			} else {
				byte[]fileData = uploadedFile.getData();
				
				//Generate token from SAP API
				token = SceneTextRecognitionService.generateToken();
							
				System.out.println("Token: " + token);
				String fileName = uploadedFile.getFileName();
				System.out.println("FileName: " + fileName);
				
			    //Retrieve texts from photo using SAP scene text recognition
				String plate = SceneTextRecognitionService.readLicencePlate(token, fileData, fileName);
				if(plate.startsWith("Error found by LTAssist T3:") || plate.startsWith("no vehicle found")) {
					putDataWithKey("message", "Unsuccessful");
					putDataWithKey("texts", plate);
				} else {
					putDataWithKey("texts", plate);
					putDataWithKey("message", "Successful");
				}
			}
			
			
		} catch (UploadException | IOException | JSONException e) {
			// TODO Auto-generated catch block
			putDataWithKey("message", "Unsuccessful");
			putDataWithKey("texts", e.getLocalizedMessage());
		}
	
	}


	@Override
	public void get(HttpServletRequest req, HttpServletResponse resp) throws IOException  {
		
		
	}
}