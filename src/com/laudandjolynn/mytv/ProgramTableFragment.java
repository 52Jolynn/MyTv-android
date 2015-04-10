package com.laudandjolynn.mytv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午9:46:42
 * @copyright: 旦旦游广州工作室
 */
public class ProgramTableFragment extends Fragment {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
