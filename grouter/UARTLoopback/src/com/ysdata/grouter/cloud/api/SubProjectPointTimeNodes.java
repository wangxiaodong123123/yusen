package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public class SubProjectPointTimeNodes {
	/**
     * ʱ���
     * */
	@SerializedName("Time")
    public double time;

    /**
     * ѹ��ֵ
     * */
	@SerializedName("Pressure")
    public double pressure;

    /**
     * �Ƿ�Ϊѹ���͵�
     * */
	@SerializedName("IsMinPressureNode")
    public boolean isMinPressureNode;

    /**
     * �Ƿ�Ϊѹ���ߵ�
     * */
	@SerializedName("IsMaxPressureNode")
    public boolean isMaxPressureNode;

    /**
     * �Ƿ�Ϊע��ʱ��ڵ�
     * */
    @SerializedName("IsGroutingNode")
    public boolean isGroutingNode;

    /**
     * ����һ��йѹʱ��㵽����ѹ���ﵽ1.05��ʱ��ʱ�䣬��λ����
     * */
    @SerializedName("CurrGroutingSecs")
    public double currGroutingSecs;
    
    /**
     * ����һ��йѹʱ��㵽����ѹ���ﵽ1.05��ʱ��ע����
     * */
    @SerializedName("CurrGrouting")
    public double currGrouting;

    /**
     * �ۼ�ע���������������ʱ���Y����ֵ
     * */
    @SerializedName("Grouting")
    public double grouting;

    
    public SubProjectPointTimeNodes(){
    	this.setTime(0);
    	this.setPressure(0D);
    	this.setGrouting(0D);
    }

	public double getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double Grouting() {
		return grouting;
	}

	public void setGrouting(double grouting) {
		this.grouting = grouting;
	}
}
