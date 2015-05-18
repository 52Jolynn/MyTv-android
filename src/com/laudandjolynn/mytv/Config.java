package com.laudandjolynn.mytv;

/**
 * @author: Laud
 * @email: htd0324@gmail.com
 * @date: 2015年4月10日 上午10:08:08
 * @copyright: 旦旦游广州工作室
 */
public class Config {
	public final static NetConfig NET_CONFIG = new NetConfig();

	public final static class NetConfig {
		private String ip = "tv.1eq1.com";
		private int port = 33117;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

	}
}
