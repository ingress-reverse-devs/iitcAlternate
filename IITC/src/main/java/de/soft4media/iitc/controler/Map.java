package de.soft4media.iitc.controler;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.json.simple.parser.ParseException;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import de.soft4media.iitc.db.Portal;
import de.soft4media.iitc.json.request.RequestMessage;
import de.soft4media.iitc.json.request.RequestPortal;

@ManagedBean
@RequestScoped
public class Map implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{RequestMessage}")
	private RequestMessage requestMessage;
	
	
	@ManagedProperty(value = "#{RequestPortal}")
	private RequestPortal requestPortal;
	
	

	private String zoomLevel = "13";
	private String centerLat = "51.340000";
	private String centerLng = "12.360000";
	
	private MapModel portalModel;
	List<Portal> portals;
	
	
	public MapModel getPortalModel() {
		return portalModel;
	}

	public void setPortalModel(MapModel portalModel) {
		this.portalModel = portalModel;
	}

	public Map()
	{
		setMapModel();
	}

	public void onStateChange(StateChangeEvent event) throws IllegalStateException, ParseException {  
		
        LatLngBounds bounds = event.getBounds();  
        int zoomLevel = event.getZoomLevel();  
         
        this.zoomLevel = String.valueOf(zoomLevel);
        this.centerLat = event.getCenter().getLat()+"";
        this.centerLng = event.getCenter().getLng()+"";
        
        
        requestMessage();
        requestPortal();
        
    }  
	
	private void requestMessage() throws IllegalStateException, ParseException 
	{
		this.requestMessage.startRequestMessage(centerLat, centerLng, zoomLevel, "20");		
	}

	private void requestPortal() throws IllegalStateException, ParseException
	{	
		portals = this.requestPortal.startRequestPortal(centerLat, centerLng, zoomLevel);
		
		setMapModel();
	}
	
	private void setMapModel()
	{
		portalModel = new DefaultMapModel();  
        
		if(portals != null)
		{
			for (Portal portal : requestPortal.getPortal()) 
			{
				if(portal.getCapturedTime() == null)
					continue;
				
				String lat =  portal.getLocationE6Lat().substring(0, 2) + "." + portal.getLocationE6Lat().substring(2);
				String lng =  portal.getLocationE6Lng().substring(0, 2) + "." + portal.getLocationE6Lng().substring(2);
				
				
		        LatLng coord = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));  
		        
		        portalModel.addOverlay(new Marker(coord, portal.getDescriptiveText().get("TITLE").toString()));
			}
		}
       
	}
	
	public String getCenterPosition()
	{
		return this.centerLat + "," + this.centerLng;
	}	
	
	
	public String getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(String zoomLevel) {
		this.zoomLevel = zoomLevel;
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

}
