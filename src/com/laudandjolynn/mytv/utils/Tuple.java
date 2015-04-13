package com.laudandjolynn.mytv.utils;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月13日 下午12:46:12
 * @copyright: 旦旦游广州工作室
 */
public class Tuple<U, V> {
	public U left;
	public V right;

	public Tuple() {
	}

	public Tuple(U left, V right) {
		this.left = left;
		this.right = right;
	}

}
