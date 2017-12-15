import java.util.ArrayDeque;
import java.util.List;
import java.util.Observable;

public class Generators extends Observable {

    private double chance;
    private ArrayDeque<String> waypoints;

    public Generators (double chance, ArrayDeque<String> waypoints){
        this.chance = chance;
        this.waypoints = waypoints;

        if (chance<0 || chance>1){
            System.out.println("CHANCE IS FALSE: "+chance);
        }
    }

    //todo Neu

    public ArrayDeque<String> getWaypoints(){
        return waypoints;
    }

    public double getChance(){
        return chance;
    }
    //todo Neu Ende

}
