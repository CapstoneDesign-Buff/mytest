package com.example.myapplication.groupsalgo;

public class BusStop {
	String company;
	String companyID;
	String routeID;
	String routeNum;	//330-1
	
	int busStopNum;	//정류장 번호 1,2,3,...
	
	String busStopID1;
	String busStopID2;
	String busStopName;
	
	int dist;
	double latitude;
	double longitude;
	double TM_X;
	double TM_Y;
	
	public static int totalDist;
	
	
	public BusStop(String line) {
		//company,companyID,routeID,routeNum,busStopNum(4),busStopID1,busStopID2,busStopName(7),dist,latitude,longitude,TM_X,TM_Y
		String[] data = line.split(",");
		company = data[0];
		companyID = data[1];
		routeID = data[2];
		routeNum = data[3];
		
		busStopNum = Integer.parseInt(data[4]);
		
		busStopID1 = data[5];
		busStopID2 = data[6];
		busStopName = data[7];
		try {
			dist = Integer.parseInt(data[8]);
		}catch(Exception e)	{
			dist = 0;
		}
		
		latitude = Double.parseDouble(data[9]);
		longitude = Double.parseDouble(data[10]);
		TM_X = Double.parseDouble(data[11]);
		TM_Y = Double.parseDouble(data[12]);
		
		
		totalDist += dist;
	}

	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public String getRouteID() {
		return routeID;
	}
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}
	public String getRouteNum() {
		return routeNum;
	}
	public void setRouteNum(String routeNum) {
		this.routeNum = routeNum;
	}
	public int getBusStopNum() {
		return busStopNum;
	}
	public void setBusStopNum(int busStopNum) {
		this.busStopNum = busStopNum;
	}
	public String getBusStopID1() {
		return busStopID1;
	}
	public void setBusStopID1(String busStopID1) {
		this.busStopID1 = busStopID1;
	}
	public String getBusStopID2() {
		return busStopID2;
	}
	public void setBusStopID2(String busStopID2) {
		this.busStopID2 = busStopID2;
	}
	public String getBusStopName() {
		return busStopName;
	}
	public void setBusStopName(String busStopName) {
		this.busStopName = busStopName;
	}
	public int getDist() {
		return dist;
	}
	public void setDist(int dist) {
		this.dist = dist;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getTM_X() {
		return TM_X;
	}
	public void setTM_X(double tM_X) {
		TM_X = tM_X;
	}
	public double getTM_Y() {
		return TM_Y;
	}
	public void setTM_Y(double tM_Y) {
		TM_Y = tM_Y;
	}
	

	
}
