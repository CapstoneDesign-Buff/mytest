package com.example.myapplication.groupsalgo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DateSpliter {
	public Map<String, Vector<Point>> pointsByDate = new HashMap<String, Vector<Point>>();
	
	public DateSpliter(String pointsFilePath)	{
		List<Point> points = new ArrayList<Point>();
		try {
			InputStreamReader read = new InputStreamReader(
					new FileInputStream(pointsFilePath), "UTF-8");
			
			BufferedReader reader = new BufferedReader(read);
			String header = reader.readLine();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				Point pt = getPointByLine(line);
				String dateKey = pt.getDate();
				
				
				Vector<Point> temp = null;
				if(pointsByDate.containsKey(dateKey))	{ //exist dete key
					temp = pointsByDate.get(dateKey);
				}
				else	{
					temp = new Vector<Point>();
				}
				temp.add(pt);
				
				pointsByDate.put(dateKey, temp);
//				points.add(getPointByLine(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public Map<String, Vector<Point>> getPointsByDate() {
		return pointsByDate;
	}



	public void setPointsByDate(Map<String, Vector<Point>> pointsByDate) {
		this.pointsByDate = pointsByDate;
	}



	private Point getPointByLine(String line) {
		String[] xyz = line.split(",");
		return new Point(Integer.parseInt(xyz[0]),
				Integer.parseInt(xyz[1]), xyz[2], xyz[3]);	//double, double, string(13:00)
	}

}
