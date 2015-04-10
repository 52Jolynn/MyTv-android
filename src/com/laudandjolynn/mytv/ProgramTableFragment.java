package com.laudandjolynn.mytv;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;
import com.laudandjolynn.mytv.service.DataService;
import com.laudandjolynn.mytv.service.HessianImpl;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午9:46:42
 * @copyright: 旦旦游广州工作室
 */
public class ProgramTableFragment extends Fragment {
	private final static String TAG = ProgramTable.class.getName();
	private static final String ARG_STATION_NAME = "stationName";
	private static final String ARG_AIR_DATE = "airDate";
	private String stationName;
	private String date;

	public static ProgramTableFragment newInstance(String stationName,
			String date) {
		ProgramTableFragment f = new ProgramTableFragment();
		Bundle b = new Bundle();
		b.putString(ARG_STATION_NAME, stationName);
		b.putString(ARG_AIR_DATE, date);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.stationName = getArguments().getString(ARG_STATION_NAME);
		this.date = getArguments().getString(ARG_AIR_DATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		DataService dataService = new HessianImpl();
		List<TvStation> stationList = dataService.getAllTvStation();
		Log.d(TAG, "station list: " + stationList.toString());
		Log.d(TAG, "query program table of " + stationName + " at " + date);
		List<ProgramTable> ptList = dataService.getProgramTable(stationName,
				date);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
