package com.laudandjolynn.mytv.model;

/**
 * 电视台
 * 
 * @author Laud
 * 
 */
public class TvStation {
	private int id;
	private String name;
	private String city;
	private String classify;
	private String channel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "TvStation [id=" + id + ", name=" + name + ", city=" + city
				+ ", classify=" + classify + ", channel=" + channel + "]";
	}

}
