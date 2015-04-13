package com.laudandjolynn.mytv;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.laudandjolynn.mytv.android.R;
import com.laudandjolynn.mytv.service.DataService;
import com.laudandjolynn.mytv.service.HessianImpl;
import com.laudandjolynn.mytv.utils.DateUtils;

public class MainActivity extends FragmentActivity {
	private String date = DateUtils.today();
	private ProgressDialog pbDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		AsyncTask<Void, Void, String[]> task = new AsyncTask<Void, Void, String[]>() {
			@Override
			protected String[] doInBackground(Void... params) {
				DataService dataService = new HessianImpl();
				String[] titles = dataService.getTvStationClassify();
				return titles;
			}
		};

		String[] titles = new String[] { getResources().getText(
				R.string.app_name).toString() };
		pbDialog = new ProgressDialog(this);
		pbDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pbDialog.setCancelable(true);
		pbDialog.setTitle(getResources().getText(
				R.string.query_epg_data_progress_dialog_title).toString());
		pbDialog.setMessage(getResources().getText(
				R.string.query_epg_data_progress_dialog_content).toString());
		pbDialog.show();
		try {
			titles = task.execute().get();
		} catch (Exception e) {
			String msg = getResources().getText(
					R.string.query_tv_station_classify_error).toString();
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
		pbDialog.dismiss();
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.nav_tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(titles,
				getSupportFragmentManager());
		pager.setAdapter(adapter);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		tabs.setViewPager(pager);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private String[] titles = null;

		public MyPagerAdapter(String[] titles, FragmentManager fm) {
			super(fm);
			this.titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			return ProgramTableFragment.newInstance(getPageTitle(position)
					.toString(), date);
		}

	}
}
