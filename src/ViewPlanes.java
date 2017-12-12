public class ViewPlanes {

    private double planeX;
    private double planeY;
    Nodes currentNode;
    Nodes nextNode;

    public ViewPlanes (Nodes currentNode, Nodes nextNode){
        this.currentNode = currentNode;
        this.nextNode = nextNode;
        this.planeX = currentNode.getX();
        this.planeY = currentNode.getY();
    }

    public void moveBetweenNodes (double partTick){
        this.planeX += 1/
                Math.sqrt(Math.pow(nextNode.getX()-planeX,2)+Math.pow(nextNode.getY()-planeY,2))
                *(nextNode.getX()-planeX)*partTick;
        this.planeY += 1/
                Math.sqrt(Math.pow(nextNode.getX()-planeX,2)+Math.pow(nextNode.getY()-planeY,2))
                *(nextNode.getY()-planeY)*partTick;
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }

/*public class ViewPlanes extends Application {
    final static int CANVAS_WIDTH = 400;
    final static int CANVAS_HEIGHT = 400;

    Image bgImage;
    double bgX, bgY, bgW = 100.0, bgH = 100.0;

    @Override
    public void start(final Stage primaryStage) {

        final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        Group root = new Group();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(canvas);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 400, 425);
        primaryStage.setTitle(image on canvas);
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/





    /*private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();


        bgImage = new Image(getClass().getResourceAsStream(accept.png));
        bgX = canvasWidth2 - bgW2;
        bgY = canvasHeight2 - bgH2;
        gc.drawImage(bgImage, bgX, bgY, bgW, bgH);
    }


    public static void main(String[] args) {
        launch(args);
    }*/
}