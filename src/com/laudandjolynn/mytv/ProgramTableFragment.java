package com.laudandjolynn.mytv;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.laudandjolynn.mytv.android.R;
import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;
import com.laudandjolynn.mytv.service.DataService;
import com.laudandjolynn.mytv.service.HessianImpl;
import com.laudandjolynn.mytv.utils.Tuple;

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
	private DataService dataService = new HessianImpl();

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
		// 获取电视台及节目数据
		AsyncTask<Void, Void, Tuple<List<TvStation>, List<ProgramTable>>> task = new AsyncTask<Void, Void, Tuple<List<TvStation>, List<ProgramTable>>>() {
			@Override
			protected Tuple<List<TvStation>, List<ProgramTable>> doInBackground(
					Void... params) {
				List<TvStation> stationList = dataService.getAllTvStation();
				Log.d(TAG, "station list: " + stationList.toString());
				Log.d(TAG, "query program table of " + stationName + " at "
						+ date);
				List<ProgramTable> ptList = dataService.getProgramTable(
						stationName, date);
				return new Tuple<List<TvStation>, List<ProgramTable>>(
						stationList, ptList);
			}
		};
		Tuple<List<TvStation>, List<ProgramTable>> result = null;
		try {
			result = task.execute().get();
		} catch (Exception e) {
			Toast.makeText(
					getActivity(),
					getResources().getText(
							R.string.query_program_table_of_tv_station_error)
							.toString(), Toast.LENGTH_SHORT).show();
		}
		List<TvStation> stationList = result.left;
		List<ProgramTable> ptList = result.right;

		// 获取页面元素
		View view = inflater.inflate(R.layout.fragment_program_table, null);
		ListView lvStation = (ListView) view
				.findViewById(R.id.fragment_program_table_tvlist);
		TvStationAdapter tvApt = new TvStationAdapter(getActivity(),
				stationList);
		lvStation.setAdapter(tvApt);

		ListView lvProgramTable = (ListView) view
				.findViewById(R.id.fragment_program_table_programs);
		ProgramTableAdapter ptApt = new ProgramTableAdapter(getActivity(),
				ptList);
		lvProgramTable.setAdapter(ptApt);
		return view;
	}

	/**
	 * 电视台数据适配器
	 * 
	 * @author tdhuang
	 * 
	 */
	private final static class TvStationAdapter extends ArrayAdapter<TvStation> {

		public TvStationAdapter(Context context, List<TvStation> stationList) {
			super(context, 0, stationList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.tv_station_item, null);
				holder = new ViewHolder();
				holder.tvStationName = (TextView) view
						.findViewById(R.id.tv_station_item_tvStationName);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			TvStation station = getItem(position);
			holder.tvStationName.setText(station.getName());
			return view;
		}

		private final static class ViewHolder {
			private TextView tvStationName;
		}
	}

	private final static class ProgramTableAdapter extends
			ArrayAdapter<ProgramTable> {

		public ProgramTableAdapter(Context context, List<ProgramTable> objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(
						R.layout.tv_station_item, null);
				holder = new ViewHolder();
				holder.tvProgram = (TextView) view
						.findViewById(R.id.program_table_item_tvProgram);
				holder.tvAirTime = (TextView) view
						.findViewById(R.id.program_table_item_tvAirTime);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			ProgramTable pt = getItem(position);
			holder.tvProgram.setText(pt.getProgram());
			holder.tvAirTime.setText(pt.getAirTime());
			return view;
		}

		private final static class ViewHolder {
			private TextView tvProgram;
			private TextView tvAirTime;
		}
	}
}
