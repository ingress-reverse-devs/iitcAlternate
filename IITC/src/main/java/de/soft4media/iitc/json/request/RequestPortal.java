package de.soft4media.iitc.json.request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import de.soft4media.iitc.connect.Connect;
import de.soft4media.iitc.db.Message;
import de.soft4media.iitc.db.Portal;


@ManagedBean(name="RequestPortal")
@RequestScoped
public class RequestPortal {


	private List<Portal> portal;
	


	public RequestPortal()
	{
		this.portal = new ArrayList<Portal>();
	}
	
	public List<Portal> startRequestPortal(String lat, String lng, String zoom) throws IllegalStateException, ParseException 
	{		
		this.portal = new ArrayList<Portal>();
		
		Connect con =  new Connect();

		Object obj = con.startRequest(con.THINNEDENTITIESV4, lat, lng, zoom, "0");
		
		JSONObject jsonObject = (JSONObject) obj;		
		
		JSONObject result = (JSONObject) jsonObject.get("result");
		
		JSONObject map = (JSONObject) result.get("map");
		
		JSONObject mapId = (JSONObject) map.get("15_17514_10923");
		
		
		JSONArray deletedGameEntities = (JSONArray) mapId.get("deletedGameEntityGuids");
		JSONArray gameEntities = (JSONArray) mapId.get("gameEntities");
	
		
		Iterator<JSONArray> iterator = gameEntities.iterator();
		
		while (iterator.hasNext()) {
			
			portal.add(parsePortal(iterator.next()));
		}	
		
		return portal;
	}
	
	
	private Portal parsePortal(JSONArray obj)
	{
		Portal portal = new Portal();
		
		JSONArray jsonArr = obj;
		
		portal.setPortalId(jsonArr.get(0).toString());
		portal.setTimestamp((Long) jsonArr.get(1));
		
		JSONObject payload = (JSONObject)jsonArr.get(2);
		
		JSONObject controllingTeam = (JSONObject) payload.get("controllingTeam");
		portal.setControllingTeam(controllingTeam.get("team").toString());
		
		JSONObject imageByUrl = (JSONObject) payload.get("imageByUrl");
		portal.setImageByUrl(imageByUrl != null ? imageByUrl.get("imageUrl").toString(): "");
		
		JSONObject locationE6 = (JSONObject) payload.get("locationE6");
		if(locationE6 != null)
		{
			portal.setLocationE6Lat(locationE6.get("latE6").toString());
			portal.setLocationE6Lng(locationE6.get("lngE6").toString());
		}
		
		JSONObject resonatorArray = (JSONObject) payload.get("resonatorArray");
		portal.setResonatorArray(locationE6 != null ?  (JSONArray) resonatorArray.get("resonators"): new JSONArray());
		
		JSONObject captured = (JSONObject) payload.get("captured");
		if(captured != null)
		{
			portal.setCapturedTime(captured.get("capturedTime").toString());
			portal.setCapturingPlayerId(captured.get("capturingPlayerId").toString());
		}
		
		JSONObject portalV2 = (JSONObject) payload.get("portalV2");
		if(portalV2 != null)
		{
			portal.setLinkedEdges((JSONArray) portalV2.get("linkedEdges"));
			portal.setLinkedModArray((JSONArray) portalV2.get("linkedModArray"));
			portal.setDescriptiveText((JSONObject) portalV2.get("descriptiveText"));
		}
		
		return portal;
	}

	public List<Portal> getMessage() {
		return portal;
	}

	public void setMessage(List<Portal> portal) {
		this.portal = portal;
	}
	
	public List<Portal> getPortal() {
		return portal;
	}

}
