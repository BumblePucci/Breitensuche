import java.util.List;
import java.util.Observable;

public class Generators extends Observable {

    private double chance;
    private String[] waypoints;

    public Generators (double chance, String[] waypoints){
        this.chance = chance;
        this.waypoints = waypoints;

        if (chance<0 || chance>1){
            System.out.println("CHANCE IS FALSE: "+chance);
        }
    }

}
