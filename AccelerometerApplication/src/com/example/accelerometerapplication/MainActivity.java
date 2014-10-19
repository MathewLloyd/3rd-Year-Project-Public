package com.example.accelerometerapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.support.v7.app.ActionBarActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 600;
	private boolean record = false;
	private String accelData = " ";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		    senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		    senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
	    super.onPause();
	    senSensorManager.unregisterListener(this);
	}
	protected void onResume() {
	    super.onResume();
	    senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	public void btnRecord_OnClick(View view){
		if(record == false){
			record = true;
		}else{
			record = false;
			WriteData(accelData);
		}
		
		Button btn = (Button) findViewById(R.id.btnRecord);
		if(record == false){
			btn.setText("Record Data");
		}else{
			btn.setText("Stop Recording");
		}
		
		
	}
	public void WriteData(String data){
		String filename = "filename.txt";
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		FileOutputStream fos;
		byte[] data2 = data.getBytes();
		try {
		    fos = new FileOutputStream(file);
		    fos.write(data2);
		    fos.flush();
		    fos.close();
		} catch (FileNotFoundException e) {
		    // handle exception
		} catch (IOException e) {
		    // handle exception
		}
	}
	public void onAccelerationChanged(float x, float y, float z){
		TextView labelX = (TextView)findViewById(R.id.lableXView);
		TextView labelY = (TextView)findViewById(R.id.labelYVal);
		TextView labelZ = (TextView)findViewById(R.id.labelZView);

		
		labelX.setText(String.valueOf(x));
		labelY.setText(String.valueOf(y));
		labelZ.setText(String.valueOf(z));
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor mySensor = event.sensor;
		
		 if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		        float x = event.values[0];
		        float y = event.values[1];
		        float z = event.values[2];
		 
		        long curTime = System.currentTimeMillis();
		 
		        if ((curTime - lastUpdate) > 100) {
		            long diffTime = (curTime - lastUpdate);
		            lastUpdate = curTime;
		            
		            onAccelerationChanged(x,y,z);
		            
		            if(record == true){
		            	
		            		accelData += x + "," + y +","+ z + ";"; 
						
		            }
		        }
		    }

		}
}
