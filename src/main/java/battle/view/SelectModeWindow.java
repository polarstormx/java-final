package battle.view;

import battle.BattleView;
import battle.conf.Configs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SelectModeWindow extends Application {
    Stage stage = new Stage();
    Pane pane = new Pane();
    Text netTxt = new Text("无提示信息");

    public enum PLAY_MODE {
        PL_JUST, PL_DARK, PL_AUTO, PL_DOUBLE
    }

    ;

    public void switchShow() {
        loadComponents();
        start(stage);
    }

    private void switchWindow(PLAY_MODE type) {
        BattleView nextWin = new BattleView();
        try {
            nextWin.switchShow(null, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (stage != null)
            stage.close();
    }

    private void loadBackGround() {
        stage.setWidth(1600);
        stage.setHeight(800);
        ImageView bg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_SELECT_BK_GROUND.ordinal()));
        ViewTools.setImgFit(bg, 1000, 500);
        ViewTools.setImgLayout(bg, 300, 150);
        ViewTools.addPaneChildren(pane, bg);
    }

    private void loadJusticeMode() {
        ImageView imgJust = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_JUST_CAMP.ordinal()));
        ViewTools.setImgFit(imgJust, 400, 465);

        Label justice = new Label("", imgJust);
        ViewTools.setLabelPara(justice, 500, 500, 200, 200);

        justice.setOnMouseClicked((MouseEvent e) -> {
            switchWindow(PLAY_MODE.PL_JUST);
        });

        ViewTools.setNodeHighLight(justice, imgJust, Configs.IMG_INDEX.IMG_JUST_CAMP, Configs.IMG_INDEX.IMG_JUST_CAMP_H);
        ViewTools.addPaneChildren(pane, justice);
    }

    private void loadDarkMode() {
        ImageView imgDark = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_DARK_CAMP.ordinal()));
        ViewTools.setImgFit(imgDark, 550, 375);

        Label dark = new Label("", imgDark);
        ViewTools.setLabelPara(dark, 500, 500, 1000, 210);

        dark.setOnMouseClicked((MouseEvent e) -> {
            switchWindow(PLAY_MODE.PL_DARK);
        });

        ViewTools.setNodeHighLight(dark, imgDark, Configs.IMG_INDEX.IMG_DARK_CAMP, Configs.IMG_INDEX.IMG_DARK_CAMP_H);
        ViewTools.addPaneChildren(pane, dark);
    }

    private void loadHelpText() {
        Text helpTxt = new Text("请您先在一台机器上点击正方\n出现等待连接字样后\n再在另一台机器上点击反方\n游戏会自动开始");
        helpTxt.setFont(Font.font("黑体", FontWeight.BLACK, 24));
        helpTxt.setLayoutX(675);
        helpTxt.setLayoutY(350);

        helpTxt.setFill(Color.WHITE);
        ViewTools.addPaneChildren(pane, helpTxt);


        netTxt.setFont(Font.font("黑体", FontWeight.BLACK, 24));
        netTxt.setLayoutX(675);
        netTxt.setLayoutY(500);
        netTxt.setFill(Color.WHITE);
        ViewTools.addPaneChildren(pane, netTxt);

    }

    private void loadComponents() {
        loadBackGround();

        loadJusticeMode();

        loadDarkMode();

        loadHelpText();
    }


    private void stageShow(Stage priStage) {
        Scene scene = new Scene(pane, Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
        scene.setFill(null);
        priStage.setScene(scene);
        priStage.initStyle(StageStyle.TRANSPARENT);
        priStage.show();
    }

    @Override
    public void start(Stage priStage) {
        ViewTools.mousePressAndDrag(priStage, pane);

        stageShow(priStage);

    }
}
