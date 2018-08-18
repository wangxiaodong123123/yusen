package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ��ͬ��ʵ����
 * */
public final class SteelArchCraftData {
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
	 * �ֹ���id 
	 * */
	@SerializedName("id")
	public int id;
	
	/**
	 * �ֹ�����̱��
	 * */
	@SerializedName("name")
	public String name;
	
    /**
     * �ֹ�����Ƽ��
     * */
	@SerializedName("designDistance")
    public double designDistance;
	
    /**
     * �ֹ��ܱ�ž���
     * */
	@SerializedName("nameMeter")
    public double nameMeter;
	
    /**
     * �ֹ����붴�ھ���
     * */
	@SerializedName("entranceDistance")
    public double entranceDistance;
	
    /**
     * ģ��̨�����
     * */
	@SerializedName("secondCarWidth")
    public double secondCarWidth;
}
