package com.ysdata.steelarch.wireless.client;

public class Format {
	public final static String CONTROL_PANEL_IP = "192.168.43.144";
	public final static String STORAGE_IP = "192.168.43.144";
	public final static int CONTROL_PANEL_PORT = 8888;
	public final static int STORAGE_PORT = 8888;
	
	public final static int DEV_PANEL = 1;
	public final static int DEV_STORAGE = 2;
	
	public final static int PANEL_HEAD = 0x5a;
	public final static int PANEL_BREAK = 0xa5;
	
	public final static int STORAGE_HEAD = 0xee;
	public final static int STORAGE_BREAK = 0x11;
	
	// ������->ƽ��
	 
	public final static int CMD_PANEL_REQUEST_INTERNET = 0x01; //���������������
	public final static int CMD_STEELARCH_DATA = 0x02; //���Խ����ǵ����ݣ�����ʱ�䡢������������λ�õ�
	public final static int CMD_STEELARCH_ENTRANCE_PIC = 0x03; //���ڷ���
	public final static int CMD_STEELARCH_TUNNELFACE_PIC = 0x04; //�����淽��
	
	// ƽ��->������
	public final static int CMD_STEELARCH_REQUEST_COMM_SEQ = 0x05; //�������
	public final static int CMD_STEELARCH_CRAFT_DATA = 0x06; //�������
	public final static int CMD_STEELARCH_REQUEST_ENG = 0x07; //�������
	public final static int CMD_STEELARCH_COMM_STOP = 0x08; //���տ���������ݴ������
	
	public final static int HANDLER_REQUEST_INTERNET = 1;
	public final static String ENG_MATCH = "android.ang.action.ENG_MATCH";
	public final static String ENG_NOT_MATCH = "android.ang.action.ENG_NOT_MATCH";
	public final static String REQUEST_TRANSFER_DATA_SUCCESS = "android.ang.action.REQUEST_TRANSFER_DATA_SUCCESS";
	public final static String RECV_STEELARCH_DATA = "android.ang.action.RECV_STEELARCH_DATA";
	public final static int HANDLER_RECV_FIRST_GROUP_MIX_PIC_RESULT = 6;
	public final static String RECV_ENTRANCE_PIC = "android.ang.action.RECV_ENTRANCE_PIC";
	public final static String RECV_TUNNELFACE_PIC = "android.ang.action.RECV_TUNNELFACE_PIC";
	public final static int HANDLER_RECV_FIRST_GROUP_MIX_PIC_SCENE = 9;
	public final static int HANDLER_RECV_GROUP_MIX_PIC_SCENE = 10;
	public final static int HANDLER_RECV_MIX_PIC_SCENE_SUCCESS = 11;
	public final static String STEELARCH_COMM_FINISH = "android.ang.action.STEELARCH_COMM_FINISH";
	
	public final static String RECEIVE_MIX_CRAFT_ACK_TIMEOUT = "android.ang.action.RECEIVE_MIX_CRAFT_ACK_TIMEOUT";
	
	
	
	public final static int CMD_PANEL_PRESSURE_TIME = 0x02; //���յ�ע��ѹ�����ڵ��ʱ��ֵ��ע��ʱ��
//	public final static int CMD_CRAFT_PARAMETER = 0x03;
	public final static int CMD_PANEL_CRAFT_PARAMETER = 0x03; //����ê��ע�����ղ��� 
	public final static int CMD_REQUEST_COMM = 0x04; //ƽ�����������崫����,����ê����ʼ�������
	public final static int CMD_PANEL_COMM_STOP = 0x05; //���տ���������ݴ������
//	public final static int CMD_COMM_CONTROL = 0x06;
//	public final static int CMD_COMM_CONTROL_ACK = 0x06;
	public final static int CMD_CRAFT_PARAMETER = 0x09; //��ÿ��ê��ע�����ղ���
	public final static int CMD_SYSTEM_INFO = 0x06; //system_time / proj / eng
	public final static int CMD_COMM_STOP = 0x0a;  //�������崫��ê�����ݽ���
	public final static int CMD_REQUEST_PRJ_ENG = 0x09; //������·����������
	
	/* ƽ�����д�����ݴ���
	 * */
	public final static int CMD_CARD_PRESSURE_TIME = 0x02; //���յ�ע��ѹ�����ڵ��ʱ��ֵ��ע��ʱ��
	public final static int CMD_CARD_CRAFT_PARAMETER = 0x03; //����ê��ע�����ղ��� 
	public final static int CMD_CARD_COMM_STOP = 0x05;
	
	public final static int CMD_500MS_ADC = 0x06; //2
	public final static int CMD_20S_ADC = 0x07;   //3
	public final static int CMD_SET_PARAM = 0x09; //4
	public final static int LENGTH_PARAM = 0x0e;
	
	public final static String UPDATE_500MS_ADC = "android.ang.action.UPDATE_500MS_ADC";
	public final static String UPDATE_20S_ADC = "android.ang.action.UPDATE_20S_ADC";
	public final static String UPDATE_PARAM = "android.ang.action.UPDATE_PARAM";
	public final static String WIFI_RECV_FINISH = "android.ang.action.WIFI_RECV_FINISH";
	
