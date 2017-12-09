import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Planes extends Observable {

    private String[] waypoints;
    private int inittime;
    private double px, py;
    private List<Nodes> nodesList;

    public Planes (String[] waypoints, int inittime){
        this.waypoints = waypoints;
        this.inittime = inittime;
    }

    public String[] getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(String[] waypoints) {
        this.waypoints = waypoints;
    }

    public int getInittime() {
        return inittime;
    }

    public void setInittime(int inittime) {
        this.inittime = inittime;
    }

    public double getPx() {
        return px;
    }

    public void setPx(double px) {
        this.px = px;
    }

    public double getPy() {
        return py;
    }

    public void setPy(double py) {
        this.py = py;
    }

    public List<Nodes> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<Nodes> nodesList) {
        this.nodesList = nodesList;
    }
}
