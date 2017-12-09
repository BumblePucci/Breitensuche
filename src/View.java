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

    }

    public void updateCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        //Male für jedes Flugzeug ein "Flugzeug-Dummie"
        for (Planes p : model.pList) {
            gc.fillOval(0, 0, 30, 30);
            //gc.fillArc(0,0, 200,200,-5, 10, ArcType.ROUND);
        }
        gc.setFill(Color.RED);
        //Erstmal Node-Dummie
        for (Nodes nodes : model.nMap.values()){
            gc.fillOval(50*nodes.getX(), 50*nodes.getY(), 10, 10);  //erst auskommentieren, wenn x, y Getter haben
        }
    }

    public void update(Observable o, Object arg){
    }
    public Stage getStage() { return stage;}

}
