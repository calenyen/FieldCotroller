package com.hwaling.udoo;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.hwaling.udoo.MainActivity;

// import com.example.mainframework02.R;

@SuppressLint("NewApi")
public class MainTab02 extends Fragment
{

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View myInflatedView = inflater.inflate(R.layout.main_tab_02, container, false);
		
		MainActivity.tabListView = (ListView) myInflatedView.findViewById(R.id.listView1);
		
		return myInflatedView;

	}

}
