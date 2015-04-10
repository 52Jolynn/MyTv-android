package com.laudandjolynn.mytv.exception;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午10:13:49
 * @copyright: 旦旦游广州工作室
 */
public class MyTvException extends RuntimeException {
	private static final long serialVersionUID = -6619326825971391114L;

	public MyTvException() {
		super();
	}

	public MyTvException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MyTvException(String detailMessage) {
		super(detailMessage);
	}

	public MyTvException(Throwable throwable) {
		super(throwable);
	}

}
