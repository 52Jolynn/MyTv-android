package com.laudandjolynn.mytv;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.laudandjolynn.mytv.android.R;
import com.laudandjolynn.mytv.service.DataService;
import com.laudandjolynn.mytv.service.HessianImpl;
import com.laudandjolynn.mytv.utils.AppUtils;
import com.laudandjolynn.mytv.utils.DateUtils;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final static String TAG = MainActivity.class.getName();
	private String date = DateUtils.today();
	private Handler handler = new MyHandler(this);
	private ProgressDialog pbDialog = null;
	private String[] classify = null;
	private MyPagerAdapter pagerApt = null;
	private PagerSlidingTabStrip pagerSlidingTabs = null;
	private String APP_NAME = null;
	private AsyncTask<Void, Void, String[]> currentTask = null;

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
		APP_NAME = getResources().getText(R.string.app_name).toString();
		setTitle(DateUtils.string2Date(date, "yyyy-MM-dd"));

		Button btnRefresh = (Button) findViewById(R.id.main_activity_btnRefresh);
		Button btnToday = (Button) findViewById(R.id.main_activity_btnToday);
		Button btnTomorrow = (Button) findViewById(R.id.main_activity_btnTomorrow);
		btnRefresh.setOnClickListener(this);
		btnToday.setOnClickListener(this);
		btnTomorrow.setOnClickListener(this);

		classify = new String[] { getResources().getText(R.string.app_name)
				.toString() };
		pagerSlidingTabs = (PagerSlidingTabStrip) findViewById(R.id.main_activity_nav_tabs);
		ViewPager pager = (ViewPager) findViewById(R.id.main_activity_vpPager);
		pagerApt = new MyPagerAdapter(classify, getSupportFragmentManager());
		pager.setAdapter(pagerApt);
		int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pagerSlidingTabs.setViewPager(pager);
		refresh();
	}

	private void refresh() {
		if (pbDialog == null) {
			pbDialog = AppUtils.buildEpgProgressDialog(this);
			pbDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (currentTask != null) {
						currentTask.cancel(true);
					}
				}
			});
		}
		pbDialog.show();

		// 获取数据
		currentTask = new AsyncTask<Void, Void, String[]>() {

			@Override
			protected String[] doInBackground(Void... params) {
				DataService dataService = new HessianImpl();
				try {
					return dataService.getMyTvClassify();
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
				Log.d(TAG, Arrays.deepToString(result));
				classify = result;
				pagerApt.titles = classify;
				pagerSlidingTabs.notifyDataSetChanged();
				pagerApt.notifyDataSetChanged();
			}
		};

		currentTask.execute();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.main_activity_btnRefresh:
			refresh();
			break;
		case R.id.main_activity_btnToday:
			String today = DateUtils.today();
			if (!today.equals(date)) {
				date = today;
				setTitle(DateUtils.string2Date(today, "yyyy-MM-dd"));
				pagerApt.notifyDataSetChanged();
			}
			break;
		case R.id.main_activity_btnTomorrow:
			Date tomorrow = DateUtils.addDay(
					DateUtils.string2Date(date, "yyyy-MM-dd"), 1);
			date = DateUtils.date2String(tomorrow, "yyyy-MM-dd");
			setTitle(tomorrow);
			pagerApt.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void setTitle(Date date) {
		int week = DateUtils.getWeek(date);
		setTitle(APP_NAME + " @" + this.date + " " + week2String(week));
	}

	private String week2String(int week) {
		switch (week) {
		case Calendar.SUNDAY:
			return getResources().getText(R.string.sunday).toString();
		case Calendar.MONDAY:
			return getResources().getText(R.string.monday).toString();
		case Calendar.TUESDAY:
			return getResources().getText(R.string.tuesday).toString();
		case Calendar.WEDNESDAY:
			return getResources().getText(R.string.wednesday).toString();
		case Calendar.THURSDAY:
			return getResources().getText(R.string.thursday).toString();
		case Calendar.FRIDAY:
			return getResources().getText(R.string.friday).toString();
		case Calendar.SATURDAY:
			return getResources().getText(R.string.saturday).toString();
		default:
			return "";
		}
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
			return ProgramTableFragment.newInstance();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ProgramTableFragment fragment = (ProgramTableFragment) super
					.instantiateItem(container, position);
			String title = getPageTitle(position).toString();
			fragment.setArguments(APP_NAME.equals(title) ? null : title, date);
			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return MyPagerAdapter.POSITION_NONE;
		}
	}
}
