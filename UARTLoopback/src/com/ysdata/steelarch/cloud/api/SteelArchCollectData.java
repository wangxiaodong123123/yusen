package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ��ͬ��ʵ����
 * */
public final class SteelArchCollectData {
	/**
     * ��·ID
     * */
	@SerializedName("ProjectId")
    public int ProjectId;
	
	/**
     * ��ͬ��ID
     * */
	@SerializedName("CsId")
    public int CsId;
	
	/**
     * ����ID
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;
	
	/**
     * ��������
     * */
	@SerializedName("SubProjectName")
    public String SubProjectName;
	
	/**
	 * �ֹ���Id
	 * */
	@SerializedName("id")
	public int id;
	
	/**
	 * �ֹ�����̱��
	 * */
	@SerializedName("name")
	public String name;
	
	/**
	 * ����������
	 * */
	@SerializedName("leftMeasureDate")
	public String leftMeasureDate;
	
	/**
	 * �Ҳ��������
	 * */
	@SerializedName("rightMeasureDate")
	public String rightMeasureDate;	
	
	/**
	 * ���������
	 * */
	@SerializedName("leftMeasureDistance")
	public double leftMeasureDistance;
	
	/**
	 * �Ҳ�������
	 * */
	@SerializedName("rightMeasureDistance")
	public double rightMeasureDistance;		
	
	/**
	 * ��������������
	 * */
	@SerializedName("leftTunnelfaceDistance")
	public double leftTunnelfaceDistance;
	
	/**
	 * �Ҳ������������
	 * */
	@SerializedName("rightTunnelfaceDistance")
	public double rightTunnelfaceDistance;		
	
	/**
	 * �����ģ��̨������
	 * */
	@SerializedName("leftSecondCarDistance")
	public double leftSecondCarDistance;
	
	/**
	 * �Ҳ���ģ��̨������
	 * */
	@SerializedName("rightSecondCarDistance")
	public double rightSecondCarDistance;		
	
	/**
	 * ��ප�ڷ�����Ƭ
	 * */
	@SerializedName("leftPicDirEntrance")
	public String leftPicDirEntrance;
	
	/**
	 * �Ҳප�ڷ�����Ƭ
	 * */
	@SerializedName("rightPicDirEntrance")
	public String rightPicDirEntrance;
	
	/**
	 * ��������淽����Ƭ
	 * */
	@SerializedName("leftPicDirTunnelface")
	public String leftPicDirTunnelface;
	
	/**
	 * �Ҳ������淽����Ƭ
	 * */
	@SerializedName("rightPicDirTunnelface")
	public String rightPicDirTunnelface;	
}
