package com.hwaling.udoo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.palazzetti.adktoolkit.AdkManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.activeandroid.query.Select;
import com.hwaling.udoo.MainTab01;
import com.hwaling.udoo.MainTab02;
import com.hwaling.udoo.MainTab03;
import com.hwaling.udoo.MainTab04;
import com.hwaling.udoo.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;







//for JSON
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements OnClickListener {
	//For Test
	private static String mymesg="";
	//
	private MainTab01 mTab01;
	private MainTab02 mTab02;
	private MainTab03 mTab03;
	private MainTab04 mTab04;

	/**
	 * 菁窒侐跺偌聽
	 */
	private LinearLayout mTabBtnWeixin;
	private LinearLayout mTabBtnFrd;
	private LinearLayout mTabBtnAddress;
	private LinearLayout mTabBtnSettings;
	/**
	 * 蚚衾勤Fragment輛俴奪燴
	 */
	private FragmentManager fragmentManager;

	
//	private static final String TAG = "UDOO_AndroidADK";
	private AdkManager mAdkManager;
	private String readValue = new String();
	private CommData myData;	
	private ToggleButton buttonLED;
	public static TextView textRcv, textTemp, textHumdy, textConductivity, textTemperature, textMsg, textCalCndct, textCalCl;
	public static ListView tabListView; 
	//private String[] list = new String[20];
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayAdapter<String> listAdapter;
	

	private String url = "http://sensing-lives2.appspot.com/sensorRD";
	private String strATemp = "0.00";
	private String strHmdty = "0.00";

	private String strRawCndct = "0.00";
	private String strRawTemp = "0.00";
	
	private String strCompTemp = "0.00";
	private String strCalCndct = "0.00";
	
	private String strCalCl = "0.00";
	private String strCompCl = "0.00";
	
    private double db_ATemp=0.0, db_Hmdty=0.0, db_RawCndct=0.0, db_Conductivity=0.0, db_RawTemp=0.0, db_CompTemp=0.0, db_CalCndct=0.0,db_CalCl = 0.0,  db_CompCl = 0.0;  
    private int int_RawCndct=0, int_RawTemp = 0;
    private double ParamA[] = { 0.0, 0.0, 0.0 };
    
    // parameter file
    
    private static int arrayNum =1024;
    private static String initFilename = "param.json";
    private byte[] jsonComp = new byte[arrayNum];
    private double compParam[][] = new double[3][6];  // Compensation  value
    private static int s_compTemp = 0;
    private static int s_compCndct = 1;
    private static int s_compCl = 2;
    private static int s_compK = 0;
    private static int s_compA = 1;
    private static int s_compB = 2;
    private static int s_compC = 3;
    private static int s_compMin = 4;
    private static int s_compMax = 5;


    private byte[] jsonCal= new byte[arrayNum];
    private double calParam[][] = new double[2][6]; // calculation value
    private static int calCndct = 0;
    private static int calCl = 1;
    private static int s_calK = 0;
    private static int s_calA = 1;
    private static int s_calB = 2;
    private static int s_calC = 3;
    private static int s_calE1 = 4;
    private static int s_calE2 = 5;
    
    private int int_PollingTime = 0;
    
	private String readString ="";

	//for T431 httppost Json
	private String uploadUrl = "http://10.14.237.105:8001/PLCMW/updatetblTK13Mon.ashx";
	private String paramUrl = "http://10.14.237.105:8001/PLCMW/updatetblTK13Mon.ashx";
	private Timer timer1 = new Timer();
	private Timer timer2 = new Timer();
	private AdkReadSerialTask adkRSTask = new AdkReadSerialTask() ;
	private uploadT431ServerTask uploadTasK = new uploadT431ServerTask();
	
	
	private  Handler TimerHandler2 = new Handler(){  
    	@Override
    	public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 1:  
            	(new Thread(new Runnable() {
                    @Override
                    public void run() {
                      // String mymesg = getHtmlByPost(url);
                    	//saveT431log();
                    	//Send2Arduino();
                    	uploadT431Server();
                    	
                    	
                    	//textMsg.setText(mymesg);
                    
                        //Log.i("Therad",mymesg);
                    }		
            	})).start();
            	//String mymesg = getHtmlByPost(url);
            	//Toast.makeText(getApplicationContext(),mymesg, Toast.LENGTH_SHORT).show();
            break;
            }            
            };
	  };
            	  
	private  Handler TimerHandler = new Handler(){  
    	@Override
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 1:  
            	//readValue = "Temp=25&Humidity=86.0&Opcity=30.3";
                
            	readValue = mAdkManager.readSerial();
                if (readValue != null){
                JSONObject JObj_ADK = null;
                        try {
 
                        	JObj_ADK = new JSONObject(readValue);
                        	db_ATemp= JObj_ADK.getDouble("ATemp");
                        	db_Hmdty= JObj_ADK.getDouble("Hmdty");
                        	
                        	//db_RawCndct = JObj_ADK.getDouble("RawCndct");
                        	int_RawCndct = JObj_ADK.getInt("RawCndct");
                        	db_RawCndct = ADConverter(int_RawCndct,compParam[s_compCndct][s_compMin],compParam[s_compCndct][s_compMax]);
                        	//db_RawTemp = JObj_ADK.getDouble("RawTemp");
                        	int_RawTemp = JObj_ADK.getInt("RawTemp");
                        	//db_RawTemp = ADConverter(int_RawTemp,compParam[s_compTemp][s_compMin],compParam[s_compTemp][s_compMax]);
                        	db_RawTemp = ADConverter(int_RawTemp,0,100);
                        	//Calculate the Conductivity 20150601 Edited by Calen
                        	//db_Conductivity = JObj_ADK.getDouble("Conductivity");
                        	//db_Temperature = JObj_ADK.getDouble("Temperature");
                        	db_CompTemp = db_RawTemp;
                        	calculateResult();
                        	
                        	//double dout=28.965432;   
                        	//BigDecimal bd= new BigDecimal(dout);   
                        	//bd=bd.setScale(4, BigDecimal.ROUND_HALF_UP);// 小數後面四位, 四捨五入   
                        	// 

                        	strATemp = String.format("%.2f",db_ATemp); 
                        	strHmdty = String.format("%.2f",db_Hmdty);
                        	strRawCndct = String.format("%.2f",db_RawCndct);
                        	strRawTemp = String.format("%.2f",db_RawTemp);
                        	strCompTemp = String.format("%.2f",db_CompTemp);
                        	
                        	strCalCndct = String.format("%.2f",db_CalCndct);
                        	
                        	strCalCl = String.format("%.2f",db_CalCl);
                        	db_CompCl = db_CalCl;
                        	strCompCl = String.format("%.2f",db_CompCl);
                        	
                        	JSONObject myjson = packUploadJSON(strATemp,strHmdty,strRawCndct,strCalCndct, strRawTemp, strCompTemp, strCalCl, strCompCl);
                            mymesg = myjson.toString();
                        	
                        	reflashView(readValue,strATemp,strHmdty,strRawCndct,strRawTemp,strCalCndct,strCalCl,mymesg);                   	
                        	
                        } catch (JSONException e) {
                            Log.e("JSON_ADK Parser", "Error parsing data " + e.toString());
                        	strATemp = "0.00";
                        	strHmdty ="0.00";
                        	strRawCndct = "0.00";
                        	strRawTemp = "0.00";
                        	strCalCndct = "0.00";
                        	strCalCl = "0.00";
                        	strCompCl ="0.00";
                        	reflashView(readValue,strATemp,strHmdty,strRawCndct,strRawTemp,strCalCndct,strCalCl,mymesg ); 
                        }
                }else
                {
                    Log.e("ADK_Read", "Error parsing data " + "result=0");
                	strATemp = "0.00";
                	strHmdty ="0.00";
                	strRawCndct = "0.00";
                	strRawTemp = "0.00";
                	strCalCndct = "0.00";
                	strCalCl = "0.00";
                	strCompCl ="0.00";
                	reflashView(readValue,strATemp,strHmdty,strRawCndct,strRawTemp,strCalCndct,strCalCl,mymesg );
                }
                /*        
                myData = new CommData();
                myData.DelimitRow(readValue);
                int i  = myData.getSize();
                
                
                if (i>3) {
                	strTemp = myData.getValue(0).toString().trim();
                	strHmdty = myData.getValue(1).toString().trim();
                	strConductivity = myData.getValue(2).toString().trim();
                	strTemperature = myData.getValue(3).toString().trim();
                	reflashView(readValue,strTemp,strHmdty,strConductivity,strTemperature);
                    
                	// For Upload
                	//JSONObject json = packUploadJSON(strTemp,strHmdty,strConductivity,strTemperature);                 
                	//JSONObject jsonResult = httpPostJsonObject(upLoadUrl, json);
                	//Toast.makeText(getApplicationContext(),jsonResult.toString(), Toast.LENGTH_SHORT).show();
                    
                }
                else
                {
                	strTemp = "0.00";
                	strHmdty ="0.00";
                	strConductivity = "0.0";
                	strTemperature = "0.0";		
                	reflashView(readValue,strTemp,strHmdty,strConductivity,strTemperature); 
                	// For Upload
                	//JSONObject json = packUploadJSON(strTemp,strHmdty,strConductivity,strTemperature);                 
                	//JSONObject jsonResult = httpPostJsonObject(upLoadUrl, json);
                	//Toast.makeText(getApplicationContext(),jsonResult.toString(), Toast.LENGTH_SHORT).show();	
                }
                
                //Toast.makeText(getApplicationContext(),Integer.toString(i) , Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),myData.getItem(0)+"="+myData.getValue(0), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),readValue, Toast.LENGTH_SHORT).show();
                myData = null;
              */

            	
            	break;
            }  
        };  
    };  
    @SuppressLint("NewApi")
	@Override
	
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homemain);
		initViews();
		fragmentManager = getFragmentManager();
		setTabSelection(0);
		//setTxtView();
		mAdkManager = new AdkManager((UsbManager) getSystemService(Context.USB_SERVICE));
		
