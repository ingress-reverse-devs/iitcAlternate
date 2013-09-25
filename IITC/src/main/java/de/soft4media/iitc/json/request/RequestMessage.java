package de.soft4media.iitc.json.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;











import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import de.soft4media.iitc.connect.Connect;
import de.soft4media.iitc.db.Message;


@ManagedBean(name="RequestMessage")
@RequestScoped
public class RequestMessage 
{
	
	private List<Message> message;
	
	
	public RequestMessage()
	{
		this.message = new ArrayList<Message>();
	}
	
	public void startRequestMessage(String lat, String lng, String zoom, String zahl) throws IllegalStateException, ParseException 
	{		
		this.message = new ArrayList<Message>();
		
		Connect con =  new Connect();

		Object obj = con.startRequest(con.PAGINATEDPLEXTSV2, lat, lng, zoom, zahl);
		
		JSONObject jsonObject = (JSONObject) obj;		
		
		JSONArray result = (JSONArray) jsonObject.get("result");
		
		
		
		Iterator<JSONArray> iterator = result.iterator();
		
		while (iterator.hasNext()) {
			
			message.add(parseMessage(iterator.next()));
		}	
		
	}
	
	
	private Message parseMessage(JSONArray obj)
	{
		Message msg = new Message();
		
		JSONArray jsonArr = obj;
		
		msg.setMsgId(jsonArr.get(0).toString());
		msg.setTimestamp((Long) jsonArr.get(1));
		
		JSONObject payload = (JSONObject)jsonArr.get(2);
		
		JSONObject plext = (JSONObject) payload.get("plext");
		
		msg.setPlextType((String) plext.get("plextType"));
		
		msg.setMarkup((JSONArray) plext.get("markup"));
		
		msg.setCategories((Long) plext.get("categories"));
		
		msg.setTeam((String) plext.get("team"));
		
		msg.setText((String) plext.get("text"));
		
		
		return msg;
	}

	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}
}
