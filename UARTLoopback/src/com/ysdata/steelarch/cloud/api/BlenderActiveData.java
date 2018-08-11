package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ��ͬ��ʵ����
 * */
public final class BlenderActiveData {
	/**
     * ��ʶID
     * */
	@SerializedName("id")
    public int id;

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
	 * ������� 
	 * */
	@SerializedName("intOrder")
	public int intOrder;
	
	/**
	 * ��������
	 * */
	@SerializedName("strDate")
	public String strDate;
	
    /**
     * ���迪ʼʱ��
     * */
	@SerializedName("strBeginTime")
    public String strBeginTime;
	
    /**
     * �������ʱ��
     * */
	@SerializedName("strEndTime")
    public String strEndTime;
	
    /**
     * ˮ�ұ�
     * */
	@SerializedName("dblMixRatioWater")
    public double dblMixRatioWater;
	
    /**
     * ��������
     * */
	@SerializedName("dblCount")
    public double dblCount;
	
    /**
     * ������������λ��
     * */
	@SerializedName("dblPosition")
    public double dblPosition;
	
    /**
     * ����Ч��ͼ�ļ���
     * */
	@SerializedName("strDesignImage")
    public String strDesignImage;
	
    /**
     * �����ֳ�ͼ�ļ���
     * */
	@SerializedName("strActiveImage")
    public String strActiveImage;
	

	/**
     * ˮ���ܶ�
     * */
	@SerializedName("dblCementDensity")
    public double dblCementDensity;
	
	/**
     * ˮ�ܶ�
     * */
	@SerializedName("dblWaterDensity")
    public double dblWaterDensity;
	
	/**
     * ˮ������kg��������/��1+ˮ�ұȣ�
     * */
	@SerializedName("dblCementCount")
    public double dblCementCount;
	
	/**
     * ˮ����kg��������-ˮ������
     * */
	@SerializedName("dblCementCount")
    public double dblWaterCount;
	
	/**
     * ������L��ROUND��ˮ�ܶ�/ˮ���ܶ�+ˮ�ұ�/ˮ�ܶȣ�3��
     * */
	@SerializedName("dblVolume")
    public double dblVolume;
	
	/**
     * ��ͺ��ܶȣ����أ���ROUND��������/������,0��
     * */
	@SerializedName("dblMixDensity")
    public double dblMixDensity;
	
	/**
     * ����ʱ�䣨���ӣ�
     * */
	@SerializedName("dblTimeSpan")
    public double dblTimeSpan;
	
	/**
     * ���������ϴ�ʱ�䣨����޸����޸�ʱ��Ϊ׼��
     * */
	@SerializedName("CreateTime")
    public String CreateTime;
	
	/**
     * ����Ч��ͼraw data
     * */
	@SerializedName("strDesignImageData")
    public String strDesignImageData;
	
	/**
     * �����ֳ�ͼraw data
     * */
	@SerializedName("strActiveImageData")
    public String strActiveImageData;
}
