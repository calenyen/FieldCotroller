package com.hwaling.udoo;

import com.activeandroid.ActiveAndroid;
import android.app.Application;
 


public class FCApplication extends Application{
	  @Override
	    public void onCreate() {
	        // TODO Auto-generated method stub
	        super.onCreate();
	        
	        //Notice this initialization code here
	        ActiveAndroid.initialize(this);
	    }
	 
	  /*
	    @Override
	    public void onTerminate() {
	    	super.onTerminate();
	    	ActiveAndroid.dispose();
	    }
     */
}
