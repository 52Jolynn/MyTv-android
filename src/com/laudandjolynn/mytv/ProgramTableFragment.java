package com.laudandjolynn.mytv;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.laudandjolynn.mytv.android.R;
import com.laudandjolynn.mytv.model.ProgramTable;
import com.laudandjolynn.mytv.model.TvStation;
import com.laudandjolynn.mytv.service.DataService;
import com.laudandjolynn.mytv.service.HessianImpl;
import com.laudandjolynn.mytv.utils.AppUtils;
import com.laudandjolynn.mytv.utils.Tuple;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午9:46:42
 * @copyright: 旦旦游广州工作室
 */
public class ProgramTableFragment extends Fragment implements
		OnItemClickListener {
	private final static String TAG = ProgramTable.class.getName();
	private String classify;
	private String date;
	private DataService dataService = new HessianImpl();
	private final static Pattern PATTERN_DATE = Pattern
			.compile("\\d+-\\d{2}-\\d{2}\\s+(\\d{2}:\\d{2}):\\d{2}(?:\\.\\d+)?");
	private ListView lvStation = null;
	private TvStationAdapter tvApt = null;
	private ProgramTableAdapter ptApt = null;
	private ProgressDialog pbDialog = null;
	private Handler handler = new MyHandler(this);

	private final static class MyHandler extends Handler {
		private ProgramTableFragment fragment = null;

		public MyHandler(ProgramTableFragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void handleMessage(Message msg) {
			if (AppUtils.DISMISS_PROGRESS_DIALOG == msg.what
					&& fragment.pbDialog != null
					&& fragment.pbDialog.isShowing()) {
				fragment.pbDialog.dismiss();
			} else if (AppUtils.EXCEPTION_CATCHED == msg.what) {
				Toast.makeText(fragment.getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static ProgramTableFragment newInstance() {
		return new ProgramTableFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setArguments(String classify, String date) {
		this.classify = classify;
		this.date = date;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_program_table,
				null);
		if (classify == null || date == null) {
			return view;
		}
		if (pbDialog == null) {
			pbDialog = AppUtils.buildEpgProgressDialog(getActivity());
		}
		pbDialog.show();
		// 获取电视台及节目数据
		AsyncTask<Void, Void, Tuple<List<TvStation>, List<ProgramTable>>> task = new AsyncTask<Void, Void, Tuple<List<TvStation>, List<ProgramTable>>>() {
			@Override
			protected Tuple<List<TvStation>, List<ProgramTable>> doInBackground(
					Void... params) {
				try {
					List<TvStation> stationList = dataService
							.getTvStationByClassify(classify);
					Log.d(TAG, "station list: " + stationList.toString());
					Log.d(TAG, "query program table of " + classify + " at "
							+ date);
					List<ProgramTable> ptList = dataService.getProgramTable(
							stationList.get(0).getName(), date);
					return new Tuple<List<TvStation>, List<ProgramTable>>(
							stationList, ptList);
				} catch (Exception e) {
					handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
					Message msg = new Message();
					msg.what = AppUtils.EXCEPTION_CATCHED;
					msg.obj = getResources().getText(
							R.string.query_epg_data_error).toString();
					handler.sendMessage(msg);
					return new Tuple<List<TvStation>, List<ProgramTable>>(
							new ArrayList<TvStation>(0),
							new ArrayList<ProgramTable>(0));
				}
			}

			@Override
			protected void onPostExecute(
					Tuple<List<TvStation>, List<ProgramTable>> result) {
				handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
				List<TvStation> stationList = result.left;
				List<ProgramTable> ptList = result.right;

				// 获取页面元素
				lvStation = (ListView) view
						.findViewById(R.id.fragment_program_table_tvlist);
				tvApt = new TvStationAdapter(getActivity(), stationList);
				tvApt.selectedItemPosition = 0;
				lvStation.setAdapter(tvApt);
				lvStation.setOnItemClickListener(ProgramTableFragment.this);

				ListView lvProgramTable = (ListView) view
						.findViewById(R.id.fragment_program_table_programs);
				ptApt = new ProgramTableAdapter(getActivity(), ptList);
				lvProgramTable.setAdapter(ptApt);
			}
		};
		task.execute();
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		tvApt.selectedItemPosition = position;
		tvApt.notifyDataSetChanged();
		final TvStation station = (TvStation) parent
				.getItemAtPosition(position);
		if (pbDialog == null) {
			pbDialog = AppUtils.buildEpgProgressDialog(getActivity());
		}
		pbDialog.show();
		AsyncTask<Void, Void, List<ProgramTable>> task = new AsyncTask<Void, Void, List<ProgramTable>>() {
			@Override
			protected List<ProgramTable> doInBackground(Void... params) {
				try {
					return dataService.getProgramTable(station.getName(), date);
				} catch (Exception e) {
					handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
					Message msg = new Message();
					msg.what = AppUtils.EXCEPTION_CATCHED;
					msg.obj = getResources().getText(
							R.string.query_epg_data_error).toString();
					handler.sendMessage(msg);
					return new ArrayList<ProgramTable>(0);
				}
			}

			@Override
			protected void onPostExecute(List<ProgramTable> result) {
				handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
				ptApt.setNotifyOnChange(false);
				ptApt.clear();
				ptApt.setNotifyOnChange(true);
				ptApt.addAll(result);
			}
		};
		task.execute();
	}

	/**
	 * 电视台数据适配器
	 * 
	 * @author tdhuang
	 * 
	 */
	private final static class TvStationAdapter extends ArrayAdapter<TvStation> {
		private int selectedItemPosition = -1;

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
				holder.tvChannel = (TextView) view
						.findViewById(R.id.tv_station_item_tvChannel);
				holder.tvStationName = (TextView) view
						.findViewById(R.id.tv_station_item_tvStationName);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			TvStation station = getItem(position);
			String channel = station.getChannel();
			holder.tvChannel.setText(channel == null ? "" : channel);
			holder.tvStationName.setText(station.getDisplayName());
			if (selectedItemPosition == position) {
				view.setBackgroundResource(R.color.tv_station_list_view_list_selector);
			} else {
				view.setBackgroundResource(R.color.transparent);
			}
			return view;
		}

		private final static class ViewHolder {
			private TextView tvChannel;
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
						R.layout.program_table_item, null);
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
			String airTime = pt.getAirTime();
			Matcher matcher = PATTERN_DATE.matcher(airTime);
			if (matcher.matches()) {
				holder.tvAirTime.setText(matcher.group(1));
			}
			return view;
		}

		private final static class ViewHolder {
			private TextView tvProgram;
			private TextView tvAirTime;
		}
	}
}
