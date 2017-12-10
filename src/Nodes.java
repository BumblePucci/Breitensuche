import java.util.*;

public class Nodes extends Observable {

    private double x;
    private double y;
    private String name;
    private String kind;
    private List<String> to;
    private List<String> conflicts;  //optional
    private double waittime;    //optional
    private String targettype;  //optional

    public Nodes(double x, double y, String name, String kind, List<String> to) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, List<String> conflicts) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.conflicts = conflicts;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, double waittime) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.waittime = waittime;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, String targettype) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.targettype = targettype;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, List<String> conflicts, double waittime) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.conflicts = conflicts;
        this.waittime = waittime;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, List<String> conflicts, String targettype) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.conflicts = conflicts;
        this.targettype = targettype;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, double waittime, String targettype) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.waittime = waittime;
        this.targettype = targettype;
    }

    public Nodes(double x, double y, String name, String kind, List<String> to, List<String> conflicts, double waittime, String targettype) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.kind = kind;
        this.to = to;
        this.conflicts = conflicts;
        this.waittime = waittime;
        this.targettype = targettype;
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

    public List<String> getConflicts() {
        return conflicts;
    }


    public double getWaittime() {
        return waittime;
    }


    public String getTargettype() {
        return targettype;
    }

}


