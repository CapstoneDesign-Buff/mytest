package com.example.myapplication.groupsalgo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Utils {
//	public static Map<Integer,String> stopMap = new HashMap<Integer, String>();
	public static Map<Integer,BusStop> mapBusStopInfo = new HashMap<Integer, BusStop>();
	
	//정류장 번호와 정류장 이름 정보 로딩
	public static void parseBusInfo(String busInfoFilePath) {
		try {
			InputStreamReader read = new InputStreamReader(
					new FileInputStream(busInfoFilePath), "UTF-8");
			
			BufferedReader reader = new BufferedReader(read);
			String header = reader.readLine();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				
				BusStop stop = new BusStop(line);
				mapBusStopInfo.put(stop.getBusStopNum(), stop);
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//정수(int) 정류장 번호로 정류장 이름 체크
	public static String getStopName(int stopNum) {
		return mapBusStopInfo.get(stopNum).getBusStopName().trim();
	}

	public static int getStopNumber(String stopName)	{
		for(int stopNum : mapBusStopInfo.keySet())	{
//			System.out.println(stopName + " vs " + mapBusStopInfo.get(stopNum).getBusStopName());
			if(stopName.trim().equals(mapBusStopInfo.get(stopNum).getBusStopName().trim()))	{
				return stopNum;
			}
		}
		return -1;
	}

	//int 시간 -> string(HH:MM)으로 변경
	public static String transformTime(int sec)	{
		String time = (int)sec / 3600 + ":";
		long minute = ((int)sec % 3600) / 60;
		
		if(0<= minute && minute <= 9)	{
			time += "0" + minute;
		}
		else	{
			time += minute;
		}

		return time;
	}
	
	
	//두 시간 사이의 차 계산
	public static String calcDiffTime(int st, int ed)	{
		int diffTime = ed - st;
		String time = (int)diffTime / 3600 + ":";
		long minute = ((int)diffTime % 3600) / 60;
		
		if(0<= minute && minute <= 9)	{
			time += "0" + minute;
		}
		else	{
			time += minute;
		}

		return time;
	}


}
