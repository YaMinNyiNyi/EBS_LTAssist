package org.sap.model;

public class Driver {
	/*
	 * <d:NRIC m:type="Edm.String">S9303496D</d:NRIC>
<d:Name m:type="Edm.String">Hu Xin</d:Name>
<d:Address m:type="Edm.String">Block 85 Bishan Street 22 #12-120</d:Address>
<d:plateNumber.plateNumber m:type="Edm.String">PA 1234 Y</d:plateNumber.plateNumber>
<d:PhoneNumber m:type="Edm.Int32">88269240</d:PhoneNumber>
	 */
	private String name, address, plateNumber, phoneNumber, nric;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	public String getPlateNumber() {
		return plateNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setNRIC(String nric) {
		this.nric = nric;
	}
	
	public String getNRIC() {
		return nric;
	}
	

}
