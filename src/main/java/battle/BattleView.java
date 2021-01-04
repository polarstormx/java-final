package battle;

import java.io.File;
import java.io.IOException;

import battle.conf.Configs;
import battle.view.GameEndView;
import battle.view.InfoColumn;
import battle.view.SelectionBar;
import battle.view.StepHint;
import battle.view.ViewTools;
import battle.view.SelectModeWindow;
import ground.IntPt;
import ground.Tile;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BattleView extends Application {

    Stage stage = new Stage();
    Pane pane = new Pane();
    public Canvas canvas;

    // 技能选择栏
    SelectionBar seclectionBar;
    // 人物信息栏
    InfoColumn infoColumn;
    // 部署提示栏
    StepHint hint;

    // 选择位置后的高亮
    Ellipse gridHighlight;


    // 保存和关闭的图标
    Label save;
    Label close;
    // 结束游戏的页面
    GameEndView endMask;

    // 拖动的处理
    double xOffset, yOffset;


    String loadfile;

    BattleController battleController;


    boolean isDarkSide = false;
    boolean isAutoMode = false;
    boolean isDoublePlayer = false;


    public void switchShow(String loadfile, SelectModeWindow.PLAY_MODE playtype) throws Exception {
        this.loadfile = loadfile;
        if (loadfile == null) {
            switch (playtype) {
                case PL_JUST:
                    isDarkSide = false;
                    break;
                case PL_DARK:
                    isDarkSide = true;
                    break;
                case PL_AUTO:
                    isAutoMode = true;
                    break;
                case PL_DOUBLE:
                    isDoublePlayer = true;
                    break;
            }
        }
        loadComponents();
        start(stage);
    }

    private void loadBkGround() {
        // 配置背景
        ImageView bkGround = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_BK_GROUND.ordinal()));
        ViewTools.setImgFit(bkGround, Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
        ViewTools.addPaneChildren(pane, bkGround);
    }

    private void loadBattleGround() {
        // 配置战斗网格
        ImageView battlefield = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_BATTLE_GRID.ordinal()));
        ViewTools.setImgFit(battlefield, Configs.TILE_LEN * Configs.TILE_W_NUMS, Configs.TILE_LEN * Configs.TILE_H_NUMS);

        ViewTools.setImgXY(battlefield, Configs.MARGIN_LEFT, Configs.MARGIN_TOP);

        ViewTools.addPaneChildren(pane, battlefield);
    }

    private void loadSelectionBar() {

        seclectionBar = new SelectionBar();

        seclectionBar.setLayoutX(0);
        seclectionBar.setLayoutY((Configs.WINDOWS_HGT - Configs.SELECTION_BAR_HGT) / 2.0);
    }

    private void loadInfoColumn() {
        // 配置信息栏
        infoColumn = new InfoColumn();

        ViewTools.setPaneLayout(infoColumn, -10,
                Configs.MARGIN_LEFT + Configs.TILE_LEN * 5 - Configs.INFO_BAR_WID / 2.0);
    }

    private void loadHighLightGrid() {
        // 对选择位置的高亮显示
        gridHighlight = new Ellipse(Configs.TILE_LEN, Configs.TILE_LEN / 2);
        gridHighlight.setFill(new Color(0.5, 0.7, 0.7, 0.5));
        pane.getChildren().add(gridHighlight);
    }

    private void loadSaveImg() {
        // 保存图标加载
        ImageView saveimg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_SAVE.ordinal()));
        ViewTools.setImgFit(saveimg, Configs.TILE_LEN / 2.0, Configs.TILE_LEN / 2.0);
        save = new Label("", saveimg);
        save.setLayoutX(Configs.WINDOWS_WID - Configs.TILE_LEN * 1.5);
        save.setOnMouseClicked((MouseEvent) -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showSaveDialog(stage);
            if (file != null)
                battleController.recSave.saveToFile(file.getPath() + ".xml");
        });
        ViewTools.addPaneChildren(pane, save);
    }

    private void loadExitImg() {
        ImageView closeimg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_EXIT.ordinal()));
        ViewTools.setImgFit(closeimg, Configs.TILE_LEN / 2.0, Configs.TILE_LEN / 2.0);

        close = new Label("", closeimg);
        close.setLayoutX(Configs.WINDOWS_WID - Configs.TILE_LEN / 2.0);
        close.setOnMouseClicked((MouseEvent e) -> {
            System.exit(0);
        });

        ViewTools.setNodeHighLight(close, closeimg, Configs.IMG_INDEX.IMG_EXIT, Configs.IMG_INDEX.IMG_EXIT_HL);
        ViewTools.addPaneChildren(pane, close);

        endMask = new GameEndView();
        endMask.setVisible(false);
    }


    private void loadStepHint() {
        hint = new StepHint();
        hint.setLayoutX(Configs.MARGIN_LEFT + Configs.TILE_LEN * 4);
        hint.setLayoutY(Configs.MARGIN_TOP / 2);
        if (isDarkSide)
            hint.setVal(2, isDarkSide);
        ViewTools.addPaneChildren(pane, hint);
    }


    private void loadCanvas() throws IOException {
        canvas = new Canvas(Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
        battleController = new BattleController(this, loadfile, isDarkSide, isAutoMode, isDoublePlayer);
        seclectionBar.battleController = battleController;

        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        ViewTools.addPaneChildren(pane, canvas);
    }


    private void loadComponents() throws IOException, InterruptedException {

        loadBkGround();
        loadBattleGround();
        loadSelectionBar();
        loadInfoColumn();
        loadHighLightGrid();

        loadCanvas();

        loadStepHint();

        loadSaveImg();
        loadExitImg();

    }


    // 对方块的高亮进行处理
    private void genGridHighLight() {
        pane.setOnMouseMoved((MouseEvent e) -> {

            for (int j = 0; j < Configs.TILE_H_NUMS; j++)
                for (int i = 0; i < Configs.TILE_W_NUMS; i++) {
                    Tile block = Configs.isInTile(e.getX(), e.getY(), i, j);
                    if (block != null) {
                        gridHighlight.setRadiusX(block.topWid / 8 + block.bottomWid * 3 / 8);
                        gridHighlight.setRadiusY(block.tileHgt / 4);
                        gridHighlight.setCenterX(block.topX * 3 / 4 + block.bottomX / 4 + block.topWid / 8 + block.bottomWid * 3 / 8);
                        gridHighlight.setCenterY(block.y + block.tileHgt * 3 / 4);
                        infoColumn.reSet(battleController.newAction(new IntPt(i, j), BattleController.ACTION_TYPE.ACTION_MOVEABOVE));
                        return;
                    }
                }
        });

    }

    private void handleClickCmd() {
        pane.setOnMouseClicked((MouseEvent e) -> {
            for (int j = 0; j < Configs.TILE_H_NUMS; j++)
                for (int i = 0; i < Configs.TILE_W_NUMS; i++) {
                    Tile block = Configs.isInTile(e.getX(), e.getY(), i, j);
                    if (block != null) {
                        seclectionBar.updateInfo(battleController.newAction(new IntPt(i, j), BattleController.ACTION_TYPE.ACTION_CLICKED));
                        return;
                    }
                }
        });
    }

    @Override
    public void start(Stage primaryStage) {

        genGridHighLight();
        handleClickCmd();

        ViewTools.mousePressAndDrag(primaryStage, pane);


        pane.setBackground(null);
        ViewTools.showScene(primaryStage, pane);

        ViewTools.addPaneChildren(pane, seclectionBar);
        ViewTools.addPaneChildren(pane, infoColumn);
        ViewTools.addPaneChildren(pane, endMask);
    }
}



