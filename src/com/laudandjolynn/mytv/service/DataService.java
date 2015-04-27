package com.laudandjolynn.mytv.service;

import java.util.List;

import android.os.RemoteException;

import com.laudandjolynn.mytv.model.MyTv;
import com.laudandjolynn.mytv.model.ProgramTable;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午10:18:06
 * @copyright: 旦旦游广州工作室
 */
public interface DataService {
	/**
	 * 获取电视台分类
	 * 
	 * @return
	 */
	public String[] getMyTvClassify();

	/**
	 * 根据电视台分类获取分类下的所有电视台
	 * 
	 * @param classify
	 * @return
	 * @throws RemoteException
	 */
	public List<MyTv> getMyTvByClassify(String classify);

	/**
	 * 获取指定电视台、日期的节目表
	 * 
	 * @param displayName
	 *            电视台名称
	 * @param classify
	 *            电视台分类
	 * @param date
	 *            日期, yyyy-MM-dd
	 * @return
	 */
	public List<ProgramTable> getProgramTable(String displayName,
			String classify, String date);
}
