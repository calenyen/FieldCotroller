package com.hwaling.udoo;

import java.util.Scanner;

public class CommData {

	
	private String[] dataRow;
	private String[] item ;
	private String[] value;
    private int size;
    private Datarow[] datalist ; 
    private Datarow Delimitdatarow ;
 
	public CommData(){
		dataRow = new String[100];
		item = new String[100];
		value = new String[100];
		datalist = new Datarow[100];
		Delimitdatarow = new Datarow();
	}
	
    public void DelimitRow(String rawData) {  
    	
    	int i = 0;
    	
       	Scanner scanLay1 = new Scanner(rawData);
        // initialize the string delimiter
        scanLay1.useDelimiter("&");
        // Printing the delimiter used
        //System.out.println("The delimiter use is "+scan.delimiter());
        // Printing the tokenized Strings
        while(scanLay1.hasNext()){
           this.dataRow[i] =  scanLay1.next();
           i++;
            
        }
        
        this.size = i;
        
        
        Scanner scanLay2;
        for (int j = 0; j < i; j++) 
        {     
        	if (dataRow[j] != null){
        scanLay2 = new Scanner(this.dataRow[j]);
        scanLay2.useDelimiter("=");
     
        if (scanLay2.hasNext()){
            this.item[j] = scanLay2.next();
        }     
        if (scanLay2.hasNext()){
            this.value[j] = scanLay2.next();
        }   

        // closing the scanner stream
        scanLay2.close();
        	}
        }
        
    }
	
	public int getSize(){
		return this.size;
	}
	
	public String getdataRow(int i){
		return this.dataRow[i];
	}
	
	public Datarow getDelimitdataRow(int i){
		
		this.Delimitdatarow = this.datalist[i];
		return this.Delimitdatarow;
	}
	
	public String getItem(int i){
		return this.item[i];
	}
	
	public String getValue(int i){
		return this.value[i];
	}	

	public void filldatalist(){
	   
        for (int k = 0; k < this.size; k++) 
        { 

		this.datalist[k].setItem(this.item[k]) ;
		this.datalist[k].setvalue(this.value[k]) ;
        }
        
	}
	

	


}