//		register a BroadcastReceiver to catch UsbManager.ACTION_USB_ACCESSORY_DETACHED action
		registerReceiver(mAdkManager.getUsbReceiver(), mAdkManager.getDetachedFilter());
		
		//buttonLED = (ToggleButton) findViewById(R.id.toggleButtonLED);
		/*
		textRcv = (TextView) findViewById(R.id.textRcv);
		textTemp = (TextView) findViewById(R.id.textTemp);
		textHumdy = (TextView) findViewById(R.id.textHumdy);
		textConductivity = (TextView) findViewById(R.id.TextCndct);
		textTemperature = (TextView) findViewById(R.id.TextSTemp);
		*/
		
		Log.i("ADK manager", "available: " + mAdkManager.serialAvailable());

		timer1.scheduleAtFixedRate(adkRSTask, 1, 1000);
		timer2.scheduleAtFixedRate(uploadTasK, 1, 10000);
		initialParam();
	}
 
	@Override
	public void onResume() {
		super.onResume(); 
		
		mAdkManager.open();
	}
 
	@Override
	public void onPause() {
		super.onPause();
		mAdkManager.close();
	}
	
	@Override
    protected void onDestroy() {
		//adkRSTask.cancel();
		//uploadTasK.cancel();
		timer1.cancel();
		timer2.cancel();
		mAdkManager.close();
        super.onDestroy();
        unregisterReceiver(mAdkManager.getUsbReceiver());
    }
 
	/*
	private void setTxtView(){
		textRcv = (TextView) findViewById(R.id.textRcv);
		textTemp = (TextView) findViewById(R.id.textTemp);
		textHumdy = (TextView) findViewById(R.id.textHumdy);
		textConductivity = (TextView) findViewById(R.id.TextCndct);
		textTemperature = (TextView) findViewById(R.id.TextSTemp);
		textMsg = (TextView) findViewById(R.id.txtMsg);
	}
	*/
	
	private void initViews()
	{

		mTabBtnWeixin = (LinearLayout) findViewById(R.id.id_tab_bottom_weixin);
		mTabBtnFrd = (LinearLayout) findViewById(R.id.id_tab_bottom_friend);
		mTabBtnAddress = (LinearLayout) findViewById(R.id.id_tab_bottom_contact);
		mTabBtnSettings = (LinearLayout) findViewById(R.id.id_tab_bottom_setting);

		mTabBtnWeixin.setOnClickListener(this);
		mTabBtnFrd.setOnClickListener(this);
		mTabBtnAddress.setOnClickListener(this);
		mTabBtnSettings.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.id_tab_bottom_weixin:
			setTabSelection(0);
			break;
		case R.id.id_tab_bottom_friend:
			setTabSelection(1);
			break;
		case R.id.id_tab_bottom_contact:
			setTabSelection(2);
			break;
		case R.id.id_tab_bottom_setting:
			setTabSelection(3);
			break;

		default:
			break;
		}
	}

	/**
	 * 跦擂換腔index統杅懂扢离恁笢腔tab珜﹝
	 * 
	 */
	@SuppressLint("NewApi")
	private void setTabSelection(int index)
	{
		// 笭离偌聽
		resetBtn();
		// 羲珨跺Fragment岈昢
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 珂笐紲裁垀衄腔Fragmentㄛ眕滅砦衄嗣跺Fragment珆尨婓賜醱奻腔錶
		hideFragments(transaction);
		switch (index)
		{
		case 0:
			// 絞萸僻賸秏洘tab奀ㄛ蜊曹諷璃腔芞睿恅趼晇伎
			((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
					.setImageResource(R.drawable.tab_weixin_pressed);
			if (mTab01 == null)
			{
				// 彆MessageFragment峈諾ㄛ寀斐膘珨跺甜氝樓善賜醱奻
				mTab01 = new MainTab01();
				//setTxtView();
				
				transaction.add(R.id.id_content, mTab01);
			} else
			{
				// 彆MessageFragment祥峈諾ㄛ寀眻諉蔚坳珆尨堤懂
				transaction.show(mTab01);
			}
			break;
		case 1:
			// 絞萸僻賸秏洘tab奀ㄛ蜊曹諷璃腔芞睿恅趼晇伎
			((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
					.setImageResource(R.drawable.tab_weixin_pressed);
			if (mTab02 == null)
			{
				// 彆MessageFragment峈諾ㄛ寀斐膘珨跺甜氝樓善賜醱奻
				mTab02 = new MainTab02();
				transaction.add(R.id.id_content, mTab02);
			} else
			{
				// 彆MessageFragment祥峈諾ㄛ寀眻諉蔚坳珆尨堤懂
				transaction.show(mTab02);
			}
			break;
		case 2:
			// 絞萸僻賸雄怓tab奀ㄛ蜊曹諷璃腔芞睿恅趼晇伎
			((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
					.setImageResource(R.drawable.tab_weixin_pressed);
			if (mTab03 == null)
			{
				// 彆NewsFragment峈諾ㄛ寀斐膘珨跺甜氝樓善賜醱奻
				mTab03 = new MainTab03();
				transaction.add(R.id.id_content, mTab03);
			} else
			{
				// 彆NewsFragment祥峈諾ㄛ寀眻諉蔚坳珆尨堤懂
				transaction.show(mTab03);
			}
			break;
		case 3:
			// 絞萸僻賸扢离tab奀ㄛ蜊曹諷璃腔芞睿恅趼晇伎
			((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
					.setImageResource(R.drawable.tab_weixin_pressed);
			if (mTab04 == null)
			{
				// 彆SettingFragment峈諾ㄛ寀斐膘珨跺甜氝樓善賜醱奻
				mTab04 = new MainTab04();
				transaction.add(R.id.id_content, mTab04);
			} else
			{
				// 彆SettingFragment祥峈諾ㄛ寀眻諉蔚坳珆尨堤懂
				transaction.show(mTab04);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 壺裁垀衄腔恁笢袨怓﹝
	 */
	private void resetBtn()
	{
		/*
		((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
				.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
				.setImageResource(R.drawable.tab_find_frd_normal);
		((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
				.setImageResource(R.drawable.tab_address_normal);
		((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
				.setImageResource(R.drawable.tab_settings_normal);
				*/
		((ImageButton) mTabBtnWeixin.findViewById(R.id.btn_tab_bottom_weixin))
		.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnFrd.findViewById(R.id.btn_tab_bottom_friend))
		.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnAddress.findViewById(R.id.btn_tab_bottom_contact))
		.setImageResource(R.drawable.tab_weixin_normal);
		((ImageButton) mTabBtnSettings.findViewById(R.id.btn_tab_bottom_setting))
		.setImageResource(R.drawable.tab_weixin_normal);
	}

	/**
	 * 蔚垀衄腔Fragment飲离峈笐紲袨怓﹝
	 * 
	 * @param transaction
	 *            蚚衾勤Fragment硒俴紱釬腔岈昢
	 */
	@SuppressLint("NewApi")
	private void hideFragments(FragmentTransaction transaction)
	{
		if (mTab01 != null)
		{
			transaction.hide(mTab01);
		}
		if (mTab02 != null)
		{
			transaction.hide(mTab02);
		}
		if (mTab03 != null)
		{
			transaction.hide(mTab03);
		}
		if (mTab04 != null)
		{
			transaction.hide(mTab04);
		}
		
	}	
	// ToggleButton method - send message to the SAM3X with the AdkManager
	public void blinkLED(View v) {
		if (buttonLED.isChecked()) { 
			// writeSerial() allows you to write a single char or a String object.
			
			
			mAdkManager.writeSerial("1");
		} else {
			mAdkManager.writeSerial("0"); 
		}	
	}
	
	public void Send2Arduino() {
            
		    JSONObject json = packArduinoJSON();
			mAdkManager.writeSerial(json.toString());

	}
	
	//手動送http post 
	public void sendHttpPost(View v) {
		//uploadGAE(strTemp,strHmdty);
		String Mesg = getHtmlByPost(this.url);
		Toast.makeText(getApplicationContext(),Mesg, Toast.LENGTH_SHORT).show();
	}
	
    private class AdkReadSerialTask extends TimerTask {  
        @Override  
        public void run() {
            Message message = new Message();  
            message.what = 1;  
            TimerHandler.sendMessage(message);
        	
        }     
    }   
    
    private class uploadT431ServerTask extends TimerTask {  
        @Override  
        public void run() {
            Message message = new Message();  
            message.what = 1;  
            TimerHandler2.sendMessage(message);
    	
        }     
    }
 
    private void reflashView(String mesg, String Temp, String Humdy, String Conductivity, String Temperature, String CalCndct, String CalCl,String message){
	   	   textRcv.setText(mesg.trim()); 
	       textTemp.setText(Temp);
	       textHumdy.setText(Humdy);
	       textConductivity.setText(Conductivity);
	       textTemperature.setText(Temperature);
	       textCalCndct.setText(CalCndct);
	       textCalCl.setText(CalCl);
	       
	  		Date dNow  =  new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz"); 	
	   
	        String strHead = formatter.format(dNow).toString()+": ";
	       // textMsg.setText(String.valueOf(compParam[0][1])+":"+ String.valueOf(compParam[0][5])+ strHead+ mymesg.trim() );
	        textMsg.setText(mymesg + "---");
	       
   }
    
    public String getHtmlByPost(String _url){    
       
       String result = "";
       HttpClient client = new DefaultHttpClient();
       
       try {
           
                       
           HttpPost post = new HttpPost(_url);
           
           //參數
    
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("ATemp", this.strATemp));
               params.add(new BasicNameValuePair("Hmdty", this.strHmdty));
               
               UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
               post.setEntity(ent);
         
           
           HttpResponse responsePOST = client.execute(post); 
           
           HttpEntityWrapper resEntity = (HttpEntityWrapper) responsePOST.getEntity();
           
           if (resEntity != null) {    
               result = EntityUtils.toString(resEntity);
           }
           
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
          client.getConnectionManager().shutdown();
          
          }
       return result;
   }
  
    public JSONObject httpPostJsonObject(String _url, JSONObject _updateJobj){
   
    String result = "";
	InputStream inputStream = null;
	HttpClient httpclient = new DefaultHttpClient();  
    
	try{
	     HttpPost httppost = new HttpPost(_url);
	     String json = "";
	     json = _updateJobj.toString();
		 StringEntity se = new StringEntity(json);
		 httppost.setEntity(se);
	     httppost.setHeader("Accept", "application/json");
	     httppost.setHeader("Content-type", "application/json");
	           
	     HttpResponse httpResponse = httpclient.execute(httppost);
	      
	     //Judge if the website is ok?
	     if ( httpResponse.getStatusLine( ).getStatusCode( ) == 200 ){
	     
	     inputStream = httpResponse.getEntity().getContent();
	       if(inputStream != null){
	          result =  convertInputStreamToString(inputStream);
	          //result =  "{\"result\":\"" + result +"\"}";
	       }
	       else
	          result = "";
	       }  
	       
	      } catch (Exception e) {
	          Log.d("InputStream", e.getLocalizedMessage());   
	          result ="";
         }
    	
	   
	   JSONObject json = null;
	   if (result != ""){
	     try {
	    
	           json = new JSONObject(result);
	         } catch (JSONException e) {
	            e.printStackTrace();
	         }
	         // 11. return result
	   }
	         return json;	   
   }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
       BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
       String line = "";
       String result = "";
       while((line = bufferedReader.readLine()) != null)
           result += line;

       inputStream.close();
       return result;
   }
   
   public void saveData(View view){
		    
	        String name = "calen"+ Integer.toString((int)(Math.random()* 10000)); //personNameEditText.getText().toString();
	        int age = (int)(Math.random()* 10000); //Integer.parseInt(personAgeEditText.getText().toString());

	        Person person = new Person(name, age);
	        person.save();
 }
   
   public void showPLCMMon(View view)
  {
	  Select select = new Select(); 
	  mymesg ="";
  	  
  	try{
  	  ArrayList<tbMon> tbMonitor = select.all().from(tbMon.class).execute();
      for (tbMon Monitor : tbMonitor) {
    	  //list[i] =  person.personName;
	        
    	  SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz"); 	
	 	   
	      String strHead = formatter.format(Monitor.myDate).toString()+": ";
    	  list.add(strHead+"Abmient Temp:" + Double.toString(Monitor.AmbientTemp));
    	  //i++;
      }	  
      listAdapter =   new ArrayAdapter<String>(this,R.layout.listitem,list); 
      tabListView.setAdapter(listAdapter);

  		
  	} catch (Exception e) {
	    // Log exception
  		mymesg = e.getMessage().toString();
  		mymesg = mymesg.trim();
	} 
	  
	  

  }
  
   public void showAll(View view)
  {
      // Create an object of Select to issue a select query

      Select select = new Select();

      // Call select.all() to select all rows from our table which is
      // represented by Person.class and execute the query.

      // It returns an ArrayList of our Person objects where each object
      // contains data corresponding to a row of our database.

      ArrayList<Person> people = select.all().from(Person.class).execute();

      // Iterate through the ArrayList to get all our data. We ll simply add
      // all the data to our StringBuilder to display it inside a Toast.
/*
      StringBuilder builder = new StringBuilder();
      for (Person person : people) {
          builder.append("Name: ")
                  .append(person.personName)
                  .append(" Age: ")
                  .append(person.personAge)
                  .append("\n");
      }

      Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
  */ 
      
      //int i = 0;
      for (Person person : people) {
    	  //list[i] =  person.personName;
    	  list.add(person.personName + ":" + Integer.toString(person.personAge));
    	  //i++;
      }
     // Toast.makeText(this, Integer.toString(i) + list[i-1].toString(), Toast.LENGTH_LONG).show();
     // listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
      //listAdapter =   new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list); 
      listAdapter =   new ArrayAdapter<String>(this,R.layout.listitem,list); 
      tabListView.setAdapter(listAdapter);
                
      
  }

   private JSONObject packUploadJSON(String ATemp, String Humdy, String RawCndct, String Conductivity, String RawTemp, String CompTemp,String rowHCL, String HCL ){  
	 
	  /*
	  Map map = new HashMap();
	  map.put("rowTemp", Temperature);
	  //compensated temp is not calculated at this time
	  map.put("Temperature", "0"); 
	  
	  map.put("rowCndct", Conductivity);
	//compensated temp is not calculated at this time
	  map.put("Conductivity", "0");
	  
	  map.put("AmbientTemp", Temp);
	  map.put("AmbientMoisture",Humdy);
	  */
	  
	  
	  JSONObject json = new JSONObject();
      try {

      //json.put("rowTemp", Temperature);
    	  json.put("rowTemp", RawTemp);
	  //compensated temp is not calculated at this time
	  json.put("Temperature", CompTemp); 
	  
	  json.put("rowCndct", RawCndct);
	//compensated temp is not calculated at this time
	  json.put("Conductivity", Conductivity);
	  
	  json.put("AmbientTemp", ATemp);
	  json.put("AmbientMoisture", Humdy);
	  
	  json.put("rowHCL", rowHCL);
	  json.put("HCL", HCL);
      } catch (Exception e)
      {	  
      }
        
	  return json;
  }
  
   private JSONObject packArduinoJSON(){   
	 
	  JSONObject json = new JSONObject();
      try {

    		JSONArray Param1 = new JSONArray();
    		//db_ATemp=0.0, db_Hmdty=0.0, db_RawCndct=0.0, db_Conductivity=0.0, db_RawTemp=0.0, db_Temperature=0.0;
    		ParamA[0]=Math.random()* 1000;
    		ParamA[1]=Math.random()* 1000;
    	    ParamA[2]=Math.random()* 1000;
    		Param1.put(ParamA[0]);
    		Param1.put(ParamA[1]);
    		Param1.put(ParamA[2]);

    		json.put("Param1", Param1);
  
      } catch (Exception e)
      {	  
      }
        
	  return json;
  }
  
   private void saveT431log(){
	  Date dNow  =  new Date(); 
	  mymesg ="";
  	  
  	try{
  		
	  tbMon PLCMW_Mon = new tbMon(dNow, db_RawTemp, db_CompTemp, db_RawCndct, db_Conductivity, db_ATemp, db_Hmdty);
      PLCMW_Mon.save();  
  	} catch (Exception e) {
	    // Log exception
  		mymesg = e.getMessage().toString();
  		mymesg = mymesg.trim();
	} 
  }
  
   private void uploadT431Server(){
	  mymesg ="";
  	  
  	try{
  		JSONObject json = packUploadJSON(strATemp,strHmdty,strRawCndct,strCalCndct, strRawTemp, strCompTemp, strCalCl, strCompCl); 
  		JSONObject jsonResult = httpPostJsonObject(uploadUrl, json); 	
  		if (jsonResult == null){
  		mymesg = json.toString();
  		}else{
  		mymesg = jsonResult.toString();	
  		}
       
  		
  	} catch (Exception e) {
	    // Log exception
  		mymesg = e.getMessage().toString();
  		mymesg = mymesg.trim();
	} 	
  	//JSONObject jsonResult = httpPostJsonObject(uploadUrl, json);
  }
   
   
   private void getParamofT431Server(){
	
	// Determine if the time is up to get paramatem from t431 server.   
	if (int_PollingTime > 50){
		  

	   
	   mymesg ="";
  	  
  	try{
  		JSONObject json = packUploadJSON(strATemp,strHmdty,strRawCndct,strCalCndct, strRawTemp, strCompTemp, strCalCl, strCompCl); 
  		JSONObject jsonResult = httpPostJsonObject(paramUrl, json); 	
  		if (jsonResult == null){
  		mymesg = jsonResult.toString();
  		}else{
  		mymesg = jsonResult.toString();	
  		}
        mymesg = mymesg.trim();
        
  		
  	} catch (Exception e) {
	    // Log exception
  		mymesg = e.getMessage().toString();
  		mymesg = mymesg.trim();
	} 	
  	//JSONObject jsonResult = httpPostJsonObject(uploadUrl, json);
  	
	  }
  }
   
  
   private void uploadGAE (String Temp,String Hmdty){
	  HttpClient httpClient = new DefaultHttpClient();
	  HttpPost httpPost = new HttpPost("http://sensing-lives.appspot.com/sensorRD");
      
	  List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
	  nameValuePair.add(new BasicNameValuePair("Temp", Temp));
	  nameValuePair.add(new BasicNameValuePair("Hmdty", Hmdty));
	  	 
	  try {
	      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
	 
	} catch (UnsupportedEncodingException e) 
	{
	     e.printStackTrace();
	}

	  try {
		  HttpResponse response = httpClient.execute(httpPost);
		    // write response to log
		    Log.d("Http Post Response:", response.toString());
		} catch (ClientProtocolException e) {
		    // Log exception
		    e.printStackTrace();
		} catch (IOException e) {
		    // Log exception
		    e.printStackTrace();
		}
	  
	  

	  
	  /*
	  try {
		    response = httpClient.execute(httpPost);
		    // write response to log
		   // Log.d("Http Post Response:", response.toString());
		} catch (ClientProtocolException e) {
		    // Log exception
		   // e.printStackTrace();
		} catch (IOException e) {
		    // Log exception
		   // e.printStackTrace();
		}
	  
	  finally {  
    	   httpClient = null;
    	   httpPost = null;
    	   nameValuePair = null;
		}
		*/
  }

   private void initialParam() {

	  readString = readFromFile(initFilename);
      try {
          
          /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
    	  JSONObject jsonParam = new JSONObject(readString);
           
          /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
          /*******  Returns null otherwise.  *******/
          JSONArray jsonParamComp = jsonParam.optJSONArray("ParamComp");       
          /*********** Process each JSON Node ************/
          int lengthJsonParamComp = jsonParamComp.length();  
          for(int i=0; i < lengthJsonParamComp; i++) 
          {
              /****** Get Object for each JSON node.***********/
              JSONObject jsonChildParamComp = jsonParamComp.getJSONObject(i);           
              /******* Fetch node values **********/
              compParam[i][0] = jsonChildParamComp.optDouble("K");
              compParam[i][1] = jsonChildParamComp.optDouble("A");
              compParam[i][2] = jsonChildParamComp.optDouble("B");
              compParam[i][3] = jsonChildParamComp.optDouble("C");
              compParam[i][4] = jsonChildParamComp.optDouble("Min");
              compParam[i][5] = jsonChildParamComp.optDouble("Max");
         }
           
          JSONArray jsonParamCal = jsonParam.optJSONArray("ParamCal");       
          /*********** Process each JSON Node ************/
          int lengthJsonParamCal = jsonParamCal.length();  
          for(int i=0; i < lengthJsonParamCal; i++) 
          {
              /****** Get Object for each JSON node.***********/
              JSONObject jsonChildParamCal = jsonParamCal.getJSONObject(i);           
              /******* Fetch node values **********/
              calParam[i][0] = jsonChildParamCal.optDouble("K");
              calParam[i][1] = jsonChildParamCal.optDouble("A");
              calParam[i][2] = jsonChildParamCal.optDouble("B");
              calParam[i][3] = jsonChildParamCal.optDouble("C");
              calParam[i][4] = jsonChildParamCal.optDouble("E1");
              calParam[i][5] = jsonChildParamCal.optDouble("E2");
         }
          
          /************ Show Output on screen/activity **********/


           
      } catch (JSONException e) {

          e.printStackTrace();
      }

	  
	    
   }
   
   public static String AssetJSONFile (String filename, Context context) throws IOException {
       AssetManager manager = context.getAssets();
       InputStream file = manager.open(filename);
       byte[] formArray = new byte[file.available()];
       file.read(formArray);
       file.close();

       return new String(formArray);
   }
   
   //2015.06.17 Read file to String , by Calen
   private String readFromFile(String filename) {

	    String ret = "";

	    try {
	        InputStream inputStream = openFileInput(filename);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
   
   //calculate the measurement result from raw measured data, 2015.06.18 by calen.
   private void calculateResult(){
	  // 導電度(校) = 導電度(測)/(1+α*(T-25))
	  // Conductivity(calibration) = Conductivity(measured)/(1+α*(T-25))
	   double db_constA = (db_RawTemp-25)*calParam[0][4];
	   db_constA = db_constA + 1.0;
	   db_CalCndct = db_RawCndct/db_constA;
	   
	 //氯離子(測) = -7E-05*【導電度(校)】^2 + 0.1561*【導電度(校)】- 0.0167
	   double db_X2,db_X1;
	   
	   db_X2 = Math.pow(db_CalCndct,2)*calParam[1][2];
	   db_X1 = calParam[1][1]*db_CalCndct;
	   db_CalCl =db_X2 +  db_X1 + calParam[1][0];

   }
   
   
   
  private double ADConverter(int int_RawValue, double db_StartValue, double db_RangeValue){
	double result = 0.0;
    result = int_RawValue/65536.0;  //
	result = result*db_RangeValue;
	result = result + db_StartValue;
	return result;
  }
   
  private String paramCheck(){
	String mymesg;
	mymesg = String.valueOf(compParam[0][1]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[0][1]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[0][2]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[0][3]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[0][4]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[0][5]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][0]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][1]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][2]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][3]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][4]);
  	mymesg = mymesg + ":" + String.valueOf(compParam[1][5]);                      	

  	mymesg = mymesg + ":" + String.valueOf(calParam[0][0]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[0][1]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[0][2]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[0][3]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[0][4]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[0][5]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][0]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][1]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][2]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][3]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][4]);
  	mymesg = mymesg + ":" + String.valueOf(calParam[1][5]);
  	return mymesg;
  }
  
  
}
