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
import com.laudandjolynn.mytv.model.MyTv;
import com.laudandjolynn.mytv.model.ProgramTable;

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
	public String[] getMyTvClassify() {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String classify = tv.getMyTvClassify();
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
	public List<MyTv> getMyTvByClassify(String classify) {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String allTvStation = tv.getMyTvByClassify(classify);
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

		List<MyTv> stations = new ArrayList<MyTv>(length);
		for (int i = 0; i < length; i++) {
			JSONObject json = array.optJSONObject(i);
			MyTv myTv = new MyTv();
			myTv.setChannel(json.optString("channel", ""));
			myTv.setClassify(json.optString("classify"));
			myTv.setId(json.optInt("id"));
			myTv.setStationName(json.optString("stationName"));
			myTv.setDisplayName(json.optString("displayName"));
			stations.add(myTv);
		}
		return stations;
	}

	@Override
	public List<ProgramTable> getProgramTable(String displayName,
			String classify, String date) {
		HessianProxyFactory proxy = new HessianProxyFactory();
		JolynnTv tv;
		try {
			tv = (JolynnTv) proxy.create(JolynnTv.class, url);
		} catch (MalformedURLException e) {
			throw new MyTvException("invalid url: " + url, e);
		}
		String programTable = tv.getProgramTable(displayName, classify, date);
		JSONArray array;
		try {
			array = new JSONArray(programTable);
		} catch (JSONException e) {
			throw new MyTvException("invalid program table data of "
					+ displayName + " at " + date, e);
		}
		int length = array == null ? 0 : array.length();
		if (length <= 0) {
			throw new MyTvException("invalid program table data of "
					+ displayName + " at " + date);
		}

		List<ProgramTable> ptList = new ArrayList<ProgramTable>(length);
		for (int i = 0; i < length; i++) {
			JSONObject json = array.optJSONObject(i);
			ProgramTable e = new ProgramTable();
			e.setAirDate(json.optString("airDate"));
			e.setAirTime(json.optString("airTime"));
			e.setId(json.optLong("id"));
			e.setProgram(json.optString("program"));
			e.setStationName(json.optString("stationName"));
			e.setWeek(json.optInt("week"));
			ptList.add(e);
		}
		return ptList;
	}

}
