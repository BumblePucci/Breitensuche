import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private Model model;
    private Stage stage;
    Canvas canvas;
    Pane pane;

    public View (Model model, Stage stage){
        this.model = model;
        this.stage = stage;

        canvas = new Canvas(600,600);
        pane = new Pane (canvas);

        Scene scene = new Scene(pane, 600,600);
        stage.setTitle("Flughafen");
        stage.setScene(scene);
        stage.show();
        updateCanvas();

        KeyFrame drawframe = new KeyFrame(Duration.seconds(0.001), event->{
            //das Flugzeug bewegt sich innerhalb zweier Nodes
        });
        Timeline t2 = new Timeline(drawframe);
        t2.setCycleCount(Timeline.INDEFINITE);
        t2.play();

    }

    public void updateCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        //Male für jedes Flugzeug ein "Flugzeug-Dummie"
        for (Planes p : model.pList) {
            gc.fillOval(p.getPx(), p.getPy(), 30, 30);
            //gc.fillArc(0,0, 200,200,-5, 10, ArcType.ROUND);
        }
        gc.setFill(Color.RED);
        //Erstmal Node-Dummie
        for (Nodes n : model.nMap.values()){
            gc.fillOval(30*n.getX()+300, 30*n.getY()+300, 10, 10);  //erst auskommentieren, wenn x, y Getter haben
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
                Nodes nachbarn = model.nnMap.get(keyName);
                gc.strokeLine(30*nodes.getX()+305, 30*nodes.getY()+305, 30*nachbarn.getX()+305,30*nachbarn.getY()+305);
            }
        }
    }



    public void update(Observable o, Object arg){
    }
    public Stage getStage() { return stage;}

}
