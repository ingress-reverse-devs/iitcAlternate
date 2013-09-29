package de.soft4media.iitc.controler;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.json.simple.parser.ParseException;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Overlay;

import de.soft4media.iitc.db.Portal;
import de.soft4media.iitc.json.request.RequestMessage;
import de.soft4media.iitc.json.request.RequestPortal;

@ManagedBean
@SessionScoped
public class Map implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{RequestMessage}")
	private RequestMessage requestMessage;
	
	
	@ManagedProperty(value = "#{RequestPortal}")
	private RequestPortal requestPortal;
	
	



	private String centerLat = "51.340000";
	private String centerLng = "12.360000";
	private String zoomLevel = "16";
	
	private String minLng = "";
	private String maxLng = "";
	
	private String minLat = "";
	private String maxLat = "";
	
	
	private MapModel portalModel;
	List<Portal> portals;
	
	private Circle selectedCircle;
	
	
	public MapModel getPortalModel() {
		return portalModel;
	}

	public void setPortalModel(MapModel portalModel) {
		this.portalModel = portalModel;
	}

	public String getCenterPosition()
	{
		return centerLat+","+centerLng;
		
	}
	
	public Map()
	{
		setMapModel();
	}

	public void onStateChange(StateChangeEvent event) throws IllegalStateException, ParseException, UnsupportedEncodingException {  
		
        LatLngBounds bounds = event.getBounds();  
        int zoomLevel = event.getZoomLevel();  
         
        this.zoomLevel = String.valueOf(zoomLevel);
        this.centerLat = event.getCenter().getLat() + "";
        this.centerLng = event.getCenter().getLng() + "";
        
        this.minLat = bounds.getSouthWest().getLat() +"";
        this.maxLat = bounds.getNorthEast().getLat() +"";
        
        this.minLng = bounds.getSouthWest().getLng() +"";
        this.maxLng = bounds.getNorthEast().getLng() +"";
        
        requestMessage();
        requestPortal();
        
    }  
	
	private void requestMessage() throws IllegalStateException, ParseException, UnsupportedEncodingException 
	{
		this.requestMessage.startRequestMessage(this.centerLat, this.centerLng, this.minLat, this.minLng, this.maxLat, this.maxLng, zoomLevel, "200", "PLAYER_GENERATED");		
	}

	private void requestPortal() throws IllegalStateException, ParseException, UnsupportedEncodingException
	{	
		portals = this.requestPortal.startRequestPortal(this.centerLat, this.centerLng, this.minLat, this.minLng, this.maxLat, this.maxLng, zoomLevel);
		
		setMapModel();
	}
	
	private void setMapModel()
	{
		portalModel = new DefaultMapModel();  
        
		if(portals != null)
		{
			for (Portal portal : requestPortal.getPortal()) 
			{				
				String lat =  portal.getLocationE6Lat().substring(0, 2) + "." + portal.getLocationE6Lat().substring(2);
				String lng =  portal.getLocationE6Lng().substring(0, 2) + "." + portal.getLocationE6Lng().substring(2);
				
				
				//portal.getDescriptiveText().get("TITLE").toString()
				
				String fractionColor = portal.getControllingTeam().equals("ENLIGHTENED")? "#2bed1b": "#009eff";
				String fractionLevelColor;
				
		        LatLng coord = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));  
		        
		        String data[] =  {portal.getDescriptiveText().get("TITLE").toString(),portal.getImageByUrl()};
		        
		        
		        Circle fractionPoint = new Circle( coord, 5);  
		        fractionPoint.setStrokeColor(fractionColor);  
		        fractionPoint.setStrokeWeight(2);
		        fractionPoint.setStrokeOpacity(1.0);  
		        fractionPoint.setFillOpacity(0.0);  
		        fractionPoint.setZindex(20);  
		        fractionPoint.setData(data);
		        
		        Circle fractionPointLevel = new Circle( coord, 5); 
		        fractionPointLevel.setData(data);
		        fractionPointLevel.setFillColor("red");
		        fractionPointLevel.setFillOpacity(1.0); 
		        fractionPointLevel.setZindex(10);
		        fractionPointLevel.setData(data);
		        
		        
		        portalModel.addOverlay(fractionPointLevel);
		        portalModel.addOverlay(fractionPoint);
			}
		}
       
	}
	
	public void onCircleSelect(OverlaySelectEvent event) {  
		Overlay overlay = event.getOverlay();
		setSelectedCircle((Circle)overlay);
		
    }

	
	public RequestMessage getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(RequestMessage requestMessage) {
		this.requestMessage = requestMessage;
	}
	
	public RequestPortal getRequestPortal() {
		return requestPortal;
	}

	public void setRequestPortal(RequestPortal requestPortal) {
		this.requestPortal = requestPortal;
	}

	public String getCenterLat() {
		return centerLat;
	}

	public void setCenterLat(String centerLat) {
		this.centerLat = centerLat;
	}

	public String getCenterLng() {
		return centerLng;
	}

	public void setCenterLng(String centerLng) {
		this.centerLng = centerLng;
	}
	
	public String getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(String zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public Circle getSelectedCircle() {
		return selectedCircle;
	}

	public void setSelectedCircle(Circle selectedCircle) {
		this.selectedCircle = selectedCircle;
	}

}
