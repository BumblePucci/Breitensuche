import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Planes extends Observable {

    private String[] waypoints;
    private int inittime;

    public Planes (String[] waypoints, int inittime){
        this.waypoints = waypoints;
        this.inittime = inittime;
    }



    /*
    //ANMERKUNG: Man sollte aus anderen Methoden mit planes.methodenname darauf zugreifen können.
    //ANMERKUNG: Bin mir bei den getPlanes und getWaypoints mit dem Typ der Methode nicht sicher

    //Getter for plane at position i
    public String getPlanes (int i){
        this.read_json(jo);
        JSONObject plane = planes.getJSONObject(i);
        return plane;
    }

    //Getter für waypoints at plane position i
    public String getWaypoints(int i){
        JSONArray waypoints = getPlanes(i).getJSONArray("waypoints");
        return waypoints;
    }

    //Getter for initTime at plane position i
    public int getInit(int i){
        int initTime = getPlanes(i).getInt("inittime");
        return initTime;
    }
    */



}
