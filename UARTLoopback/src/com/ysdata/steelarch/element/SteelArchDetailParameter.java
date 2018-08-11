package com.ysdata.steelarch.element;

public class SteelArchDetailParameter {
	private String left_measure_date = "";
	private String mileage_tunnel_entry = "";
	private int orderno = 0;
	private String name = "";
	private double design_distance = 0;
	private double steelarch_to_tunnel_entrance_distance;
	private String left_measure_time = "";
	private double left_measure_distance = 0;
	private double left_steelarch_to_tunnelface_distance = 0;
	private String right_measure_date = "";
	private String right_measure_time = "";
	private double right_measure_distance = 0;
	private double right_steelarch_to_tunnelface_distance = 0;	
	private String left_pic_dir_tunnel_entrance = "";
	private String left_pic_dir_tunnel_face = "";
	private String right_pic_dir_tunnel_entrance = "";
	private String right_pic_dir_tunnel_face = "";
	
	public SteelArchDetailParameter(int orderno, String name, String left_measure_date, String right_measure_date,
			String mileage_tunnel_entry, double design_distance, double steelarch_to_tunnel_entrance_distance,
			String left_measure_time, double left_measure_distance, double left_steelarch_to_tunnelface_distance,
			String right_measure_time, double right_measure_distance, double right_steelarch_to_tunnelface_distance,
			String left_pic_dir_tunnel_entrance, String left_pic_dir_tunnel_face, String right_pic_dir_tunnel_entrance,
			String right_pic_dir_tunnel_face) {
		this.setOrderno(orderno);
		this.setName(name);
		this.setLeftMeasureDate(left_measure_date);
		this.setMileageTunnelEntry(mileage_tunnel_entry);
		this.setDesign_distance(design_distance);
		this.setSteelarch_to_tunnel_entrance_distance(steelarch_to_tunnel_entrance_distance);
		this.setLeft_measure_time(left_measure_time);
		this.setLeft_measure_distance(left_measure_distance);
		this.setLeft_steelarch_to_tunnelface_distance(left_steelarch_to_tunnelface_distance);
		this.setRight_measure_time(right_measure_time);
		this.setRight_measure_date(right_measure_date);
		this.setRight_measure_distance(right_measure_distance);
		this.setRight_steelarch_to_tunnelface_distance(right_steelarch_to_tunnelface_distance);
		this.setLeft_pic_dir_tunnel_entrance(left_pic_dir_tunnel_entrance);
		this.setLeft_pic_dir_tunnel_face(left_pic_dir_tunnel_face);
		this.setRight_pic_dir_tunnel_entrance(right_pic_dir_tunnel_entrance);
		this.setRight_pic_dir_tunnel_face(right_pic_dir_tunnel_face);
	}

	/**
	 * @return the left_measure_date
	 */
	public String getLeftMeasureDate() {
		return left_measure_date;
	}

	/**
	 * @param left_measure_date the left_measure_date to set
	 */
	public void setLeftMeasureDate(String left_measure_date) {
		this.left_measure_date = left_measure_date;
	}

	/**
	 * @return the mileage_tunnel_entry
	 */
	public String getMileageTunnelEntry() {
		return mileage_tunnel_entry;
	}

	/**
	 * @param mileage_tunnel_entry the mileage_tunnel_entry to set
	 */
	public void setMileageTunnelEntry(String mileage_tunnel_entry) {
		this.mileage_tunnel_entry = mileage_tunnel_entry;
	}

	/**
	 * @return the orderno
	 */
	public int getOrderno() {
		return orderno;
	}

	/**
	 * @param orderno the orderno to set
	 */
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the design_distance
	 */
	public double getDesign_distance() {
		return design_distance;
	}

	/**
	 * @param design_distance the design_distance to set
	 */
	public void setDesign_distance(double design_distance) {
		this.design_distance = design_distance;
	}

	/**
	 * @return the steelarch_to_tunnel_entrance_distance
	 */
	public double getSteelarch_to_tunnel_entrance_distance() {
		return steelarch_to_tunnel_entrance_distance;
	}

	/**
	 * @param steelarch_to_tunnel_entrance_distance the steelarch_to_tunnel_entrance_distance to set
	 */
	public void setSteelarch_to_tunnel_entrance_distance(
			double steelarch_to_tunnel_entrance_distance) {
		this.steelarch_to_tunnel_entrance_distance = steelarch_to_tunnel_entrance_distance;
	}

	/**
	 * @return the left_measure_time
	 */
	public String getLeft_measure_time() {
		return left_measure_time;
	}

