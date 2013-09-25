package de.soft4media.iitc.json.request;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.json.simple.parser.ParseException;

import de.soft4media.iitc.connect.Connect;

@ManagedBean(name="SendMessage")
@RequestScoped
public class SendMessage {

	private String message = "";
	private String lat = "";
	private String lng = "";
	
	public void startSendMessage(String lat, String lng) throws IllegalStateException, ParseException {
		
		Connect con =  new Connect();

		Object obj = con.startSendRequest(con.SENDPLEXT, getMessage(), lat, lng);
		
		this.message = "";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
