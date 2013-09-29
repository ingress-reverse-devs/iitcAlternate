package de.soft4media.iitc.json.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.primefaces.component.fieldset.Fieldset;

import de.soft4media.iitc.connect.Connect;
import de.soft4media.iitc.db.Message;
import de.soft4media.iitc.db.Portal;


@ManagedBean(name="RequestPortal")
@SessionScoped
public class RequestPortal {


	private List<Portal> portal;
	Map<String,Boolean> tiles;
	StringBuilder queryTiles;
	Map<String,Boolean> queryTilesNumber;


	public RequestPortal()
	{
		this.portal = new ArrayList<Portal>();
	}
	
	
	public static int[] getTileNumber(final double lat, final double lon, final int zoom) 
	{
		int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
		int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
		return new int[]{ytile, xtile};
	}

	
	public void buildQueryArray(int[] minYX, int[] maxYX, String zoom)
	{		
		tiles = new HashMap<String, Boolean>();
		
		int yLngSize = maxYX[0] - minYX[0];
		int xLatSize = maxYX[1] - minYX[1];
	
		
		for(int a = 0; a<=xLatSize; a++)
		{
			for(int b = 0; b<=yLngSize; b++)
			{
				tiles.put(zoom + "_" + (minYX[1]+a)+ "_" +(minYX[0]+b), false);
			}
			
		}
		
		
	}
	
	public List<Portal> startRequestPortal(String centerlat,String centerlng, String minlat,String minlng,String maxlat,String maxlng,String zoom) throws IllegalStateException, ParseException, UnsupportedEncodingException 
	{		
		this.portal = new ArrayList<Portal>();
		
		Connect con =  new Connect();
		
		int[] maxYX = getTileNumber(Double.parseDouble(minlat), Double.parseDouble(maxlng), Integer.parseInt(zoom));
		int[] minYX = getTileNumber(Double.parseDouble(maxlat), Double.parseDouble(minlng), Integer.parseInt(zoom));
		
		minlat = minlat.replace(".", "").substring(0,8);
		maxlat = maxlat.replace(".", "").substring(0,8);
		minlng = minlng.replace(".", "").substring(0,8);
		maxlng = maxlng.replace(".", "").substring(0,8);
		
		buildQueryArray(minYX, maxYX, zoom);
		
		
		StringEntity input = null;
		
		int anzahl = 0;
		int count = 0;
		
		for (Entry<String, Boolean> entry : tiles.entrySet()) 
		{
			if(anzahl == 0)				
			{
				queryTiles = new StringBuilder("{\"4kr3ofeptwgary2j\":\"dashboard.getThinnedEntitiesV4\",\"n27qzc8389kgakyv\":[");
				queryTilesNumber = new HashMap<String, Boolean>();
			}
				
			if(anzahl < 5)
			{
				queryTiles.append("{\"39031qie1i4aq563\":\"" +  entry.getKey() + "\",\"bgxibcomzoto63sn\":\"" +  entry.getKey() +"\",\"pg98bwox95ly0ouu\":" + minlat + ",\"eib1bkq8znpwr0g7\":" + minlng + ",\"ilfap961rwdybv63\":" + maxlat + ",\"lpf7m1ifx0ieouzq\":" + maxlng + "}");
				queryTilesNumber.put(entry.getKey(), entry.getValue());
				
				anzahl++;
				count++;
				
				if(anzahl < 4)
				{
					
					
					if(count == tiles.size()-1)
					{
						System.out.println("last tile");
					}
					else
					{
						queryTiles.append(",");
						continue;
					}
				}
			}
			
			
						
				
			queryTiles.append("]}");
			
			
			input = new StringEntity(queryTiles.toString());
			
			Object obj = con.startRequest(con.THINNEDENTITIESV4, input, centerlat, centerlng, zoom);
			
			//setzt tile auf positive rückmeldung
			if(obj != null)
				tiles.put(entry.getKey(), true);
			else
				continue;
			
			JSONObject jsonObject = (JSONObject) obj;		
			
			JSONObject result = (JSONObject) jsonObject.get("result");
			
			JSONObject map = (JSONObject) result.get("map");
			
			
			for (Entry<String, Boolean> tileNumber : queryTilesNumber.entrySet()) 
			{		
			
				JSONObject mapId = (JSONObject) map.get(tileNumber.getKey());
				
				
				JSONArray deletedGameEntities = (JSONArray) mapId.get("deletedGameEntityGuids");
				JSONArray gameEntities = (JSONArray) mapId.get("gameEntities");
			
				
				if(gameEntities != null)
				{
					
					Iterator<JSONArray> iterator = gameEntities.iterator();
					
					while (iterator.hasNext()) {
						
						Portal parsedPortal = null;
						
						if((parsedPortal = parsePortal(iterator.next())) != null)
							portal.add(parsedPortal);
					}
				}
			}
			
			
			anzahl = 0;
		
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
		
		JSONObject captured = (JSONObject) payload.get("captured");
		if(captured != null)
		{
			portal.setCapturedTime(captured.get("capturedTime").toString());
			portal.setCapturingPlayerId(captured.get("capturingPlayerId").toString());
		}
		else 
			return null;
		
		
		
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
