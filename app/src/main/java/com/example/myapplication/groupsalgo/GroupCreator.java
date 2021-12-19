package com.example.myapplication.groupsalgo;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class GroupCreator {

	public static void main(String[] args) {
		
		//https://www.data.go.kr/data/15041828/fileData.do
		//read bus info

		String busInfoFilePath = "./data/busInfo.csv";
		Utils.parseBusInfo(busInfoFilePath);
		
		System.out.println("total Dist:" + BusStop.totalDist);
		
		//read stops name
//		String stopNamesFilePath = "./data/stopsName.csv";
//		Utils.parseBusStop(stopNamesFilePath);
		
//		String pointsFilePath = "./data/sample.csv";
		String pointsFilePath = "./data/happyCase.csv";
//		Grouping g = new Grouping(pointsFilePath);
		
		DateSpliter ds = new DateSpliter(pointsFilePath);
//		2021-10-17, 333
//		2021-10-15, 324
//		2021-10-16, 343
		Vector<Grouping> busScheduler = new Vector<Grouping>();
		for(String dateKery : ds.getPointsByDate().keySet())	{
			System.out.println(dateKery + ", " + ds.getPointsByDate().get(dateKery).size());
			Grouping temp = new Grouping(ds.getPointsByDate().get(dateKery));
			
			busScheduler.add(temp);
		}
		
		//add new reservation
//		Scanner in = new Scanner(System.in);
//		System.out.println("Enter a reservation(stPos,edPos,time(HH:MM):");
//		int stPos = Integer.parseInt(in.next());
//		int edPos = Integer.parseInt(in.next());
//		String time = in.next();
//		String date = in.next();
//		Point newPt = new Point(stPos,  edPos, time, date);
//		
////		(2021-10-16#1,2,16:45,{16:50}) Group57에 추가 1 3 16:45 2021-10-16
//		//같은 date를 찾아서 add point
//		for(Grouping g : busScheduler)	{
//			if(g.getDate().equalsIgnoreCase(date))	{
//				g.autoGenerateGroup(newPt);
//			}
//		}
//		in.close();
		
		
		
//		for(String key: g.getCustomGroup().keySet())	{
		for(Grouping g : busScheduler)	{
			System.out.println("%" + g.getDate() + "%");
			for(int i = 0; i < g.getCustomGroup().size(); i++)	{
				String key = "Group" + i;
				System.out.print((i+1)+"회차:");
				
				//print groups
				System.out.println(g.getGroups(key));
				System.out.println("\t-총 승객수: " + g.getGroups(key).size() + "명");
				System.out.println("\t-운행 시간: " + g.predictBusRunTime(key));
				//print path
				System.out.println("\t-버스 운행경로: " + g.getPath(key)+"\n");
			}
		}
		
		
	}

}
