package com.ysdata.steelarch.element;

public class DdSteelArchDataParameter {
	private int id = 0;
	private String name = "";
	private String date = "";
	private double steelarch_to_steelarch_distance = 0;
	private double secondcar_to_tunnelface_distance = 0;
	private double steelarch_to_tunnelface_distance = 0;
	
	public DdSteelArchDataParameter(int id, String name, String date,  
			double steelarch_to_steelarch_distance, double secondcar_to_tunnelface_distance, 
			double steelarch_to_tunnelface_distance) {
		this.id = id;   //page
		this.name = name;
		this.date = date;
		this.steelarch_to_steelarch_distance = steelarch_to_steelarch_distance;
		this.secondcar_to_tunnelface_distance = secondcar_to_tunnelface_distance;
		this.steelarch_to_tunnelface_distance = steelarch_to_tunnelface_distance;
	}
	
	public DdSteelArchDataParameter() {
		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setSteelarchToSteelarchDistance (double steelarch_to_steelarch_distance) {
		this.steelarch_to_steelarch_distance = steelarch_to_steelarch_distance;
	}
	
	public void setSteelarchToTunnelFaceDistance(double steelarch_to_tunnelface_distance) {
		this.steelarch_to_tunnelface_distance = steelarch_to_tunnelface_distance;
	}
	
	public void setSecondCarToTunnelFaceDistance(double secondcar_to_tunnelface_distance) {
		this.secondcar_to_tunnelface_distance = secondcar_to_tunnelface_distance;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDate() {
		return date;
	}
	
	public double getSteelarchToSteelarchDistance () {
		return steelarch_to_steelarch_distance;
	}
	
	public double getSteelarchToTunnelFaceDistance() {
		return steelarch_to_tunnelface_distance;
	}
	
	public double getSecondCarToTunnelFaceDistance() {
		return secondcar_to_tunnelface_distance;
	}
}

