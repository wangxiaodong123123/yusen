package com.ysdata.steelarch.cloud.util;

public class ConstDef {
	public static String PROJECT_NAME = "YS200/SteelArch";
	public static String PKG_NAME = "com.ysdata.steelarch";
	
	public static int DB_FIRST_VERSION = 1;
	public static int DB_VERSION = 1;
	
	public static String DATABASE_PATH = "/" + PROJECT_NAME + "/database/";
	public static String KEY_SHAREPREF_CONTRACTSECTION_SYNCTIME = "ContractSectionSyncTime";
	public static String KEY_SHAREPREF_SUBPROJECT_SYNCTIME = "SubProjectSyncTime";
	public static String KEY_SHAREPREF_SUBPROJECTSECTION_SYNCTIME = "SubProjectSectionSectionSyncTime";
	public static String KEY_SHAREPREF_IMAGES_SYNCTIME = "SubProjectImagesSyncTime";
	public static String KEY_SHAREPREF_SUBPROJECTPOINT_SYNCTIME = "SubProjectPointSyncTime";
	public static String KEY_SHAREPREF_SUBPROJECTPOINT_GROUTING_DATA_SYNCTIME = "PointGroutingDataSyncTime";

	public static float HOLD_UP_RATE = (float) 0.8;

	public static int SYNCTIME_TYPE_CONTRACTSECTION = 1;
	public static int SYNCTIME_TYPE_SUBPROJECT = 2;
	public static int SYNCTIME_TYPE_SUBPROJECTSECTION = 3;
	public static int SYNCTIME_TYPE_STEELARCH_DEL_DATA = 4;
	public static int SYNCTIME_TYPE_STEELARCH_CRAFT_DATA = 5;
	public static int SYNCTIME_TYPE_STEELARCH_COLLECT_DATA = 6;
	public static int SYNCTIME_TYPE_BLENDER_DATA = 10;
	
	public static String SYNCTIME_NAME_CONTRACTSECTION = "";
	
	public static final String EXPIRED_RESPONSE_STRING = "Forbidden";
	public static final int EXPIRED_RESPONSE_CODE = 403;
}
