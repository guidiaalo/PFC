package com.mOpenXC;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Sinais extends Activity{
	
	
	private ListView mListview;
	private ArrayList<String> mArrayListSinais;
	private ArrayAdapter<String> mAdapterSinais;
	private String sinal = "HOLA";
	
	
	@Override
	//inicializar variables en OnCreate
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sinais);
		
		mListview = (ListView) this.findViewById(R.id.listView);
		mArrayListSinais = new ArrayList<String>();
		//mAdapterSinais = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mArrayListSinais);
		
	}
	
	
	
	public class receiveSinais extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle extras = getIntent().getExtras();
		    String s = extras.getString("dados");
		    Log.i("tag", sinal);
			mArrayListSinais.add("steering_wheel_angle: " + sinal);
			mAdapterSinais = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, mArrayListSinais);
			mListview.setAdapter(mAdapterSinais);
		}
		
	}
	
	

}
