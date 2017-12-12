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

    public Controller (Model model, View view){
        this.model = model;
        this.view = view;

        KeyFrame drawframe = new KeyFrame(Duration.seconds(model.bigTick), event->{
            model.update();
            view.updateCanvas();
        });
        Timeline t1 = new Timeline(drawframe);
        t1.setCycleCount(Timeline.INDEFINITE);
        t1.play();


        model.addObserver(this);
        view.getStage().show();
    }


    public void update (Observable o, Object arg){}
}
