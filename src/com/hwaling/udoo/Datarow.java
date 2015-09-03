package com.hwaling.udoo;

public class Datarow { 
    	
    	private String item ;
    	private String value ;
    
    	public Datarow(){
    		item = new String();
    		value = new String();	
    	}
    	
    	public String getitem(){
    		return  this.item;
    	}	
    	public String getvalue(){
    		return  this.value;
    	}
    	
    	public void setItem(String myitem){
    		this.item = myitem;
    	}

    	public void setvalue(String myvalue){
    		this.value = myvalue;
    	}
    	
 
    }
