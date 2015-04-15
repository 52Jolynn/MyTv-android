package com.laudandjolynn.mytv.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.caucho.hessian.client.HessianProxyFactory;
import com.laudandjolynn.mytv.Config;
import com.laudandjolynn.mytv.exception.MyTvException;
import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午10:04:18
 * @copyright: 旦旦游广州工作室
 */
public class HessianImpl implements DataService {
	private final static String url = "http://" + Config.NET_CONFIG.getIp()
			+ ":" + Config.NET_CONFIG.getPort() + "/epg";

	@Override
	public String[] getTvStationClassify() {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String classify = tv.getTvStationClassify();
		JSONArray array;
		try {
			array = new JSONArray(classify);
		} catch (JSONException e) {
			throw new MyTvException("invalid data of tv station classify.", e);
		}
		int length = array == null ? 0 : array.length();
		if (length <= 0) {
			throw new MyTvException("invalid data of tv station classify.");
		}
		String[] classifyArray = new String[length];
		for (int i = 0; i < length; i++) {
			try {
				classifyArray[i] = array.getString(i);
			} catch (JSONException e) {
				throw new MyTvException("invalid data of tv station classify.",
						e);
			}
		}
		return classifyArray;
	}

	@Override
	public List<TvStation> getTvStationByClassify(String classify) {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String allTvStation = tv.getTvStationByClassify(classify);
		JSONArray array;
		try {
			array = new JSONArray(allTvStation);
		} catch (JSONException e) {
			throw new MyTvException("invalid data of tv station.", e);
		}
		int length = array == null ? 0 : array.length();
		if (length <= 0) {
			throw new MyTvException("invalid data of tv station.");
		}

		List<TvStation> stations = new ArrayList<TvStation>(length);
		for (int i = 0; i < length; i++) {
			JSONObject json = array.optJSONObject(i);
			TvStation station = new TvStation();
			station.setChannel(json.optString("channel", ""));
			station.setCity(json.optString("city"));
			station.setClassify(json.optString("classify"));
			station.setId(json.optInt("id"));
			String stationName = json.optString("name");
			station.setName(stationName);
			station.setDisplayName(json.optString("displayName", stationName));
			stations.add(station);
		}
		return stations;
	}

	@Override
	public List<TvStation> getAllTvStation() {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String allTvStation = tv.getAllTvStation();
		JSONArray array;
		try {
			array = new JSONArray(allTvStation);
		} catch (JSONException e) {
			throw new MyTvException("invalid data of tv station.", e);
		}
		int length = array == null ? 0 : array.length();
		if (length <= 0) {
			throw new MyTvException("invalid data of tv station.");
		}

		List<TvStation> stations = new ArrayList<TvStation>(length);
		for (int i = 0; i < length; i++) {
			JSONObject json = array.optJSONObject(i);
			TvStation station = new TvStation();
			station.setCity(json.optString("city"));
			station.setClassify(json.optString("classify"));
			station.setId(json.optInt("id"));
			String stationName = json.optString("name");
			station.setName(stationName);
			station.setDisplayName(json.optString("displayName", stationName));
			stations.add(station);
		}
		return stations;
	}

	@Override
	public List<ProgramTable> getProgramTable(String name, String date) {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String programTable = tv.getProgramTable(name, date);
		JSONArray array;
		try {
			array = new JSONArray(programTable);
		} catch (JSONException e) {
			throw new MyTvException("invalid program table data of " + name
					+ " at " + date, e);
		}
		int length = array == null ? 0 : array.length();
		if (length <= 0) {
			throw new MyTvException("invalid program table data of " + name
					+ " at " + date);
		}

		List<ProgramTable> ptList = new ArrayList<ProgramTable>(length);
		for (int i = 0; i < length; i++) {
			JSONObject json = array.optJSONObject(i);
			ProgramTable e = new ProgramTable();
			e.setAirDate(json.optString("airDate"));
			e.setAirTime(json.optString("airTime"));
			e.setId(json.optLong("id"));
			e.setProgram(json.optString("program"));
			e.setStation(json.optInt("station"));
			e.setStationName(json.optString("stationName"));
			e.setWeek(json.optInt("week"));
			ptList.add(e);
		}
		return ptList;
	}

}
