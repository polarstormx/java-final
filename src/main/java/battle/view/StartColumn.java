package battle.view;

import java.io.File;

import battle.BattleView;
import battle.conf.Configs;
import battle.view.SelectModeWindow.PLAY_MODE;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StartColumn {
    public Stage stage = null;
    public Pane pane = new Pane();

    public StartColumn(Stage s) {
        // TODO Auto-generated constructor stub
        stage = s;
        loadComponents();
    }

    private void loadBkGround() {
        ImageView bkGround = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_PRE_BK_GROUND.ordinal()));
        ViewTools.setImgFit(bkGround, 300, 500);
        ViewTools.addPaneChildren(pane, bkGround);
    }

    private void loadStartImg() {
        ImageView startImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_START.ordinal()));
        ViewTools.setImgFit(startImg, 280, 100);

        Label startLabel = new Label("", startImg);
        ViewTools.setLabelPara(startLabel, 200, 100, 260, 300);
        ViewTools.setNodeHighLight(startLabel, startImg, Configs.IMG_INDEX.IMG_START, Configs.IMG_INDEX.IMG_START_H);
        startLabel.setOnMouseClicked((MouseEvent e) -> {
            SwitchWindows(null);
        });
        ViewTools.addPaneChildren(pane, startLabel);
    }

    private void loadLoadFileImg() {
        ImageView loadImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_LOAD.ordinal()));
        ViewTools.setImgFit(loadImg, 280, 100);
        Label loadFileLabel = new Label("", loadImg);
        ViewTools.setLabelPara(loadFileLabel, 200, 100, 260, 400);

        ViewTools.setNodeHighLight(loadFileLabel, loadImg, Configs.IMG_INDEX.IMG_LOAD, Configs.IMG_INDEX.IMG_LOAD_H);
        loadFileLabel.setOnMouseClicked((MouseEvent) -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Xml Files", "*.xml"));
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                SwitchWindows(file.getPath());
            }
        });
        ViewTools.addPaneChildren(pane, loadFileLabel);
    }

    private void loadExitImg() {
        ImageView exitImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_EXIT.ordinal()));
        ViewTools.setImgFit(exitImg, 50, 50);
        Label exitLabel = new Label("", exitImg);
        ViewTools.setLabelPara(exitLabel, 50, 50, 550, 50);
        exitLabel.setOnMouseClicked((MouseEvent e) -> {
            Platform.exit();
        });
        ViewTools.setNodeHighLight(exitLabel, exitImg, Configs.IMG_INDEX.IMG_EXIT, Configs.IMG_INDEX.IMG_EXIT_HL);

        ViewTools.addPaneChildren(pane, exitLabel);
    }

    public void loadComponents() {
        loadBkGround();

        loadStartImg();

        loadLoadFileImg();

        loadExitImg();

    }

    private void SwitchWindows(String loadfile) {
        if (loadfile == null) {
            SelectModeWindow nextWin = new SelectModeWindow();
            try {
                nextWin.switchShow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stage != null)
                stage.close();
        } else {
            BattleView nextWin = new BattleView();
            try {
                nextWin.switchShow(loadfile, PLAY_MODE.PL_JUST);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stage != null)
                stage.close();
        }
    }

    public void handleMouseDragEvent(Stage primaryStage) {
        ViewTools.mousePressAndDrag(primaryStage, pane);
    }

}
