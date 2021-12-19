package com.example.myapplication.groupsalgo;

public class Point {
	public static final int NUM_OF_STOPS = 44;
//	public static final int BUS_INTERVAL = 30*60;
	public static final int STOP_INTERVAL = 3*60;	//정류장마다 3분 소요된다고 가정 44*3분 == 한바퀴: 132 
	public static final int WAIT_THRESHOLD = 5*60;
	
	public int stPos, edPos;
	public int depTime;
	private int expArrivalTime;
	private String date;

//	public Point(int x, int y, int z) {
//		this.stPos = x;
//		this.edPos = y;
//		this.depTime = z;
//		
//		// 5 - 3 ==> 2 * STOP_INTERVAL
//		expArrivalTime = this.depTime + ((this.edPos - this.stPos) * STOP_INTERVAL); 
//	}
	
	public Point(int x, int y, String z, String date) {
		this.stPos = x;
		this.edPos = y;
		
		String[] time = z.split(":");
		this.depTime = (Integer.parseInt(time[0]) * 3600) + Integer.parseInt(time[1]) * 60;
		
		// 5 - 3 ==> 2 * STOP_INTERVAL
		expArrivalTime = this.depTime + ((this.edPos - this.stPos) * STOP_INTERVAL);
		
		this.date = date;
	}

	public int getStPos() {
		return stPos;
	}

	public int getEdPos() {
		return edPos;
	}

	public int getDepTime() {
		return depTime;
	}
	
	public int getExpArrivalTime() {
		return expArrivalTime;
	}

	public String getDate() {
		return date;
	}


	public int calcExpectedArrivalTime(int nextStPos) {
		if(nextStPos == this.stPos)	{
			return this.depTime + STOP_INTERVAL;	//5분 waiting
		}
		else	{
			return this.depTime + ((nextStPos - this.stPos) * STOP_INTERVAL);
		}
	}

	public String toString(){
//		return "#" + date + "# ("+ (int)stPos + "," + (int)edPos + "," 
//					+ formatTime(depTime) 
//					+ ",{" + formatTime(expArrivalTime) + "})";

//		return "#" + date + "# ("+ Utils.getStopName(stPos) + "," + Utils.getStopName(edPos) + "," 
//		+ Utils.transformTime(depTime) 
//		+ ",{" + Utils.transformTime(expArrivalTime) + "})";

		return "("+Utils.getStopName(stPos) + "," + Utils.getStopName(edPos) + "," 
		+ Utils.transformTime(depTime) 
		+ ",{" + Utils.transformTime(expArrivalTime) + "})";
	}

}