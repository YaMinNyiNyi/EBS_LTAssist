package org.sap.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.sap.service.ODataService;

public class ODataController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, JSONException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void get(HttpServletRequest req, HttpServletResponse resp) throws IOException, JSONException, SQLException {
		// TODO Auto-generated method stub
		ODataService.readDriverInfoFromOdata();
	}

}
