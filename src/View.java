import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private Model model;
    private Stage stage;
    Canvas canvas;
    Pane pane;
    double wNodes;
    double hNodes;
    int zoomFactor;
    double wScene;
    double hScene;
    double wNode;
    double hNode;
    ViewPlanes viewPlanes;

    public View (Model model, Stage stage){
        this.model = model;
        this.stage = stage;

        wNodes = Math.abs(model.getMinX()-model.getMaxX());
        hNodes = Math.abs(model.getMinY()-model.getMaxY());
        zoomFactor = 300;
        wScene = 600;
        hScene = 600;
        wNode = 10;
        hNode = 10;
        viewPlanes = new ViewPlanes(model.nMap.get("air3"),model.nMap.get("air4"));
        canvas = new Canvas(wScene,hScene);

        pane = new Pane (canvas);

        Scene scene = new Scene(pane, wScene,hScene);
        stage.setTitle("Flughafen");
        stage.setScene(scene);
        stage.show();
        updateCanvas();
        //Lisas Teil
        /*model.pList.get(0).setCurrentNode(model.nMap.get(model.pList.get(0).getWaypoints().getFirst()));
        model.pList.get(0).getWaypoints().removeFirst();
        model.breadthSearch(model.pList.get(0));
        */

        KeyFrame drawframe = new KeyFrame(Duration.seconds(model.partTick), event->{
            System.out.print("-");
            viewPlanes.moveBetweenNodes(model.partTick);
            updatePlane();
        });
        Timeline t2 = new Timeline(drawframe);
        t2.setCycleCount(Timeline.INDEFINITE);
        t2.play();

    }

    public void updatePlane(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(viewPlanes.getPlaneX()* zoomFactor / wNodes +wScene/2- wNodes /2,viewPlanes.getPlaneY()* zoomFactor / wNodes +hScene/2- hNodes /2,10,10);
        gc.setFill(Color.BLUE);
        gc.fillOval(viewPlanes.getPlaneX()* zoomFactor / wNodes +wScene/2- wNodes /2,viewPlanes.getPlaneY()* zoomFactor / wNodes +hScene/2- hNodes /2,10,10);
    }

    public void updateCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        //Erstmal Node-Dummie
        for (Nodes n : model.nMap.values()){
            gc.fillOval(n.getX()* zoomFactor / wNodes +wScene/2- wNodes /2,n.getY()* zoomFactor / wNodes +hScene/2- hNodes /2,wNode,hNode);
            //gc.fillOval(30*n.getX()+300, 30*n.getY()+300, 10, 10);  //erst auskommentieren, wenn x, y Getter haben
            //relative Anzeige: Hilfe möglciher Weise im Aufgabenblatt: erster Hinweispunkt unter c)
        }

        gc.setStroke(Color.DARKGRAY);
        //Pfade
        //for (Nodes n : model.nMap.values()){
        //    gc.strokeLine(n.getX(), n.getY(), n.getTo());
        //}
        for (String name : model.nMap.keySet()){
            Nodes nodes = model.nMap.get(name);
            for (int i=0; i<nodes.getTo().size(); i++) {
                String keyName = nodes.getTo().get(i);
                Nodes nachbarn = model.nachbarnNMap.get(keyName);
                gc.strokeLine(nodes.getX()* zoomFactor / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nodes.getY()* zoomFactor / wNodes +(hScene/2+hNode/2)- hNodes /2,
                        nachbarn.getX()* zoomFactor / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nachbarn.getY()* zoomFactor / wNodes +(hScene/2+hNode/2)- hNodes /2);
                //gc.strokeLine(30*nodes.getX()+305, 30*nodes.getY()+305, 30*nachbarn.getX()+305,30*nachbarn.getY()+305);
            }
        }
    }


    public void update(Observable o, Object arg) {
    }

    public Stage getStage() {
        return stage;
    }

}
