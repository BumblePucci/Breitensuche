import java.util.ArrayDeque;
import java.util.Observable;

public class Generators extends Observable {

    private double chance;
    private ArrayDeque<String> waypoints;

    public Generators (double chance, ArrayDeque<String> waypoints){
        this.chance = chance; // Chance kann erhöht werden damit Generator eher auslöst;
        this.waypoints = waypoints;

        if (chance<0 || chance>1){
            System.out.println("CHANCE IS FALSE: "+chance);
        }
    }


    public ArrayDeque<String> getWaypoints(){
        return waypoints;
    }

    public double getChance(){return chance;}

}
