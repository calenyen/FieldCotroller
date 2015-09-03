package com.hwaling.udoo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwaling.udoo.MainActivity;
import com.hwaling.udoo.R.id;


@SuppressLint("NewApi")
public class MainTab01 extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View myInflatedView = inflater.inflate(R.layout.main_tab_01, container,false);
		
		MainActivity.textRcv = (TextView) myInflatedView.findViewById(R.id.textRcv);
		MainActivity.textTemp = (TextView) myInflatedView.findViewById(R.id.textTemp);
		MainActivity.textHumdy = (TextView) myInflatedView.findViewById(R.id.textHumdy);
		MainActivity.textConductivity = (TextView) myInflatedView.findViewById(R.id.TextCndct);
		MainActivity.textTemperature = (TextView) myInflatedView.findViewById(R.id.TextSTemp);
		MainActivity.textMsg = (TextView) myInflatedView.findViewById(R.id.txtMsg);
		MainActivity.textCalCndct = (TextView) myInflatedView.findViewById(R.id.textCalCndct);
		MainActivity.textCalCl = (TextView) myInflatedView.findViewById(R.id.textCalCl);
		
		
		//return inflater.inflate(R.layout.main_tab_01, container, false);
		return myInflatedView;

	}

}
