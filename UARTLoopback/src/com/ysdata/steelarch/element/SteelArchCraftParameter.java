package com.ysdata.steelarch.element;

public class SteelArchCraftParameter {
	private int id = 0;
	private String name = "";
	private double steelarch_to_steelarch_distance = 0;
	private double steelarch_to_entrance_distance = 0;
	private double second_car_width = 0;
	private double nameMeter = 0;
	
	public SteelArchCraftParameter(int id, String name, double steelarch_to_steelarch_distance,
			double steelarch_to_entrance_distance, double second_car_width, 
			double nameMeter) {
		this.id = id;
		this.name = name;
		this.steelarch_to_steelarch_distance = steelarch_to_steelarch_distance;
		this.steelarch_to_entrance_distance = steelarch_to_entrance_distance;
		this.second_car_width = second_car_width;
		this.nameMeter = nameMeter;
	}

	public SteelArchCraftParameter() {

	}
	
	public int getId () {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getSteelarchToSteelarchDistance () {
		return steelarch_to_steelarch_distance;
	}
	
	public double getSteelarchToEntranceDistance() {
		return steelarch_to_entrance_distance;
	}
	
	public double getSecondCarWidth() {
		return second_car_width;
	}
	
	public double getNameMeter() {
		return nameMeter;
	}	
	
	public void setId (int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSteelarchToSteelarchDistance (double steelarch_to_steelarch_distance) {
		this.steelarch_to_steelarch_distance = steelarch_to_steelarch_distance;
	}
	
	public void setSteelarchToEntranceDistance(double steelarch_to_entrance_distance) {
		this.steelarch_to_entrance_distance = steelarch_to_entrance_distance;
	}
	
	public void setSecondCarWidth(double second_car_width) {
		this.second_car_width = second_car_width;
	}
	
	public void setNameMeter(double nameMeter) {
		this.nameMeter = nameMeter;
	}
}

