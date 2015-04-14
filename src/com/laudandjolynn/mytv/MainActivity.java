package com.laudandjolynn.mytv;

import java.lang.ref.WeakReference;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.laudandjolynn.mytv.utils.AppUtils;
import com.laudandjolynn.mytv.utils.DateUtils;

public class MainActivity extends FragmentActivity {
	private String date = DateUtils.today();
	private Handler handler = new MyHandler(this);
	private ProgressDialog pbDialog = null;
	private String[] classify = null;

	private final static class MyHandler extends Handler {
		private WeakReference<MainActivity> ctx = null;

		public MyHandler(MainActivity ctx) {
			this.ctx = new WeakReference<MainActivity>(ctx);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = ctx.get();
			if (activity != null) {
				if (AppUtils.DISMISS_PROGRESS_DIALOG == msg.what
						&& activity.pbDialog != null
						&& activity.pbDialog.isShowing()) {
					activity.pbDialog.dismiss();
				} else if (AppUtils.EXCEPTION_CATCHED == msg.what) {
					Toast.makeText(activity, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (pbDialog == null) {
			pbDialog = AppUtils.buildEpgProgressDialog(this);
		}
		pbDialog.show();
		classify = new String[] { getResources().getText(R.string.app_name)
				.toString() };
		// 获取数据
		AsyncTask<Void, Void, String[]> task = new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... params) {
				DataService dataService = new HessianImpl();
				try {
					return dataService.getTvStationClassify();
				} catch (Exception e) {
					handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
					Message msg = new Message();
					msg.what = AppUtils.EXCEPTION_CATCHED;
					msg.obj = getResources().getText(
							R.string.query_epg_data_error).toString();
					handler.sendMessage(msg);
					return classify;
				}
			}

			@Override
			protected void onPostExecute(String[] result) {
				handler.sendEmptyMessage(AppUtils.DISMISS_PROGRESS_DIALOG);
				classify = result;
				loadData();
			}
		};

		task.execute();
	}

	private void loadData() {
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.nav_tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(classify,
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
