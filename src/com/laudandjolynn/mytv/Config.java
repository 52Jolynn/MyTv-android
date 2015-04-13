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
		private String ip = "192.168.0.109";
		private int hessianPort = 33117;
		private int rmiPort = 33118;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getHessianPort() {
			return hessianPort;
		}

		public void setHessianPort(int hessianPort) {
			this.hessianPort = hessianPort;
		}

		public int getRmiPort() {
			return rmiPort;
		}

		public void setRmiPort(int rmiPort) {
			this.rmiPort = rmiPort;
		}

	}
}
