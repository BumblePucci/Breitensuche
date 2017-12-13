import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
    int zoomFactor;
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
    ViewPlanes viewPlanes;
    Image planeImage;

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

        /*TODO: für jedes Planes ein ViewPlanes zeichnen
        List<ViewPlanes> viewPlanes = new ArrayList<>();
        for (Planes p : model.pList){
            for (Nodes node1 : model.breadthSearch(p)) {
                for (Nodes node2 : model.breadthSearch(p)) {
                    viewPlanes.add(new ViewPlanes(node1, node2));
                }
            }
        }*/

        viewPlanes = new ViewPlanes(model.nMap.get("air10"),model.nMap.get("air11"));
        planeImage = new Image(getClass().getResourceAsStream("airplane.png"));

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
            updateCanvas();
            updatePlane();
        });
        Timeline t2 = new Timeline(drawframe);
        t2.setCycleCount(Timeline.INDEFINITE);
        t2.play();

    }

    public void updatePlane(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Affine affine = new Affine();
        affine.append(new Scale(zoomFactor,zoomFactor,zoomX,zoomY));
        affine.append(new Translate(shiftX, shiftY));
        if (viewPlanes.nextNode.getX() - viewPlanes.presentNode.getX() > 0) {
            affine.append(new Rotate(Math.toDegrees(Math.atan((viewPlanes.nextNode.getY() - viewPlanes.presentNode.getY()) / (viewPlanes.nextNode.getX() - viewPlanes.presentNode.getX()))) - 180,
                    (viewPlanes.getPlaneX() * zoom / wNodes + wScene / 2 - wNodes / 2) + wNode / 2, (viewPlanes.getPlaneY() * zoom / wNodes + hScene / 2 - hNodes / 2) + hNode / 2));
        }
        else{
            affine.append(new Rotate(Math.toDegrees(Math.atan((viewPlanes.nextNode.getY() - viewPlanes.presentNode.getY()) / (viewPlanes.nextNode.getX() - viewPlanes.presentNode.getX()))),
                    (viewPlanes.getPlaneX() * zoom / wNodes + wScene / 2 - wNodes / 2) + wNode / 2, (viewPlanes.getPlaneY() * zoom / wNodes + hScene / 2 - hNodes / 2) + hNode / 2));
        }
        gc.setTransform(affine);
        //gc.clearRect(viewPlanes.getPlaneX()* zoom / wNodes +wScene/2- wNodes /2,viewPlanes.getPlaneY()* zoom / wNodes +hScene/2- hNodes /2,10,10);
        gc.drawImage(planeImage,(viewPlanes.getPlaneX()* zoom / wNodes +wScene/2- wNodes /2)+wNode/2-wPlaneImage/2,(viewPlanes.getPlaneY()* zoom / wNodes +hScene/2- hNodes /2)+hNode/2-hPlaneImage/2,wPlaneImage,hPlaneImage);
    }

    public void updateCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Affine affine = new Affine();
        affine.append(new Scale(zoomFactor,zoomFactor,zoomX,zoomY));
        affine.append(new Translate(shiftX, shiftY));
        gc.setTransform(affine);
        gc.setFill(Color.RED);
        //Erstmal Node-Dummie
        for (Nodes n : model.nMap.values()){
            gc.fillOval(n.getX()* zoom / wNodes +wScene/2- wNodes /2,n.getY()* zoom / wNodes +hScene/2- hNodes /2,wNode,hNode);
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
                gc.strokeLine(nodes.getX()* zoom / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nodes.getY()* zoom / wNodes +(hScene/2+hNode/2)- hNodes /2,
                        nachbarn.getX()* zoom / wNodes +(wScene/2+wNode/2)- wNodes /2,
                        nachbarn.getY()* zoom / wNodes +(hScene/2+hNode/2)- hNodes /2);
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
