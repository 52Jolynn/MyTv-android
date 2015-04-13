package com.laudandjolynn.mytv.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

import com.laudandjolynn.mytv.android.R;

public class AppUtils {
	public final static int SHOW_PROGRESS_DIALOG = 0;
	public final static int DISMISS_PROGRESS_DIALOG = 1;
	public final static int READY_TO_LOAD_DATA = 2;
	public final static int EPG_TASK_THREA_NUM = 3;

	public final static ProgressDialog buildEpgProgressDialog(Context ctx) {
		ProgressDialog pbDialog = new ProgressDialog(ctx);
		Resources resources = ctx.getResources();
		pbDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pbDialog.setCancelable(true);
		pbDialog.setTitle(resources.getText(
				R.string.query_epg_data_progress_dialog_title).toString());
		pbDialog.setMessage(resources.getText(
				R.string.query_epg_data_progress_dialog_content).toString());
		return pbDialog;
	}

}
