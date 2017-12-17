import static java.lang.Math.min;

public class ViewPlanes {

    private double planeX;
    private double planeY;
    Nodes presentNode;
    Nodes nextNode;
    int tickCount = 0; // zählt in Schritten von 0-1000

    public ViewPlanes (Nodes presentNode, Nodes nextNode){
        this.presentNode = presentNode;
        this.nextNode = nextNode;
        this.planeX = presentNode.getX();
        this.planeY = presentNode.getY();
    }

    public void moveBetweenNodes (double partTick){
        tickCount = min(tickCount+1,(int) (1./partTick)); // maximal = 30 ticks
        this.planeX = presentNode.getX() + (nextNode.getX()- presentNode.getX()) * (partTick*tickCount);
        this.planeY = presentNode.getY() + (nextNode.getY()- presentNode.getY()) * (partTick*tickCount);
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }

}