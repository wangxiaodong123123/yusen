package com.ysdata.steelarch.element;

public class SteelArchCollectParameter {
	private int id = 0;
	private String name = "";
	private double left_steelarch_to_steelarch_distance = 0;
	private double left_steelarch_to_tunnelface_distance = 0;
	private double left_secondcar_to_steelarch_distance = 0;
	private String left_measure_date = "";
	private double right_steelarch_to_steelarch_distance = 0;
	private double right_steelarch_to_tunnelface_distance = 0;
	private double right_secondcar_to_steelarch_distance = 0;
	private String right_measure_date = "";
	
	public SteelArchCollectParameter(int id, String name, double left_steelarch_to_steelarch_distance,
			double left_steelarch_to_tunnelface_distance, double left_secondcar_to_steelarch_distance, 
			String left_measure_date, double right_steelarch_to_steelarch_distance,
			double right_steelarch_to_tunnelface_distance, double right_secondcar_to_steelarch_distance,
			String right_measure_date) {
		this.id = id;
		this.name = name;
		this.left_steelarch_to_steelarch_distance = left_steelarch_to_steelarch_distance;
		this.left_steelarch_to_tunnelface_distance = left_steelarch_to_tunnelface_distance;
		this.left_secondcar_to_steelarch_distance = left_secondcar_to_steelarch_distance;
		this.left_measure_date = left_measure_date;
		this.right_steelarch_to_steelarch_distance = right_steelarch_to_steelarch_distance;
		this.right_steelarch_to_tunnelface_distance = right_steelarch_to_tunnelface_distance;
		this.right_secondcar_to_steelarch_distance = right_secondcar_to_steelarch_distance;
		this.right_measure_date = right_measure_date;
	}
	
	public int getId () {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLeftSteelarchToSteelarchDistance () {
		return left_steelarch_to_steelarch_distance;
	}
	
	public double getLeftSteelarchToTunnelFaceDistance() {
		return left_steelarch_to_tunnelface_distance;
	}
	
	public double getLeftSecondCarToSteelarchDistance() {
		return left_secondcar_to_steelarch_distance;
	}
	
	public String getLeftMeasureDate() {
		return left_measure_date;
	}	
	
	public double getRightSteelarchToSteelarchDistance () {
		return right_steelarch_to_steelarch_distance;
	}
	
	public double getRightSteelarchToTunnelFaceDistance() {
		return right_steelarch_to_tunnelface_distance;
	}
	
	public double getRightSecondCarToSteelarchDistance() {
		return right_secondcar_to_steelarch_distance;
	}
	
	public String getRightMeasureDate() {
		return right_measure_date;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLeftSteelarchToSteelarchDistance (double left_steelarch_to_steelarch_distance) {
		this.left_steelarch_to_steelarch_distance = left_steelarch_to_steelarch_distance;
	}
	
	public void setLeftSteelarchToTunnelFaceDistance(double left_steelarch_to_tunnelface_distance) {
		this.left_steelarch_to_tunnelface_distance = left_steelarch_to_tunnelface_distance;
	}
	
	public void setLeftSecondCarToSteelarchDistance(double left_secondcar_to_steelarch_distance) {
		this.left_secondcar_to_steelarch_distance = left_secondcar_to_steelarch_distance;
	}
	
	public void setLeftMeasureDate(String left_measure_date) {
		this.left_measure_date = left_measure_date;
	}
	
	public void setRightSteelarchToSteelarchDistance (double right_steelarch_to_steelarch_distance) {
		this.right_steelarch_to_steelarch_distance = right_steelarch_to_steelarch_distance;
	}
	
	public void setRightSteelarchToTunnelFaceDistance(double right_steelarch_to_tunnelface_distance) {
		this.right_steelarch_to_tunnelface_distance = right_steelarch_to_tunnelface_distance;
	}
	
	public void setRightSecondCarToSteelarchDistance(double right_secondcar_to_steelarch_distance) {
		this.right_secondcar_to_steelarch_distance = right_secondcar_to_steelarch_distance;
	}
	
	public void setRightMeasureDate(String right_measure_date) {
		this.right_measure_date = right_measure_date;
	}
}

