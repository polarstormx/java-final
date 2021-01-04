package battle.view;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import battle.BattleController;
import battle.conf.Configs;
import battle.rec.RecordSave;
import unit.HuLuCharacter;

// 选择技能的窗口
public class SelectionBar extends Pane {

    Text name;
    Label attackComm;
    Label attackAOE;
    Label attackZXC;
    HuLuCharacter curChara = null;
    public BattleController battleController = null;
    private void loadImgSkillText() {
        ImageView imgSkillText = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_FLOAT_BAR.ordinal()));
        ViewTools.setImgFit(imgSkillText, Configs.SELECTION_BAR_WID, Configs.SELECTION_BAR_HGT);

        getChildren().add(imgSkillText);
    }

    private void loadName() {
        name = new Text("空");
        name.setFont(Font.font("楷体", FontWeight.BOLD, 35));
        name.setFill(Color.BLUE);
        name.setTextAlignment(TextAlignment.CENTER);
        name.setY(Configs.SELECTION_BAR_HGT / 5.0);
        name.setX(Configs.SELECTION_BAR_WID / 2.5);

        getChildren().add(name);
    }


    private void loadAttackComm() {
        ImageView imgComm = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_B_ATTACK1.ordinal()));
        ViewTools.setImgFit(imgComm, Configs.SELECTION_BAR_WID * 4.0 / 5.0, Configs.SELECTION_BAR_HGT / 10.0);
        attackComm = new Label("", imgComm);

        ViewTools.setLabelPara(attackComm,
                Configs.SELECTION_BAR_WID / 10.0,
                Configs.SELECTION_BAR_HGT / 5.0 * 2.0 - Configs.SELECTION_BAR_HGT / 10.0
        );

        ViewTools.setNodeHighLight(attackComm, imgComm, Configs.IMG_INDEX.IMG_B_ATTACK1, Configs.IMG_INDEX.IMG_B_ATTACK1_H);
        attackComm.setOnMouseClicked((MouseEvent) -> {
            if (curChara != null && curChara.isFree.getAndSet(false)
                    && (battleController == null || !battleController.isPlayerAccessible.get())) {
                if (battleController != null)
                    battleController.stepDecrease();
                //battleController.recSave.addMove(curChara.confInd, 0, 0, RecordSave.SAVE_MODE.ST_AT1);
                battleController.sendAndSave(curChara.confInd, 0, 0, RecordSave.SAVE_MODE.ST_AT1);
                curChara.cmd.set(2);
                updateInfo(null);
            }
        });

        attackComm.setVisible(false);
        ViewTools.addPaneChildren(this, attackComm);
    }

    private void loadAttackAOE() {
        ImageView aoeImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_B_ATTACK2.ordinal()));
        ViewTools.setImgFit(aoeImg, Configs.SELECTION_BAR_WID * 4.0 / 5.0, Configs.SELECTION_BAR_HGT / 10.0);
        attackAOE = new Label("", aoeImg);

        ViewTools.setLabelPara(attackAOE,
                Configs.SELECTION_BAR_WID / 10.0,
                Configs.SELECTION_BAR_HGT / 5.0 * 3.0 - Configs.SELECTION_BAR_HGT / 10.0);
        ViewTools.setNodeHighLight(attackAOE, aoeImg, Configs.IMG_INDEX.IMG_B_ATTACK2, Configs.IMG_INDEX.IMG_B_ATTACK2_H);
        attackAOE.setOnMouseClicked((MouseEvent) -> {
            if (curChara != null && curChara.isFree.getAndSet(false)
                    && (battleController == null || !battleController.isPlayerAccessible.get())) {
                if (battleController != null)
                    battleController.stepDecrease();
                battleController.sendAndSave(curChara.confInd, 0, 0, RecordSave.SAVE_MODE.ST_AT2);
                curChara.cmd.set(3);
                updateInfo(null);
            }
        });

        attackAOE.setVisible(false);
        ViewTools.addPaneChildren(this, attackAOE);
    }

    private void loadAttackZXC() {
        ImageView zxcImg = new ImageView(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_B_ATTACK3.ordinal()));
        ViewTools.setImgFit(zxcImg, Configs.SELECTION_BAR_WID * 4.0 / 5.0, Configs.SELECTION_BAR_HGT / 10.0);
        attackZXC = new Label("", zxcImg);
        ViewTools.setLabelPara(attackZXC,
                Configs.SELECTION_BAR_WID / 10.0,
                Configs.SELECTION_BAR_HGT / 5.0 * 4.0 - Configs.SELECTION_BAR_HGT / 10.0);

        ViewTools.setNodeHighLight(attackZXC, zxcImg, Configs.IMG_INDEX.IMG_B_ATTACK3, Configs.IMG_INDEX.IMG_B_ATTACK3_H);
        attackZXC.setOnMouseClicked((MouseEvent) -> {
            if (curChara != null && curChara.isFree.getAndSet(false)
                    && (battleController == null || !battleController.isPlayerAccessible.get())) {
                if (battleController != null)
                    battleController.stepDecrease();
                battleController.sendAndSave(curChara.confInd, 0, 0, RecordSave.SAVE_MODE.ST_AT3);
                curChara.cmd.set(4);
                updateInfo(null);
            }
        });

        attackZXC.setVisible(false);
        ViewTools.addPaneChildren(this, attackZXC);
    }

    public SelectionBar() {
        loadImgSkillText();

        loadName();

        loadAttackComm();

        loadAttackAOE();

        loadAttackZXC();

    }

    // 更新技能栏信息
    public void updateInfo(HuLuCharacter chara) {
        curChara = chara;
        attackComm.setVisible(false);
        attackAOE.setVisible(false);
        attackZXC.setVisible(false);
        if (curChara != null) {
            name.setText(curChara.charaName);
            attackComm.setVisible(true);
            if (curChara.isAOEReady)
                attackAOE.setVisible(true);
            if (curChara.isZXCReady)
                attackZXC.setVisible(true);
        }
    }

}
