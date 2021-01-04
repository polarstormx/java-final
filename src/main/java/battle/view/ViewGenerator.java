package battle.view;

import java.util.ArrayList;

import battle.Bullet;
import battle.BattleController;
import battle.conf.Configs;
import ground.Tile;
import ground.IntPt;
import ground.GroundView;
import unit.HuLuCharacter;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

// 动画生成器
public class ViewGenerator implements Runnable {

    // 角色列表
    protected ArrayList<HuLuCharacter> huLuCharacterList = new ArrayList<>();
    protected ArrayList<Bullet> bulletList = new ArrayList<>();

    // 新角色
    public void addChara(HuLuCharacter chara) {
        huLuCharacterList.add(chara);
    }

    // 新攻击
    public void addBullet(Bullet b) {
        bulletList.add(b);
    }

    protected BattleController btController = null;

    public ViewGenerator(BattleController b) {
        this.btController = b;
    }

    @Override
    public void run() {
        boolean darkSideAlive = false; // 用于判断是否当前还有妖怪存活
        boolean justSideAlive = false; // 用于判断是否当前还有人类存活
        boolean isEnd = false; // 游戏是否结束
        while (true) {
            darkSideAlive = false;
            justSideAlive = false;
            try {
                Thread.sleep(1000 / 36);
            } catch (Exception e) {
                e.printStackTrace();
            }

            GraphicsContext graphicCon = btController.btView.canvas.getGraphicsContext2D();
            Platform.runLater(() -> {
                graphicCon.clearRect(0, 0, Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
            });

            /*
             * //MARK: Debug Map Platform.runLater(()->{ for(int
             * j=0;j<virtualField.height;j++) for(int i=0;i<virtualField.width;i++) {
             * virtualField.cmaplock.lock(); Charactor chat = virtualField.cmap[j][i];
             * virtualField.cmaplock.unlock(); if(chat==null || !chat.alive) {
             * gc.setFill(Color.WHITE); gc.fillRect(i*10,j*10,10,10); } else
             * if(chat.monster) { gc.setFill(Color.RED); gc.fillRect(i*10,j*10,10,10); }
             * else { gc.setFill(Color.BLUE); gc.fillRect(i*10,j*10,10,10); } } });
             */

            for (HuLuCharacter x : huLuCharacterList) {
                Tile block = Configs.genMidSize(x.posX.get(), x.posY.get());
                if (x.isAlive) {
                    if (x.isDarkSide)
                        darkSideAlive = true;
                    else
                        justSideAlive = true;
                    if (x.isVisible.get()) {
                        Platform.runLater(() -> {
//							if (x.isMoving.get())
//								gc.drawImage(
//										Configs.movableImgs.get(Math.min(Configs.movableImgs.size() - 1, x.confInd)),
//										block.bottomX - 0.25*block.topWid, block.y-0.25*block.topWid, 1.5 * block.topWid, 1.5 * block.topWid);
//							else
                            graphicCon.drawImage(Configs.commonImgs.get(Math.min(Configs.commonImgs.size() - 1, x.confInd)),
                                    block.bottomX - 0.25 * block.topWid, block.y - 0.25 * block.topWid, 1.5 * block.topWid, 1.5 * block.topWid);
                        });
                    }
                } else {
                    // 墓碑动画
                    Platform.runLater(() -> {
                        graphicCon.drawImage(Configs.sysImgs.get(Configs.IMG_INDEX.IMG_RIP.ordinal()), block.bottomX, block.y, block.topWid,
                                block.topWid);
                    });
                }
            }
            for (Bullet x : bulletList) {
                if (x.isVisible.get()) {
                    try {
                        Tile block = Configs.genMidSize(x.posX.get(), x.posY.get());
                        Platform.runLater(() -> {
                            graphicCon.drawImage(x.bulletImg, block.bottomX, block.y, block.topWid, block.topWid);
//                        gc.drawImage(x.icon,x.PositionX.get(), x.PositionY.get(),Configs.B_SIZE, Configs.B_SIZE);
                        });
                    } catch (Exception e) {
                        IntPt vloc = GroundView.realPtToPt(x.posX.get() + 1, x.posY.get() + 1);
                        System.out.println("bullet " + x.posX.get() + "," + x.posY.get());
                        System.out.println(vloc.x + "," + vloc.y);
                        e.printStackTrace();
                        throw e;
                    }
                }
            }

            if (!isEnd && (!darkSideAlive || !justSideAlive)) {
                // TODO: Game End
                System.out.println("Game End");
                isEnd = true;
                btController.GameEnd(darkSideAlive);
            }
        }
    }
}

/*
 * for(Bullet x:ViewBundle.Attack1) { if(x.visuable.get()) {
 * Platform.runLater(()->{ x.setVisible(false); x.setVisible(true);
 * x.relocate(x.PositionX.get(), x.PositionY.get()); }); } else {
 * Platform.runLater(()->{ x.setVisible(true); x.setVisible(false); }); } }
 *
 * for(Bullet x:ViewBundle.Attack2_1) { if(x.visuable.get()) {
 * Platform.runLater(()->{ x.setVisible(false); x.setVisible(true);
 * x.relocate(x.PositionX.get(), x.PositionY.get()); }); } else {
 * Platform.runLater(()->{ x.setVisible(true); x.setVisible(false); }); } }
 *
 * for(Bullet x:ViewBundle.Attack2_2) { if(x.visuable.get()) {
 * Platform.runLater(()->{ x.setVisible(false); x.setVisible(true);
 * x.relocate(x.PositionX.get(), x.PositionY.get()); }); } else {
 * Platform.runLater(()->{ x.setVisible(true); x.setVisible(false); }); } }
 *
 * for(Bullet x:ViewBundle.Attack2_3) { if(x.visuable.get()) {
 * Platform.runLater(()->{ x.setVisible(false); x.setVisible(true);
 * x.relocate(x.PositionX.get(), x.PositionY.get()); }); } else {
 * Platform.runLater(()->{ x.setVisible(true); x.setVisible(false); }); } }
 *
 * for(Bullet x:ViewBundle.Attack3) { if(x.visuable.get()) {
 * Platform.runLater(()->{ x.setVisible(false); x.setVisible(true);
 * x.relocate(x.PositionX.get(), x.PositionY.get()); }); } else {
 * Platform.runLater(()->{ x.setVisible(true); x.setVisible(false); }); } }
 *
 */