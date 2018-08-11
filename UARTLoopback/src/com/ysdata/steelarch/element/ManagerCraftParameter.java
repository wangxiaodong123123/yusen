package com.ysdata.steelarch.element;

public class ManagerCraftParameter {
	private int id = 0;
	private String name = "";
	private double design_steelarch_to_steelarch_distance = 0;
	private double measure_steelarch_to_steelarch_distance = 0;
	private double secondcar_to_tunnelface_distance = 0;
	private double steelarch_to_tunnelface_distance = 0;
	
	public ManagerCraftParameter(int id, String name, double design_steelarch_to_steelarch_distance,  
			double measure_steelarch_to_steelarch_distance, double secondcar_to_tunnelface_distance, 
			double steelarch_to_tunnelface_distance) {
		this.id = id;   //page
		this.name = name;
		this.design_steelarch_to_steelarch_distance = design_steelarch_to_steelarch_distance;
		this.measure_steelarch_to_steelarch_distance = measure_steelarch_to_steelarch_distance;
		this.secondcar_to_tunnelface_distance = secondcar_to_tunnelface_distance;
		this.steelarch_to_tunnelface_distance = steelarch_to_tunnelface_distance;
	}
	
	public ManagerCraftParameter() {
		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDesignSteelarchToSteelarchDistance (double design_steelarch_to_steelarch_distance) {
		this.design_steelarch_to_steelarch_distance = design_steelarch_to_steelarch_distance;
	}
	
	public void setMeasureSteelarchToSteelarchDistance (double measure_steelarch_to_steelarch_distance) {
		this.measure_steelarch_to_steelarch_distance = measure_steelarch_to_steelarch_distance;
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
	
	public double getDesignSteelarchToSteelarchDistance () {
		return design_steelarch_to_steelarch_distance;
	}
	
	public double getMeasureSteelarchToSteelarchDistance () {
		return measure_steelarch_to_steelarch_distance;
	}
	
	public double getSteelarchToTunnelFaceDistance() {
		return steelarch_to_tunnelface_distance;
	}
	
	public double getSecondCarToTunnelFaceDistance() {
		return secondcar_to_tunnelface_distance;
	}
}

