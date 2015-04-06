package com.laudandjolynn.mytv.model;

/**
 * 电视节目表
 * 
 * @author Laud
 * 
 */
public class ProgramTable {
	private long id;
	private int station;
	private String stationName;
	private String program;
	private String airDate;
	private String airTime;
	private int week;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getAirDate() {
		return airDate;
	}

	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}

	public String getAirTime() {
		return airTime;
	}

	public void setAirTime(String airTime) {
		this.airTime = airTime;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "ProgramTable [id=" + id + ", station=" + station
				+ ", stationName=" + stationName + ", program=" + program
				+ ", airDate=" + airDate + ", airTime=" + airTime + ", week="
				+ week + "]";
	}
}
