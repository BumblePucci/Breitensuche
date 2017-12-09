import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Nodes extends Observable {

    private double x;
    private double y;
    private String name;
    private String kind;
    private List<String> to;
    //private List<Nodes.name> conflicts = List<name>;  //optional
    //private double waittime;    //optional
    //private String targettype;  //optional

    //public Nodes (double x, double y, String name, String kind, List to, List, conflicts, double waittime, String targettype)
    public Nodes (double x, double y, String name, String kind, List<String> to){
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
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
}