	public final static String TIMEOUT_CONNECT = "android.ang.action.TIMEOUT_CONNECT";
	public final static String SUCCESS_CONNECT = "android.ang.action.SUCCESS_CONNECT";
	public final static String REQUEST_TRANSFER_DATA = "android.ang.action.REQUEST_TRANSFER_DATA";
	public final static String SUCCESS_CONNECT_INTERNET = "android.ang.action.SUCCESS_CONNECT_INTERNET";
	public final static String CHECK_SUCCESS = "android.ang.action.CHECK_SUCCESS";
	public final static String CHECK_FAILED = "android.ang.action.CHECK_FAILED";
	public final static String DEVICE_CONNECTION_LOST = "android.ang.action.DEVICE_CONNECTION_LOST";
	public final static String DATA_COMM_PARAM = "android.ang.action.DATA_COMM_PARAM";
	public final static String SUCCESS_SEND_DATA = "android.ang.action.SUCCESS_SEND_DATA";
	public final static String FAILED_SEND_DATA = "android.ang.action.FAILED_SEND_DATA";
	public final static String SENDING_PARAM = "android.ang.action.SENDING_PARAM";
	public final static String SEND_DATA_FINISH = "android.ang.action.SEND_DATA_FINISH";
	public final static String SEND_ANCHOR_PARAMETER = "android.ang.action.SEND_ANCHOR_PARAMETER";	
	public final static String PROJ_ENG_NOT_MATCH = "android.ang.action.PROJ_ENG_NOT_MATCH";
	public final static String PROJ_ENG_MATCH = "android.ang.action.PROJ_ENG_MATCH";
	public final static String SUCCESS_SEND_MIX_CRAFT_DATA = "android.ang.action.SUCCESS_SEND_MIX_CRAFT_DATA";
	public final static String FAILED_SEND_MIX_CRAFT_DATA = "android.ang.action.FAILED_SEND_MIX_CRAFT_DATA";
	
	
	public final static String RECV_PANEL_DATA = "android.ang.action.RECV_PANEL_DATA";
	public final static String CARD_PROJ_NOT_MATCH = "android.ang.action.CARD_PROJ_NOT_MATCH";
	public final static String CARD_ENG_NOT_MATCH = "android.ang.action.CARD_ENG_NOT_MATCH";
	public final static String CONTINUE_COLLECT_CARD_NEXT = "android.ang.action.CONTINUE_COLLECT_CARD_NEXT";
	public final static String CONTINUE_COLLECT_CARD_CANCEL = "android.ang.action.CONTINUE_COLLECT_CARD_CANCEL";
	
	
	public final static String SEND_CONTROL_DATA = "android.ang.action.SEND_CONTROL_DATA";
	public final static String RECEIVE_DATA_TIMEOUT = "android.ang.action.RECEIVE_DATA_TIMEOUT";
	public final static String EXIT_PARAM_SET_ACTIVITY = "android.ang.action.EXIT_PARAM_SET_ACTIVITY";
	public final static String RECEIVE_SYSTEM_INFO_ACK_TIMEOUT = "android.ang.action.RECEIVE_SYSTEM_INFO_ACK_TIMEOUT";
	public final static String RECEIVE_STEELARCH_PARAM_ACK_TIMEOUT = "android.ang.action.RECEIVE_STEELARCH_PARAM_ACK_TIMEOUT";
	
	public final static String ACTION_CRAFT_ERASE = "android.ang.action.CRAFT_ERASE";
	public final static String ACTION_CRAFT_DRAW = "android.ang.action.CRAFT_DRAW";
	public final static String ACTION_CRAFT_BACK = "android.ang.action.CRAFT_BACK";
	public final static String ACTION_CRAFT_LINE = "android.ang.action.CRAFT_LINE";
	public final static String ACTION_CRAFT_WIDTH = "android.ang.action.CRAFT_WIDTH";
	public final static String ACTION_CRAFT_SAVE = "android.ang.action.CRAFT_SAVE";
	
	public final static String ACTION_UPDATE_PICTURE = "android.ang.action.UPDATE_PICTURE";
	
	public final static String ACTION_CREATE_PROJECT = "android.ang.action.CREATE_PROJECT";
	public final static String ACTION_CREATE_ENG = "android.ang.action.CREATE_ENG";
	
	public final static String ACTION_LOAD_IMAGE_DEMO = "android.ang.action.LOAD_IMAGE_DEMO";
	
	public final static String ACTION_SEND_MILEAGE_NAME = "android.ang.action.send_mileage_name";
	
	public final static String SUCCESS_SEND_SYSTEM_INFO = "android.ang.action.SUCCESS_SEND_SYSTEM_INFO";
	public final static String FAILED_SEND_SYSTEM_INFO = "android.ang.action.FAILED_SEND_SYSTEM_INFO";
}
