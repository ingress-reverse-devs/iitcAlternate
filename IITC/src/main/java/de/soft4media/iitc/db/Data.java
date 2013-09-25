package de.soft4media.iitc.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private String markupType;
	

	//---------------------------------------------------

	

	public String getMarkupType() {
		return markupType;
	}

	public void setMarkupType(String markupType) {
		this.markupType = markupType;
	}
}
