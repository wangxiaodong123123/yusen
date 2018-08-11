package com.ysdata.steelarch.cloud.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class SharedView {

    public static final String EXIT = "com.iximo.www.iximo.EXIT";
    
    private static Toast toast = null;
    private static Context toastContext = null;

    private static Handler mHandler;
    private static boolean shouldShow = false;
    private static ProgressDialog mDialog = null;

    public static void showToastText(Context context, CharSequence text, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        if (toastContext == context) {
            toast.setText(text);
            toast.setDuration(duration);
        } else {
            toastContext = context;
            toast = Toast.makeText(context, text, duration);
        }
        toast.show();
    }
    
    public static void showToastText(Context context, int resId, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        if (toastContext == context) {
            toast.setText(resId);
            toast.setDuration(duration);
        } else {
            toastContext = context;
            toast = Toast.makeText(context, resId, duration);
        }
        toast.show();
    }
    
    public static void showProgressBar(Activity activity, String msg) {
        // Log.e("----showOnlineDataProgressBar----", "-----  start  -------");
        SharedView.showProgressBar(activity, "", msg, true);
    }

    public static void showOnlineDataProgressBar(Activity activity) {
        // Log.e("----showOnlineDataProgressBar----", "-----  start  -------");
        SharedView.showProgressBar(activity, "", "正在读取网络数据...\n请稍后", true);
    }

    public static void showOfflineDataProgressBar(Activity activity) {
        // Log.e("----showOfflineDataProgressBar----", "-----  start  -------");
        SharedView.showProgressBar(activity, "", "正在读取数据...\n请稍后", false);
    }

    public static void showHandlingDataProgressBar(Activity activity) {
        // Log.e("----showHandlingDataProgressBar----",
        // "-----  start  -------");
        SharedView.showProgressBar(activity, "", "正在处理数据...\n请稍后", false);
    }

    public static void showProgressBar(final Activity activity, final String title,
                                       final String message, final boolean shouldShowInTitleBar) {
        shouldShow = true;
        Thread t = new Thread() {
            public void run() {
                Looper.prepare();
                final ProgressDialog dialog = new ProgressDialog(activity);
                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        // process incoming messages here
                    	switch (msg.what) {
                    	case 1:
                    		dialog.cancel();
                    		activity.setProgressBarIndeterminateVisibility(false);
                    		shouldShow = false;
                    		break;
                    	case 2:
                    		dialog.setMessage((String)msg.obj);
                    		dialog.show();
                            activity.setProgressBarIndeterminateVisibility(shouldShowInTitleBar);
                            break;
                    	}
                    }
                };
                dialog.setTitle(title);
                dialog.setMessage(message);
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                if (shouldShow) {
                    dialog.show();
                    activity.setProgressBarIndeterminateVisibility(shouldShowInTitleBar);
                }
                if (!shouldShow) {
                	mDialog = null;
                    dialog.cancel();
                    activity.setProgressBarIndeterminateVisibility(false);
                }
                Looper.loop();
            }
        };
        t.start();
    }
    
    public static void showDynamicMessage(String msg) {
    	 Message message = new Message();
    	 message.what = 2;
    	 message.obj = msg;
         if (mHandler != null) {
             mHandler.sendMessage(message);
         } else {
             shouldShow = false;
         }
    }

    public static void cancelProgressBar() {
        // Log.e("----cancelProgressBar----", "-----  end  -------");
        Message msg = new Message();
        msg.what = 1;
        if (mHandler != null) {
            mHandler.sendMessage(msg);
        } else {
            shouldShow = false;
        }
    }
    
    public static void showsAlertDialog(Activity activity, String msg) {
		new  AlertDialog.Builder(activity)    
        .setTitle("提示" )
        .setMessage(msg )  
        .setPositiveButton("确定" ,  null )
        .create()
        .show();
    }
    
    public static void showUserExpiredAlertDialog(Activity activity) {
    	if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
    		new  AlertDialog.Builder(activity)    
            .setTitle("提示" )
            .setMessage("用户已过期，请重新登录！" )  
            .setPositiveButton("确定" ,  null )
            .create()
            .show();
    	}
    }

    public static void showsExitDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setTitle("确定退出程序？").setPositiveButton("确定",
            new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(EXIT);
                    activity.sendBroadcast(broadcastIntent);
                }

            }).setNegativeButton("取消", null).create().show();
    }
}
