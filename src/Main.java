import javafx.application.Application;
import javafx.stage.Stage;
import org.JSONObject;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        String path="./src/small.json";
        JSONObject jsonObject1 = model.load_json_file(path);
        View view = new View(model, stage);
        new Controller(model, view);
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
