import javafx.application.Application;
import javafx.stage.Stage;
import org.JSONObject;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        View view = new View(model, stage);
        new Controller(model, view);
    }

    public static void main(String[] args) throws IOException {
        Model model = new Model();
        JSONObject jsonObject1 = model.load_json_file("C:/Users/Lisa/IdeaProjects/Chaos-Flughafen-2/src/small.json");
        model.read_maxplanes(jsonObject1);

        JSONObject jsonObject2 = model.load_json_file("C:/Users/Lisa/IdeaProjects/Chaos-Flughafen-2/src/small.json");
        model.read_panes(jsonObject2);

        JSONObject jsonObject3 = model.load_json_file("C:/Users/Lisa/IdeaProjects/Chaos-Flughafen-2/src/small.json");
        model.read_nodes(jsonObject3);

        JSONObject jsonObject4 = model.load_json_file("C:/Users/Lisa/IdeaProjects/Chaos-Flughafen-2/src/small.json");
        model.read_generators(jsonObject4);

        launch(args);
    }

}
