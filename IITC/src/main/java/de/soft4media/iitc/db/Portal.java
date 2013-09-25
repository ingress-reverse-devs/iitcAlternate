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
public class Portal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String portalId;

	private Long timestamp;
	
	private String controllingTeam;
	
	private String imageByUrl;
	
	private String locationE6Lat;
	
	private String locationE6Lng;
	
	private JSONArray resonatorArray;
	
	private String capturedTime;
	
	private String capturingPlayerId;
	
	private JSONArray linkedEdges;
	
	private JSONArray linkedModArray;
	
	private JSONObject descriptiveText;

	//---------------------------------------------------

	
	public String getPortalId() {
		return portalId;
	}

	public String getControllingTeam() {
		return controllingTeam;
	}

	public void setControllingTeam(String controllingTeam) {
		this.controllingTeam = controllingTeam;
	}

	public String getImageByUrl() {
		return imageByUrl;
	}

	public void setImageByUrl(String imageByUrl) {
		this.imageByUrl = imageByUrl;
	}

	public JSONArray getResonatorArray() {
		return resonatorArray;
	}

	public void setResonatorArray(JSONArray object) {
		this.resonatorArray = object;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	public Long getTimestamp() {
		long time = 60*60*2 * 1000;
		return timestamp + time;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getLocationE6Lat() {
		return locationE6Lat;
	}

	public void setLocationE6Lat(String locationE6Lat) {
		this.locationE6Lat = locationE6Lat;
	}

	public String getLocationE6Lng() {
		return locationE6Lng;
	}

	public void setLocationE6Lng(String locationE6Lng) {
		this.locationE6Lng = locationE6Lng;
	}

	public String getCapturedTime() {
		return capturedTime;
	}

	public void setCapturedTime(String capturedTime) {
		this.capturedTime = capturedTime;
	}

	public String getCapturingPlayerId() {
		return capturingPlayerId;
	}

	public void setCapturingPlayerId(String capturingPlayerId) {
		this.capturingPlayerId = capturingPlayerId;
	}

	public JSONArray getLinkedEdges() {
		return linkedEdges;
	}

	public void setLinkedEdges(JSONArray linkedEdges) {
		this.linkedEdges = linkedEdges;
	}

	public JSONArray getLinkedModArray() {
		return linkedModArray;
	}

	public void setLinkedModArray(JSONArray linkedModArray) {
		this.linkedModArray = linkedModArray;
	}

	public JSONObject getDescriptiveText() {
		return descriptiveText;
	}

	public void setDescriptiveText(JSONObject jsonObject) {
		this.descriptiveText = jsonObject;
	}

}