	/**
	 * @param left_measure_time the left_measure_time to set
	 */
	public void setLeft_measure_time(String left_measure_time) {
		this.left_measure_time = left_measure_time;
	}

	/**
	 * @return the left_measure_distance
	 */
	public double getLeft_measure_distance() {
		return left_measure_distance;
	}

	/**
	 * @param left_measure_distance the left_measure_distance to set
	 */
	public void setLeft_measure_distance(double left_measure_distance) {
		this.left_measure_distance = left_measure_distance;
	}

	/**
	 * @return the left_steelarch_to_tunnelface_distance
	 */
	public double getLeft_steelarch_to_tunnelface_distance() {
		return left_steelarch_to_tunnelface_distance;
	}

	/**
	 * @param left_steelarch_to_tunnelface_distance the left_steelarch_to_tunnelface_distance to set
	 */
	public void setLeft_steelarch_to_tunnelface_distance(
			double left_steelarch_to_tunnelface_distance) {
		this.left_steelarch_to_tunnelface_distance = left_steelarch_to_tunnelface_distance;
	}

	/**
	 * @return the right_measure_time
	 */
	public String getRight_measure_time() {
		return right_measure_time;
	}

	/**
	 * @param right_measure_time the right_measure_time to set
	 */
	public void setRight_measure_time(String right_measure_time) {
		this.right_measure_time = right_measure_time;
	}

	/**
	 * @return the right_measure_distance
	 */
	public double getRight_measure_distance() {
		return right_measure_distance;
	}

	/**
	 * @param right_measure_distance the right_measure_distance to set
	 */
	public void setRight_measure_distance(double right_measure_distance) {
		this.right_measure_distance = right_measure_distance;
	}

	/**
	 * @return the right_steelarch_to_tunnelface_distance
	 */
	public double getRight_steelarch_to_tunnelface_distance() {
		return right_steelarch_to_tunnelface_distance;
	}

	/**
	 * @param right_steelarch_to_tunnelface_distance the right_steelarch_to_tunnelface_distance to set
	 */
	public void setRight_steelarch_to_tunnelface_distance(
			double right_steelarch_to_tunnelface_distance) {
		this.right_steelarch_to_tunnelface_distance = right_steelarch_to_tunnelface_distance;
	}

	/**
	 * @return the left_pic_dir_tunnel_entrance
	 */
	public String getLeft_pic_dir_tunnel_entrance() {
		return left_pic_dir_tunnel_entrance;
	}

	/**
	 * @param left_pic_dir_tunnel_entrance the left_pic_dir_tunnel_entrance to set
	 */
	public void setLeft_pic_dir_tunnel_entrance(
			String left_pic_dir_tunnel_entrance) {
		this.left_pic_dir_tunnel_entrance = left_pic_dir_tunnel_entrance;
	}

	/**
	 * @return the left_pic_dir_tunnel_face
	 */
	public String getLeft_pic_dir_tunnel_face() {
		return left_pic_dir_tunnel_face;
	}

	/**
	 * @param left_pic_dir_tunnel_face the left_pic_dir_tunnel_face to set
	 */
	public void setLeft_pic_dir_tunnel_face(String left_pic_dir_tunnel_face) {
		this.left_pic_dir_tunnel_face = left_pic_dir_tunnel_face;
	}

	/**
	 * @return the right_pic_dir_tunnel_entrance
	 */
	public String getRight_pic_dir_tunnel_entrance() {
		return right_pic_dir_tunnel_entrance;
	}

	/**
	 * @param right_pic_dir_tunnel_entrance the right_pic_dir_tunnel_entrance to set
	 */
	public void setRight_pic_dir_tunnel_entrance(
			String right_pic_dir_tunnel_entrance) {
		this.right_pic_dir_tunnel_entrance = right_pic_dir_tunnel_entrance;
	}

	/**
	 * @return the right_pic_dir_tunnel_face
	 */
	public String getRight_pic_dir_tunnel_face() {
		return right_pic_dir_tunnel_face;
	}

	/**
	 * @param right_pic_dir_tunnel_face the right_pic_dir_tunnel_face to set
	 */
	public void setRight_pic_dir_tunnel_face(String right_pic_dir_tunnel_face) {
		this.right_pic_dir_tunnel_face = right_pic_dir_tunnel_face;
	}

	/**
	 * @return the right_measure_date
	 */
	public String getRight_measure_date() {
		return right_measure_date;
	}

	/**
	 * @param right_measure_date the right_measure_date to set
	 */
	public void setRight_measure_date(String right_measure_date) {
		this.right_measure_date = right_measure_date;
	}
}

