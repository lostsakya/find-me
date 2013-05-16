package com.gdg.findme.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.gdg.findme.receiver.LocationHandleReceiver;

/**
 * 发送索要位置的短信给目标手机,并注册一个广播接收者,等着接收目标手机的位置
 * important:当收到目标手机发来的坐标信息后需要及时停止该服务;
 * @author gaoyihang
 *
 */
public class SendTextService extends Service {

	private LocationHandleReceiver locationHandleReceiver;
	private MyBinder myBinder;
	
	@Override
	public void onCreate() {
		locationHandleReceiver=new LocationHandleReceiver();
		myBinder=new MyBinder();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String targetNumber = intent.getStringExtra("targetNumber");
		String smsBody="#location#";
		//发#location#短信,并注册一个短信广播接收者
		SmsManager.getDefault().sendTextMessage(targetNumber, null,smsBody, null, null);
		register();
		return super.onStartCommand(intent, flags, startId);
	}

	//notify ui update 
	private void updateUI(String longtitude,String latitude,String address){
		//TODO 更新UI的操作
	}
	
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}

	
	@Override
	public void onDestroy() {
		unregister();
	}
	
	
	/**
	 * register the broadcast receiver
	 */
	private void register() {
		IntentFilter intent = new IntentFilter();
		intent.setPriority(Integer.MAX_VALUE);
		intent.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(locationHandleReceiver, intent);
	}

	/**
	 * unregister the broadcast receiver
	 */
	private void unregister() {
		unregisterReceiver(locationHandleReceiver);
	}
	
	public class MyBinder extends Binder{
		public void excuteUpdateUI(String longtitude,String latitude,String address) {
			updateUI(longtitude,latitude,address);
		}
	}
	
	
	
}
