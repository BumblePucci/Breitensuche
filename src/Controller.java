import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private View view;
    private Model model;
    private double currentMouseX;
    private double currentMouseY;
    private double targetMouseX;
    private double targetMouseY;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;


        view.canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, ev -> {
            this.currentMouseX = ev.getX();
            this.currentMouseY = ev.getY();
        });

        view.canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, ev -> {
            this.targetMouseX = ev.getX();
            this.targetMouseY = ev.getY();
            view.shiftX = (int) (targetMouseX - currentMouseX);
            view.shiftY = (int) (targetMouseY - currentMouseY);

        });

        view.canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, ev -> {
            this.targetMouseX = ev.getX();
            this.targetMouseY = ev.getY();
            view.shiftX = (int) (targetMouseX - currentMouseX);
            view.shiftY = (int) (targetMouseY - currentMouseY);

        });

        view.canvas.addEventHandler(ScrollEvent.SCROLL_STARTED, ev -> {
            view.zoomX = (int) ev.getX();
            view.zoomY = (int) ev.getY();


        });

      view.canvas.addEventHandler(ScrollEvent.SCROLL, ev -> {
          double zoomUnit = 1.2;
          double deltaY = ev.getDeltaY();
            if (deltaY < 0) {
                zoomUnit = 2.0 - zoomUnit;
            }
            view.zoomFactor *= zoomUnit;
        });

        KeyFrame drawframe = new KeyFrame(Duration.seconds(model.bigTick), event -> {
            model.update();
            view.update();
            //view.updateCanvas();
        });
        Timeline t1 = new Timeline(drawframe);
        t1.setCycleCount(Timeline.INDEFINITE);
        t1.play();


        model.addObserver(this);
        view.getStage().show();
    }


    public void update(Observable o, Object arg) {
    }
}
