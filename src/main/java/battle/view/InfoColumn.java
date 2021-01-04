package battle.view;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.text.View;

import battle.conf.Configs;
import unit.HuLuCharacter;


public class InfoColumn extends Pane {
    ImageView infoBkGround;
    Text charaName;

    ImageView HPImg;
    ImageView MPImg;

    VolumeBar HPVolBar;
    VolumeBar MPVolBar;

    private void loadInfoBkGround() {

        infoBkGround = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_INFO_COL.ordinal()));


        ViewTools.setImgFit(infoBkGround,
                Configs.INFO_BAR_WID * 4 / 5.0,
                Configs.INFO_BAR_HGT);
        getChildren().add(infoBkGround);
        setMaxSize(Configs.INFO_BAR_WID * 4 / 5.0, Configs.INFO_BAR_HGT);
    }

    private void loadCharaName() {
        charaName = new Text("无");
        charaName.setFont(Font.font(23));
        charaName.setFill(Color.color(245 / 256.0, 225 / 256.0, 10 / 256.0, 1.0));
        charaName.setTextAlignment(TextAlignment.CENTER);
        charaName.setLayoutX(Configs.INFO_BAR_WID * 4 / 5.0 / 3.0);
        charaName.setLayoutY(Configs.INFO_BAR_HGT / 3.0);
        ViewTools.addPaneChildren(this, charaName);
    }

    private void loadHpImg() {
        HPImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_HP.ordinal()));
        ViewTools.setImgFit(HPImg, Configs.INFO_BAR_HGT / 6.0, Configs.INFO_BAR_HGT / 4.0);
        ViewTools.setImgXY(HPImg, Configs.INFO_BAR_HGT / 6.0, Configs.INFO_BAR_HGT / 3.0);
        ViewTools.addPaneChildren(this, HPImg);
    }

    private void loadMpImg() {
        MPImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_MP.ordinal()));

        ViewTools.setImgFit(MPImg, Configs.INFO_BAR_HGT / 6.0, Configs.INFO_BAR_HGT / 4.0);
        ViewTools.setImgXY(MPImg, Configs.INFO_BAR_HGT / 6.0, Configs.INFO_BAR_HGT / 3.0 * 2.0);

        ViewTools.addPaneChildren(this, MPImg);
    }

    private void loadHpVol() {
        HPVolBar = new VolumeBar(Configs.INFO_BAR_WID * 2 / 3 * 4 / 5, Configs.INFO_BAR_HGT / 7, Color.RED);
        ViewTools.setPaneLayout(HPVolBar, Configs.INFO_BAR_HGT * 5 / 12.0 + 10, Configs.INFO_BAR_HGT / 3.0 + Configs.INFO_BAR_HGT / 14.0);
    }

    private void loadMpVol() {
        MPVolBar = new VolumeBar(Configs.INFO_BAR_WID * 2 / 3 * 4 / 5, Configs.INFO_BAR_HGT / 7, Color.BLUE);
        MPVolBar.setLayoutX(Configs.INFO_BAR_HGT * 5 / 12.0 + 10);
        MPVolBar.setLayoutY(Configs.INFO_BAR_HGT / 3.0 * 2.0 + Configs.INFO_BAR_HGT / 14.0);
        ViewTools.setPaneLayout(MPVolBar, Configs.INFO_BAR_HGT * 5 / 12.0 + 10, Configs.INFO_BAR_HGT / 3.0 * 2.0 + Configs.INFO_BAR_HGT / 14.0);
    }

    private void loadHpMp() {
        loadHpImg();
        loadMpImg();
        loadHpVol();
        loadMpVol();

    }

    private void loadAllComponent() {
        loadInfoBkGround();
        loadCharaName();

        loadHpMp();
    }

    public InfoColumn() {
        loadAllComponent();


        getChildren().add(HPVolBar);
        getChildren().add(MPVolBar);


    }

    public void reSet(HuLuCharacter chara) {
        if (chara != null) {
            Platform.runLater(() -> {

                charaName.setText(chara.charaName);
                HPVolBar.set(chara.HP, chara.maxHP, false);
                MPVolBar.set(chara.MP, chara.maxMP, false);
            });
        }
    }

}

