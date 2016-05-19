package com.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.synapse.thumbimpression.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends Activity
{
	final File mFile = new File("/sys/class/power_supply/usb/device/CONTROL_GPIO114");
	private long splashDelay = 6000;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		openVoltage();
		TimerTask timertask = new TimerTask(){

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				Intent it = new Intent(getApplicationContext(),FpSDKSampleP41MActivity.class);
				it.putExtra("first", true);
				startActivity(it);
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}};
		Timer timer = new Timer();
		timer.schedule(timertask, splashDelay);
	}
	
	public void openVoltage()
	{
		try {
	      	 FileReader inCmd = new FileReader(mFile);
	      	 int i = inCmd.read();
	      	 inCmd.close();
	       }
	       catch (Exception e) {
	           Log.e("wuyb","wuyb--open--write error");
	       }
	}
	
	public void closeVoltage()
	{
		FileWriter closefr;
        try {
            closefr = new FileWriter(mFile);
            closefr.write("1"); 
            closefr.close();
        }
        catch (Exception e) {
            Log.e("wuyb","wuyb--close--write error");
        }
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
