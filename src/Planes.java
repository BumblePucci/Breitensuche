import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Planes extends Observable {

    private ArrayDeque<String> waypoints;
    private int inittime;
    private double px, py;
    private List<Nodes> nodesList;
    private Nodes currentNode;
    private Nodes nextNode;


    public Planes(ArrayDeque<String> waypoints, int inittime) {
        this.waypoints = waypoints;
        this.inittime = inittime;
        this.nodesList = new ArrayList<>();
        this.nextNode = null;
    }

    public ArrayDeque<String> getWaypoints() {
        return waypoints;
    }

    public int getInittime() {
        return inittime;
    }

    public List<Nodes> getNodesList() {
        return nodesList;
    }

    public void setNodesList(List<Nodes> nodesList) {
        this.nodesList = nodesList;
    }

    public Nodes getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Nodes currentNode) {
        this.currentNode = currentNode;
    }

    public Nodes getNextNode() {
        return nextNode;
    }

    public void setNextNode(Nodes nextNode) {
        this.nextNode = nextNode;
    }


}
