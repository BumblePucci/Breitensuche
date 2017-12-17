import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    private Model model;
    private Stage stage;
    Canvas canvas;
    Pane pane;
    double wNodes;
    double hNodes;
    int zoom;
    double zoomFactor;
    int zoomX;
    int zoomY;
    int shiftX;
    int shiftY;
    double wScene;
    double hScene;
    double wNode;
    double hNode;
    double wPlaneImage;
    double hPlaneImage;
    List<ViewPlanes> viewPlanesList;
    Image planeImage;
    Image nodeImage;

    public View (Model model, Stage stage){
        this.model = model;
        this.stage = stage;

        wNodes = Math.abs(model.getMinX()-model.getMaxX());
        hNodes = Math.abs(model.getMinY()-model.getMaxY());

        zoomFactor = 1;
        zoom = 300;
        zoomX = 0;
        zoomY = 0;
        shiftX = 0;
        shiftY = 0;

        wScene = 600;
        hScene = 600;
        wNode = 10;
        hNode = 10;
        wPlaneImage = 50;
        hPlaneImage = 50;

        viewPlanesList = new ArrayList<>();
        planeImage = new Image(getClass().getResourceAsStream("airplane.png"));
        nodeImage = new Image(getClass().getResourceAsStream("nodes.png"));

        canvas = new Canvas(wScene,hScene);
        pane = new Pane (canvas);
        pane.setStyle("-fx-background-color: #0176a0");
        Scene scene = new Scene(pane, wScene,hScene);
        stage.setTitle("Flughafen");
        stage.setScene(scene);
        stage.show();
        updateCanvas();

        KeyFrame drawframe = new KeyFrame(Duration.seconds(model.partTick), event->{
            updateCanvas();                             //Methode zur Anzeige der Nodes und Pfade
            for (ViewPlanes vP : viewPlanesList) {
                vUpdate(vP);                            //Methode zur Anzeige der Flugzeuge
                vP.moveBetweenNodes(model.partTick);    //Methode zur Bewegung der ViewPlanes
            }
        });
        Timeline t2 = new Timeline(drawframe);
        t2.setCycleCount(Timeline.INDEFINITE);
        t2.play();
    }

    //für jedes pExistList-Objekt der Klasse Planes wird ein Objekt der Klasse ViewPlanes der Liste viewPlanesList hinzugefügt
    public void update(){
        viewPlanesList.clear();
        for (Planes p : model.pExistList) {
            viewPlanesList.add(new ViewPlanes(p.getCurrentNode(), p.getNextNode()));
        }
    }

    //Die ViewPlanes werden gezeichnet
    public void vUpdate(ViewPlanes vP) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Affine affine = new Affine();
        affine.append(new Scale(zoomFactor, zoomFactor, zoomX, zoomY));
        affine.append(new Translate(shiftX, shiftY));

        if (vP.nextNode.getX() - vP.presentNode.getX() > 0) {
            affine.append(new Rotate(Math.toDegrees(Math.atan((vP.nextNode.getY() - vP.presentNode.getY()) / (vP.nextNode.getX() - vP.presentNode.getX()))) +90,
                    (vP.getPlaneX() * zoom / wNodes + wScene / 2 - wNodes / 2) + wNode / 2, (vP.getPlaneY() * zoom / wNodes + hScene / 2 - hNodes / 2) + hNode / 2));
        } else {
            affine.append(new Rotate(Math.toDegrees(Math.atan((vP.nextNode.getY() - vP.presentNode.getY()) / (vP.nextNode.getX() - vP.presentNode.getX())))-90,
                    (vP.getPlaneX() * zoom / wNodes + wScene / 2 - wNodes / 2) + wNode / 2, (vP.getPlaneY() * zoom / wNodes + hScene / 2 - hNodes / 2) + hNode / 2));
        }
        gc.setTransform(affine);
        gc.drawImage(planeImage, (vP.getPlaneX() * zoom / wNodes + wScene / 2 - wNodes / 2) + wNode / 2 - wPlaneImage / 2, (vP.getPlaneY() * zoom / wNodes + hScene / 2 - hNodes / 2) + hNode / 2 - hPlaneImage / 2, wPlaneImage, hPlaneImage);

    }

    //Nodes und Pfade werden gezeichnet
    public void updateCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Affine affine = new Affine();
        gc.setTransform(affine);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        affine = new Affine();
        affine.append(new Scale(zoomFactor,zoomFactor,zoomX,zoomY));
        affine.append(new Translate(shiftX, shiftY));
        gc.setTransform(affine);
        for (Nodes n : model.nMap.values()){
            gc.drawImage(nodeImage, n.getX()* zoom / wNodes +wScene/2- wNodes /2,n.getY()* zoom / wNodes +hScene/2- hNodes /2,wNode,hNode);
        }

        gc.setStroke(Color.DARKGRAY);
        for (String name : model.nMap.keySet()){
            Nodes nodes = model.nMap.get(name);
            for (int i=0; i<nodes.getTo().size(); i++) {
                String keyName = nodes.getTo().get(i);
                Nodes nachbarn = model.nachbarnNMap.get(keyName);
                gc.strokeLine(nodes.getX()* zoom / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nodes.getY()* zoom / wNodes +(hScene/2+hNode/2)- hNodes /2,
                        nachbarn.getX()* zoom / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nachbarn.getY()* zoom / wNodes +(hScene/2+hNode/2)- hNodes /2);
            }
        }
    }


    public void update(Observable o, Object arg) {
    }

    public Stage getStage() {
        return stage;
    }

}
