package org.sap.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class BaseController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> responseData = new HashMap<String, Object>();

	public abstract void post(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, JSONException, SQLException;

	public abstract void get(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, JSONException, SQLException;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			// setAccessControlHeaders(resp);
			
			post(req, resp);

			String payload = new JSONObject(responseData).toString();
			resp.addHeader("Content-Type", "application/json");
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("application/json");
			resp.setContentLength(payload.getBytes("utf-8").length);
			resp.getWriter().write(payload);
			responseData.clear();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {

			// setAccessControlHeaders(resp);

			get(req, resp);
			String payload = new JSONObject(responseData).toString();
			resp.addHeader("Content-Type", "application/json");

			resp.setCharacterEncoding("utf-8");
			resp.setContentType("application/json");
			resp.setContentLength(payload.getBytes("utf-8").length);
			resp.getWriter().write(payload);
			responseData.clear();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	// --Response Helper--

	public void putDataWithKey(String key, Object value) {
		responseData.put(key, value);
	}

	// --Request Helper--

	public String getUriLastPath(HttpServletRequest req) {
		String uri = req.getRequestURI().toString();
		return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
	}

	public JSONObject getJsonRequestObj(HttpServletRequest req) throws IOException, JSONException {

		InputStream inputStream = req.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String input, output = "";

		while ((input = reader.readLine()) != null) {
			output += input + "\n";
		}

		reader.close();
		return new JSONObject(output);
	}


}
