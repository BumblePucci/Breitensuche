import java.util.List;
import java.util.Observable;

public class Nodes extends Observable {

    private double x;
    private double y;
    private String name;
    private String kind;
    private List<String> to;
    private List<String> conflicts;  //optional
    private double waittime;    //optional
    private String targettype;  //optional
    private boolean visited;

    public Nodes(double x, double y, String name, String kind, List<String> to, List<String> conflicts, double waittime, String targettype) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.conflicts = conflicts;
        this.waittime = waittime;
        this.targettype = targettype;
        this.visited = false;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public List<String> getTo() {
        return to;
    }

    public String getTargettype() {
        return targettype;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}


