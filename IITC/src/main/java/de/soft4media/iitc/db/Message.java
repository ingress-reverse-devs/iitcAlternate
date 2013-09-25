package de.soft4media.iitc.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
@Table
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String msgId;

	private Long timestamp;
	
	private String plextType;
	
	private JSONArray markup;
	
	private String text;
	
	private Long categories;
	
	private String team;

	//---------------------------------------------------

	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Long getTimestamp() {
		long time = 60*60*2 * 1000;
		return timestamp + time;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public JSONArray getMarkup() {
		return markup;
	}

	public void setMarkup(JSONArray jsonArray) {
		this.markup = jsonArray;
	}

	public Long getCategories() {
		return categories;
	}

	public void setCategories(Long long1) {
		this.categories = long1;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getPlextType() {
		return plextType;
	}

	public void setPlextType(String plextType) {
		this.plextType = plextType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}



}
