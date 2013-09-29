package de.soft4media.iitc.json.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
















import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import de.soft4media.iitc.connect.Connect;
import de.soft4media.iitc.db.Message;


@ManagedBean(name="RequestMessage")
@SessionScoped
public class RequestMessage 
{
	
	private List<Message> message;
	
	
	public RequestMessage()
	{
		this.message = new ArrayList<Message>();
	}
	
	public void startRequestMessage(String centerlat,String centerlng, String minlat,String minlng,String maxlat,String maxlng,String zoom,String zahl, String type) throws IllegalStateException, ParseException, UnsupportedEncodingException 
	{		
		this.message = new ArrayList<Message>();
		
		minlat = minlat.replace(".", "").substring(0,8);
		maxlat = maxlat.replace(".", "").substring(0,8);
		minlng = minlng.replace(".", "").substring(0,8);
		maxlng = maxlng.replace(".", "").substring(0,8);
		
		Connect con =  new Connect();
		
		StringEntity input = null;
		
		input = new StringEntity("{\"4kr3ofeptwgary2j\":\"dashboard.getPaginatedPlextsV2\",\"tmb0vgxgp5grsnhp\":" + zahl + ",\"pg98bwox95ly0ouu\":" + minlat + ",\"eib1bkq8znpwr0g7\":" + minlng + ",\"ilfap961rwdybv63\":" + maxlat + ",\"lpf7m1ifx0ieouzq\":" + maxlng + ",\"hljqffkpwlx0vtjt\":" + System.currentTimeMillis() + ",\"sw317giy6x2xj9zm\":-1,\"hljqffkpwlx0vtjt\":-1,\"0dvtbatgzcfccchh\":false}");
		

		Object obj = con.startRequest(con.PAGINATEDPLEXTSV2, input, centerlat, centerlng, zoom);
		
		JSONObject jsonObject = (JSONObject) obj;		
		
		JSONArray result = (JSONArray) jsonObject.get("result");
		
		
		
		Iterator<JSONArray> iterator = result.iterator();
		
		while (iterator.hasNext()) {
			
			Message msg = parseMessage(iterator.next(), type);
			
			if(msg != null)
				message.add(msg);
		}	

	}
	
	
	private Message parseMessage(JSONArray obj, String type)
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
		
		
		if(type != null) 
			if(type.equals(msg.getPlextType()))
				return msg;
			else
				return null;
		
		return msg;
	}

	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}
}
