package org.sap.model;

public class Offence {

	private String location, timestamp, plateNumber, name, offenceType;

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setDriverName(String name) {
		this.name = name;
	}

	public String getDriverName() {
		return name;
	}

	public void setOffenceType(String offenceType) {
		this.offenceType = offenceType;
	}

	public String getOffenceType() {
		return offenceType;
	}

}
