

import battle.conf.Configs;
import battle.view.StartColumn;
import battle.view.ViewBundle;
import ground.GroundView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    Stage stage = null;

    StartColumn stColumn;

    @Override
    public void init() throws Exception {
        super.init();
        GroundView.init();
        Configs.loadAll();
        ViewBundle.init();
        HelpDocGenerator.generateHelpDoc();
        stColumn = new StartColumn(stage);
    }

    private void setScene(Stage primaryStage) {
        Scene scene = new Scene(stColumn.pane, 600, 600);
        scene.setFill(null);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        stColumn.stage = primaryStage;

        stColumn.handleMouseDragEvent(primaryStage);

        stColumn.pane.setBackground(null);

        setScene(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
