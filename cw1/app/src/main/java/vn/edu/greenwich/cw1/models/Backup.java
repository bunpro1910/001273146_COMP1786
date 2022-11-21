package vn.edu.greenwich.cw1.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Backup implements Serializable {
    protected Date _date;
    protected String _deviceName;
    protected String _Userid ;
    protected ArrayList<Trip> _trips;
    protected ArrayList<TripItem> _tripItems;

    public Backup(Date date, String deviceName,String userid, ArrayList<Trip> trips, ArrayList<TripItem> tripItems) {
        _date = date;
        _deviceName = deviceName;
        _Userid =userid ;
        _trips = trips;
        _tripItems = tripItems;
    }

    public void setDate(Date date) {
        _date = date;
    }
    public Date getDate() {
        return _date;
    }
    public String getUserid() {
        return _Userid;
    }
    public void setUserid(String userid) {
        _Userid = userid;
    }



    public void setDeviceName(String deviceName) {
        _deviceName = deviceName;
    }

    public String getDeviceName() {
        return _deviceName;
    }

    public void setTrips(ArrayList<Trip> trips) {
        _trips = trips;
    }

    public ArrayList<Trip> getTrips() {
        return _trips;
    }

    public void setTripItems(ArrayList<TripItem> tripItems) {
        _tripItems = tripItems;
    }

    public ArrayList<TripItem> getTripItems() {
        return _tripItems;
    }
}