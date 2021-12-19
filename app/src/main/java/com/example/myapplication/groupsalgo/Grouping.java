package com.example.myapplication.groupsalgo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Grouping {
	private final int START_TIME_BUS = 9;
	private final int RUNTIME_BUS = 8;
	private final int MAX_NUM_BUS = RUNTIME_BUS * 2; // 8h * 2 bus
	private Map<String, Vector<Point>> customGroup;
	private String date;
	
	public Grouping(String pointsFilePath)	{
		customGroup = new HashMap<String, Vector<Point>>();
		
		//차량을 총 운행시간 * 2 (30분당 1대씩 운행)
		//매 정각에 출발하는 차를 만들어두고 고객의 예약에 따라 출발 시간을 조정.
		for( int i =0; i < MAX_NUM_BUS; i++)	{
			String key = "Group"+i;
			int onTime = START_TIME_BUS * 3600 + i * 1800;
			Point defaultBus = new Point(1, Point.NUM_OF_STOPS, Utils.transformTime(onTime), date);
			Vector<Point> vec = new Vector<Point>();
			vec.add(defaultBus);
			customGroup.put(key, vec);
		}
		
		List<Point> points = new ArrayList<Point>();
		try {
			InputStreamReader read = new InputStreamReader(
					new FileInputStream(pointsFilePath), "UTF-8");
			BufferedReader reader = new BufferedReader(read);
			String header = reader.readLine();
			String line = null;
			while ((line = reader.readLine()) != null) {
				points.add(getPointByLine(line));
			}
				
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//sort by time
		Collections.sort(points, (a, b) -> a.getDepTime() - b.getDepTime());
		this.date = points.get(0).getDate();	//set date
//		System.out.println(points);
		
		//to check the new point which is located the group.
//		locatePointToGroup(points);
		
		for(Point pt : points)	{
			autoGenerateGroup(pt);
		}
	}
	
	public Grouping(List<Point> points)	{
		customGroup = new HashMap<String, Vector<Point>>();
		
		//차량을 총 운행시간 * 2 (30분당 1대씩 운행)
		//매 정각에 출발하는 차를 만들어두고 고객의 예약에 따라 출발 시간을 조정.
//		for( int i =0; i < MAX_NUM_BUS; i++)	{
//			String key = "Group"+i;
//			int onTime = START_TIME_BUS * 3600 + i * 1800;
//			Point defaultBus = new Point(1, Point.NUM_OF_STOPS, Utils.transformTime(onTime), date);
//			Vector<Point> vec = new Vector<Point>();
//			vec.add(defaultBus);
//			customGroup.put(key, vec);
//		}
//		
		//sort by time
		Collections.sort(points, (a, b) -> a.getDepTime() - b.getDepTime());
		
		this.date = points.get(0).getDate();	//set date
		
		for(Point pt : points)	{
			autoGenerateGroup(pt);
		}
	}
	
	
	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------
	
	public void autoGenerateGroup(Point pointByLine) {
		//그룹이 비어있으면 새로 생성
		if(customGroup.size() == 0)	{
//			System.out.println("???????????");
			String key = "Group0";
			Vector<Point> temp = new Vector<Point>();
			temp.add(pointByLine);
			customGroup.put(key, temp);
		}
		//그룹이 존재한다면 조건을 확인해서 기존 그룹 추가 혹은 새로운 그룹 추
		else	{
			//기존 그룹에 추가
			if(checkAddGroup(pointByLine))	{
				return ;
			}
			//새로운 그룹 생성
			else {
//				System.err.println("try to make a new group");
//				System.out.println("기존에 그룹에 추가할 수 없습니다.. 예약을 새로해주세요.");
				String key = "Group" + customGroup.size();
				Vector<Point> temp = new Vector<Point>();
				temp.add(pointByLine);
				customGroup.put(key, temp);
			}
		}
	}

	private boolean checkAddGroup(Point newPt) {
		//그룹을 하나씩 순회하면서 그안에 모든 고객과 비교
//		for(String key : customGroup.keySet())	{	//group 순서대로 처리하려고 주석처리
		for(int i = 0; i < customGroup.size(); i++)	{
			String key = "Group" + i;
			boolean isAddable = true;
			
			for(Point prevPt : customGroup.get(key))	{
				//기존 고객보다 새로운 고객의 출발지가 앞선 경우
				if(prevPt.getStPos() > newPt.getStPos())	{
					//출발지점의 간격차이 * 최소 시간의 interval이 반드시 필요
					//새로운 고객의 시작 지점과 기존 고객의 시작지점 사이의 간격 * 이동시간이 기존 고객의 출발시간보다 빨라야함
//					System.out.println("new->prev: "+ newPt + " $ " + prevPt);
//					System.out.println(newPt.calcExpectedArrivalTime(prevPt.getStPos())/3600 +":"+ newPt.calcExpectedArrivalTime(prevPt.getStPos()) % 3600/60);
//					System.out.println(newPt.calcExpectedArrivalTime(prevPt.getStPos()) <= prevPt.getDepTime() 
//							&& prevPt.getDepTime() <= newPt.calcExpectedArrivalTime(prevPt.getStPos()) + Point.WAIT_THRESHOLD);
					
					if(prevPt.getDepTime() == newPt.getDepTime())	{	//이전 고객과 새로운 고객의 출발 시간이 같은 경우. 다른 그룹으로..
						isAddable = false;
					}
					
					if(newPt.calcExpectedArrivalTime(prevPt.getStPos()) <= prevPt.getDepTime() 
							&& prevPt.getDepTime() <= newPt.calcExpectedArrivalTime(prevPt.getStPos()) + Point.WAIT_THRESHOLD)	{
						//처리 없음
					}
					else	{	// 시간내에 다음 출발지점을 못가는 상황.
//						break;
						isAddable = false;
					}
				}
				//기존 고객보다 새로운 고객의 출발지가 뒷노선 번호인 경우
				else if(prevPt.getStPos() < newPt.getStPos())	{
					//기존 고객의 출발지에서 새로운 고객의 출발지까지 이동 시간이 필요.
//					System.out.println("prev->new: "+prevPt + " $ " + newPt);
//					System.out.println(prevPt.calcExpectedArrivalTime(newPt.getStPos())/3600 +":"+ prevPt.calcExpectedArrivalTime(newPt.getStPos()) % 3600/60);
//					System.out.println(prevPt.calcExpectedArrivalTime(newPt.getStPos()) <= newPt.getDepTime()
//							&& newPt.getDepTime() <= prevPt.calcExpectedArrivalTime(newPt.getStPos()) + Point.WAIT_THRESHOLD);
					
					if(prevPt.getDepTime() == newPt.getDepTime())	{	//이전 고객과 새로운 고객의 출발 시간이 같은 경우. 다른 그룹으로..
						isAddable = false;
					}
					
					if(prevPt.calcExpectedArrivalTime(newPt.getStPos()) <= newPt.getDepTime()
							&& newPt.getDepTime() <= prevPt.calcExpectedArrivalTime(newPt.getStPos()) + Point.WAIT_THRESHOLD)	{
						//처리 없음
					}
					else	{
//						break;
						isAddable = false;
					}
				}
				//기존 고객과 출발지가 같은 경우
				else	{
					//최대 탑승 대기시간 5분까지는 허용
					//두 승객의 탑승 시간 차이가 5분보다 작은지 비교 
					int diffTime = Math.abs((prevPt.getDepTime() - newPt.getDepTime()));
					if(diffTime <= Point.WAIT_THRESHOLD)	{
						//처리 없음
					}
					else	{
//						break;
						isAddable = false;
					}
				}
			}
			
			if(isAddable)	{
				Vector<Point> temp = customGroup.get(key);
				temp.add(newPt);
				
				return true;
			}
		}
		
		return false;
	}


	//회차별 버스 노선 계산
	public List<String> getPath(String key) {
		Vector<Point> points = customGroup.get(key);
		Set<Integer> path = new HashSet();
		for(Point pt : points)	{
			path.add(pt.getStPos());
			path.add(pt.getEdPos());
		}

		List<Integer> pathList = new ArrayList<Integer>();
		for(Integer v : path)	{
			pathList.add(v);
		}
		Collections.sort(pathList);

		List<String> stopNamesPath = new Vector<String>();

		for(int stopNum : pathList)	{
			stopNamesPath.add(  Utils.getStopName(stopNum) );
		}

		return stopNamesPath;
	}

	//회차별 고객정보 전달
	public Vector<Point> getGroups(String key) {
		Collections.sort(customGroup.get(key), (a, b) -> a.getDepTime() - b.getDepTime());
		return customGroup.get(key);
	}

	//버스 예상 운행 시간 계산
	public String predictBusRunTime(String groupName)	{
		int stTime = customGroup.get(groupName).get(0).getDepTime();
		int edTime = customGroup.get(groupName).get(customGroup.get(groupName).size()-1).getExpArrivalTime();
		String deptTime = Utils.transformTime( stTime );
		String expArrTime = Utils.transformTime( edTime );
		return deptTime + " ~ " + expArrTime + "(" + Utils.calcDiffTime(stTime, edTime) + ")";  
	}

	private Point getPointByLine(String line) {
		String[] xyz = line.split(",");
		return new Point(Integer.parseInt(xyz[0]),
				Integer.parseInt(xyz[1]), xyz[2],xyz[3]);	//double, double, string(13:00)
	}


	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------


	
	//--------------------------------------------Getter/Setter---------------------------------------------
	//-------------------------------------------------------------------------------------------------------
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Map<String, Vector<Point>> getCustomGroup() {
		return customGroup;
	}

	
	//-------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	//------------------------------------------prev code----------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------
	

//	private void locatePointToGroup(List<Point> points) {
////		System.out.println("??");
//		
//		int nGroups = 0;
//		for(Point pt : points)	{
////			System.out.println(pt);
//			//check make a new group or put a new point in previous group.
//			if(createNewGroup(pt))	{ // make a new group
//				nGroups++;
//				String key = "Group"+nGroups;
//				Vector<Point> temp = new Vector<Point>();
//				temp.add(pt);
//				customGroup.put(key, temp);
//			}
//		}
//		
//	}


//	private boolean createNewGroup(Point newPt) {
//		String selectedGroup = "";
//		if(customGroup.keySet().size() == 0)	
//			return true;
//		
//		for(String key : customGroup.keySet())	{
//			int currentBusDepTime = customGroup.get(key).get(0).getDepTime();
//			if(currentBusDepTime + Point.BUS_INTERVAL < newPt.getDepTime())	{
//				continue;
//			}
//				
//			for(Point prevPt : customGroup.get(key))	{
//				
//				//기존 그룹의 노드에서 예상 도착시간보다 새로운 노드의 출발시간이 앞선다면 그룹에 추가
//				if( prevPt.getExpArrivalTime() > newPt.getDepTime() )	{
//					
//					if(prevPt.getStPos() < newPt.getStPos())	{
//						selectedGroup = key;
//						customGroup.get(selectedGroup).add(newPt);
//						return false;
//					}
//					else if(prevPt.getStPos() == newPt.getStPos())	{
//						if(prevPt.getDepTime() + Point.STOP_INTERVAL < newPt.getDepTime())	{
//							selectedGroup = key;
//							customGroup.get(selectedGroup).add(newPt);
//							return false;
//						}
//					}
//				}
//				
//				
//				
//				
////				//기존 그룹에 추가하려면 그룹안의 고객의 출발위치보다 다음이어야 함. 시간상으로 정렬되어 있기 때문에
////				//새로운 그룹을 만들려면 기존 그룹의 고객 출발지보다 빨라야 함께 탑승할 수 없음.
////				
////				//첫 시작 고객의 위치와 시작 시간과 버스 한바퀴 회차 남은 시간보다 먼 시간의 고객이라면 같은그룹으로 넣지 않는다.
////				//1번 역부터 10번역까지는 9개의 구간
////				int remainingTimeByInterval = Point.BUS_INTERVAL; //- ((NUM_OF_STOPS-1) - prevPt.getStPos())*STOP_INTERVAL;
////				if(prevPt.getDepTime() + remainingTimeByInterval < newPt.getDepTime())	{
////					continue;
////				}
////				else	{
////					if(prevPt.getStPos() == newPt.getStPos())	{
////						if((prevPt.getDepTime() - newPt.getDepTime()) % 3600 <= Point.STOP_INTERVAL)	{	//같은 출발지인 경우 10분 이내의 열차는 같이 출발
////							selectedGroup = key;
////							customGroup.get(selectedGroup).add(newPt);
////							
////							return false;
////						}
////						else	{
////							return true;
////						}
////					}
////					else if (prevPt.getStPos() < newPt.getStPos())	{		
////						
////						if (!isValidPointIntoGroup(customGroup.get(key), newPt))	{	//30Minutes
////							continue;
////						}
////						
////						selectedGroup = key;
////						customGroup.get(selectedGroup).add(newPt);
////						
////						return false;
////					}
////					
////				}
//			}
//		}
//		
//		//기존 그룹에 추가
////		System.out.println(customGroup.get(selectedGroup));
//		return true;
//		
//	}


	
//	private boolean isValidPointIntoGroup(Vector<Point> vector, Point newPt) {
//		int min = Integer.MAX_VALUE;
//		int max = Integer.MIN_VALUE;
//		
//		if(vector.size() == 1)	{
//			int distance = Math.abs(Math.subtractExact(vector.get(0).getStPos(), newPt.getStPos()));
//			
//			//추가하려는 고객과 기존 고객의 출발지사이의 거리 * 5분씩 최소 필요.
//			if( vector.get(0).getDepTime() + distance * Point.STOP_INTERVAL >= newPt.getDepTime())	{
//				return true;
//			}
//		}
//		else	{
//			for(Point p : vector)	{
//				if(max < p.getDepTime())	{
//					 max = p.getDepTime();
//				}
//				
//				if(min > p.getDepTime())	{
//					min = p.getDepTime();
//				}
//			}
//			
//			// 새로 추가하려는 고객이 기존 그룹의 고객 예약 시간 
//			if(min <= newPt.getDepTime() && newPt.getDepTime() <= max)	{
//				return true;
//			}
//		}
//		
//		return false;
////		return (max-min) % 3600;
//	}
//

	
}
