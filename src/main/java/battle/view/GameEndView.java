package battle.view;

import battle.conf.Configs;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// 游戏结束时显示的界面 
// 可能有移动下方界面的错误可能
public class GameEndView extends Pane {

    Label exitLabel;

    boolean theWinSide;

    private void loadBkGround() {
        Rectangle bkGround = new Rectangle(Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
        bkGround.setFill(Color.color(1.0, 1.0, 1.0, 0.8));
        bkGround.setStroke(null);
        ViewTools.addPaneChildren(this, bkGround);
    }

    private void loadGameOverImg() {
        ImageView gameOverImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_GAMEOVER.ordinal()));
        ViewTools.setImgFit(gameOverImg, Configs.WINDOWS_HGT / 1.5, Configs.WINDOWS_HGT / 3.0);
        ViewTools.setImgXY(gameOverImg,
                Configs.WINDOWS_WID / 2.0 - Configs.WINDOWS_HGT / 3.0,
                Configs.WINDOWS_HGT / 5.0);
        ViewTools.addPaneChildren(this, gameOverImg);
    }

    private void loadWinSideImg() {
        ImageView winSideImg;

        if (theWinSide) {
            winSideImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_JUST_CAMP.ordinal()));
        } else {
            winSideImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_DARK_CAMP.ordinal()));
        }
        ViewTools.setImgFit(winSideImg, Configs.WINDOWS_HGT / 3.5, Configs.WINDOWS_HGT / 5.0);
        ViewTools.setImgXY(winSideImg,
                Configs.WINDOWS_WID / 2.0 - Configs.WINDOWS_HGT / 10.0,
                Configs.WINDOWS_HGT * 3 / 5.0);
        ViewTools.addPaneChildren(this, winSideImg);
    }


    private void loadExitImg() {
        ImageView exitImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_EXIT.ordinal()));
        ViewTools.setImgFit(exitImg, Configs.TILE_LEN / 2.0, Configs.TILE_LEN / 2.0);
        exitLabel = new Label("", exitImg);
        exitLabel.setLayoutX(Configs.WINDOWS_WID - Configs.TILE_LEN / 2.0);

        exitLabel.setOnMouseEntered((MouseEvent e) -> {
            exitImg.setImage(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_EXIT_HL.ordinal()));
        });
        exitLabel.setOnMouseExited((MouseEvent e) -> {
            exitImg.setImage(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_EXIT.ordinal()));
        });
        exitLabel.setOnMouseClicked((MouseEvent e) -> {
            System.exit(0);
        });

        getChildren().add(exitLabel);
    }

    private void loadEndText() {
        Text endText;
        if (theWinSide) {
            endText = new Text("正义方 胜利了 ！！！");
        } else {
            endText = new Text("黑暗方 胜利了 ！！！");
        }

        endText.setFont(new Font(32));
        endText.setLayoutX(Configs.WINDOWS_WID / 2.0 - Configs.WINDOWS_HGT / 10.0);
        endText.setLayoutY(Configs.WINDOWS_HGT * 7 / 8.0);
        getChildren().add(endText);
    }

    private void loadAllComponents() {

        loadBkGround();

        loadGameOverImg();

        loadWinSideImg();

        loadExitImg();

        loadEndText();
    }

    public GameEndView() {
        loadAllComponents();
    }


    public void setWinSide(boolean winSide) {
        theWinSide = winSide;
    }
}