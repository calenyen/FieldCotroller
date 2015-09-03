package com.hwaling.udoo;


import java.util.Date;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
 
//Notice how we specified the name of the table below
@Table(name = "tbMonitor")
public class tbMon extends Model {
 
    // Notice how we specified the name of our column here
    @Column(name = "rawTemp")
    public double rawTemp;
    
    @Column(name = "Temperature")
    public double Temperature;

    @Column(name = "rawCndct")
    public double rawCndct;
    
    @Column(name = "Conductivity")
    public double Conductivity;
    
    @Column(name = "AmbientTemp")
    public double AmbientTemp;
    
    @Column(name = "AmbientHmdty")
    public double AmbientHmdty;
    
    @Column(name = "Date")
    public Date myDate;
    
    public tbMon() {
        // Notice how super() has been called to perform default initialization
        // of our Model subclass
        super();
    }
 
    public tbMon(Date myDate, double rawTemp, double Temperature, double rawCndct, double Conductivity, double AmbientTemp, double AmbientHmdty) {
        super();
        
        this.myDate = myDate;
        this.rawTemp = rawTemp;
        this.Temperature = Temperature;
        this.rawCndct = rawCndct;
        this.Conductivity = Conductivity;
        this.AmbientTemp = AmbientTemp;
        this.AmbientHmdty = AmbientHmdty;
        
    }

}
