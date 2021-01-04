package battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import battle.conf.Configs;
import battle.rec.AutoPlayer;
import battle.rec.RecordSave;
import battle.view.ViewBundle;
import battle.view.ViewGenerator;
import ground.IntPt;
import ground.GroundView;
import unit.DarkFollowers;
import unit.HuLuBaby;
import unit.HuLuCharacter;
import unit.OldMan;
import unit.Snake;
import unit.XieZiJing;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import net.SocketClient;
import net.SocketServer;

public class BattleController {
    public BattleView btView;
    ViewGenerator viewGen = null;
    public RecordSave recSave;
    public SocketServer server;
    public SocketClient client;

    ArrayList<HuLuCharacter> charas = new ArrayList<>();
    XieZiJing xiezijing;
    HuLuCharacter selectedChara = null;
    boolean isJustSide = true;
    boolean isAutoBattle = false;
    boolean isDoublePlayer = false;
    public boolean isAutoPlaying = false;
    public AtomicBoolean isPlayerAccessible = new AtomicBoolean(true);
    protected AtomicBoolean isEnd = new AtomicBoolean(false);
    protected int remainingSteps = 0;

    protected String loadFile;

    AutoPlayer autoPlayer = null;

    public static enum ACTION_TYPE {
        ACTION_CLICKED,
        ACTION_MOVEABOVE
    }

    public BattleController(BattleView v, String l, boolean s, boolean a, boolean db) throws IOException {
        this.btView = v;
        this.loadFile = l;
        this.isJustSide = s;
        this.isAutoBattle = a;
        this.isDoublePlayer = db;

        if (l != null)
            isAutoPlaying = true;
        else {//不为播放存档
            if (!isJustSide) {
                server = new SocketServer();
                server.start();
            } else {
                client = new SocketClient();
                client.start();
            }
        }
        viewGen = new ViewGenerator(this);
        remainingSteps = s ? 0 : 3;
        if (isAutoPlaying)
            autoPlayer = new AutoPlayer(l, this);

        try {
            addAllCharacters();
        } catch (Exception e) {
            System.out.println("add charactors fail");
            e.printStackTrace();
        }
        addBullets();
        Thread t = new Thread(viewGen);
        recSave = new RecordSave();
        t.start();
        t = new Thread(() -> {
            for (HuLuCharacter x : charas) {
                x.isVisible.set(true);
                x.cmd.set(1);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isPlayerAccessible.set(false);
            if (isAutoPlaying)
                new Thread(autoPlayer).start();
        });
        t.start();
    }

    protected void addHuLuCharacter(HuLuCharacter chara) {
        viewGen.addChara(chara);
        charas.add(chara);
        if (autoPlayer != null)
            autoPlayer.add(chara);
        Thread newThread = new Thread(chara);
        newThread.start();
        chara.cmd.set(0);
    }


    public static IntPt generateRandomMarginPt() {
        IntPt res = new IntPt(0, 0);
        Random temRand = new Random();
        if (temRand.nextBoolean() == true) {
            res.y = temRand.nextInt(7);
            if (temRand.nextBoolean() == true) {
                res.x = 0;

            } else {
                res.x = 9;
            }
        } else {
            res.x = temRand.nextInt(10);
            if (temRand.nextBoolean() == true) {
                res.y = 0;

            } else {
                res.y = 6;

            }
        }

        return res;
    }

    public void addAllCharacters() throws Exception {

        IntPt temPt = generateRandomMarginPt();
        OldMan grandpa = new OldMan(temPt.x, temPt.y, 0, GroundView.grHght - 1);
        addHuLuCharacter(grandpa);
        HuLuBaby[] brothers = new HuLuBaby[7];
        for (int i = 0; i < 7; i++) {
            temPt = generateRandomMarginPt();
            brothers[i] = new HuLuBaby(temPt.x, temPt.y, 1, i);
            addHuLuCharacter(brothers[i]);
        }
        temPt = generateRandomMarginPt();
        Snake snake = new Snake(temPt.x, temPt.y, 9, 0);
        addHuLuCharacter(snake);
        temPt = generateRandomMarginPt();
        XieZiJing scorpion = new XieZiJing(temPt.x, temPt.y, 9, 4);
        scorpion.initPos();
        addHuLuCharacter(scorpion);
        for (DarkFollowers x : scorpion.followerList)
            addHuLuCharacter(x);

    }

    public void addBullets() {//攻击效果
        for (Bullet x : ViewBundle.Attack1)
            viewGen.addBullet(x);
        for (Bullet x : ViewBundle.Attack2_1)
            viewGen.addBullet(x);
        for (Bullet x : ViewBundle.Attack2_2)
            viewGen.addBullet(x);
        for (Bullet x : ViewBundle.Attack2_3)
            viewGen.addBullet(x);
        for (Bullet x : ViewBundle.Attack3)
            viewGen.addBullet(x);
    }

    public HuLuCharacter newAction(IntPt loc, ACTION_TYPE type) {
        // 自动战斗模式下不能控制角色
        if (type == ACTION_TYPE.ACTION_CLICKED && isAutoBattle)
            return null;
        // 重放时不能控制角色
        if (type == ACTION_TYPE.ACTION_CLICKED && isAutoPlaying)
            return null;
        if (remainingSteps <= 0)
            stepDecrease();
        for (HuLuCharacter x : charas) {
            if (x.isAlive && x.isFree.get()) {
                IntPt vp = GroundView.realPtToPt(x.posX.get() + Configs.TILE_LEN / 2, x.posY.get() + Configs.TILE_LEN / 2);
                if (loc.x == vp.x && loc.y == vp.y) {
                    if (type == ACTION_TYPE.ACTION_CLICKED)
                        selectedChara = x;
                    if (type == ACTION_TYPE.ACTION_MOVEABOVE || (!isPlayerAccessible.get() && x.isDarkSide == isJustSide))
                        return x;
                }
            }
        }
        if (type == ACTION_TYPE.ACTION_CLICKED) {
            if (selectedChara != null && selectedChara.isDarkSide != isJustSide)
                selectedChara = null;
            if (!isPlayerAccessible.get() && selectedChara != null) {
                sendAndSave(selectedChara.confInd, loc.x, loc.y, RecordSave.SAVE_MODE.ST_MOVE);
                selectedChara.setDesPt(GroundView.ptToRealPt(loc.x, loc.y));
                selectedChara.cmd.set(1);

                stepDecrease();
                return selectedChara;
            }
        }
        return null;
    }

    public void stepDecrease() {//操作后设置剩余步数，判断是否交换控制权
        if (isEnd.get())
            return;
        remainingSteps--;
        btView.hint.setVal(remainingSteps, isJustSide);
        if (remainingSteps <= 0) {
            isPlayerAccessible.set(true);
            new Thread(() -> {
                // 等待所有角色完成当前操作
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean ready = true;
                    for (HuLuCharacter x : charas) {
                        if (x.isAlive && !x.isFree.get()) {
                            ready = false;
                            break;
                        }
                    }
                    if (ready)
                        break;
                }
                int waitSteps = isJustSide ? 3 : 2;
                for (; waitSteps > 0; --waitSteps) {//等待对手操作
                    String cmd = null;
                    int chatId, x, y, mode;
                    try {
                        if (!isJustSide)
                            cmd = server.read();
                        else
                            cmd = client.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert cmd != null;
                    String[] res = cmd.split(" ");
                    chatId = new Integer(res[0]);
                    x = new Integer(res[1]);
                    y = new Integer(res[2]);
                    mode = new Integer(res[3]);
                    nextMove(chatId, x, y, mode);
                }

                GroundView.mapLock.lock();
                for (HuLuCharacter x : charas)
                    if (x.isAlive)
                        GroundView.charaMap[GroundView.realYToY(x.posY.get())][GroundView.realXToX(x.posX.get())] = x;
                GroundView.mapLock.unlock();
                isPlayerAccessible.set(false);
            }).start();
            remainingSteps = isJustSide ? 2 : 3;
            btView.hint.setVal(remainingSteps, isJustSide);
        }
    }

    public void sendAndSave(int chatId, int dstx, int dsty, RecordSave.SAVE_MODE mode) {//发送进行的指令并保存
        String message = chatId + " " + dstx + " " + dsty + " " + mode.ordinal();
        if (!isJustSide)
            server.write(message);
        else
            client.write(message);
        recSave.addMove(chatId, dstx, dsty, mode);//保存
    }

    private void nextMove(int chatId, int dstx, int dsty, int saveMode) {//根据指令进行行动
        IntPt dst = new IntPt(0, 0);
        dst.x = dstx;
        dst.y = dsty;
        RecordSave.SAVE_MODE svMd = RecordSave.SAVE_MODE.values()[saveMode];
        recSave.addMove(chatId, dstx, dsty, svMd);//保存

        HuLuCharacter tmpchat = null;
        for (HuLuCharacter x : charas)         //找到本次操作的人物
        {
            if (x.confInd == chatId)
                tmpchat = x;
        }

        switch (svMd) {
            case ST_MOVE:
                tmpchat.setDesPt(GroundView.ptToRealPt(dst.x, dst.y));
                tmpchat.cmd.set(1);
                break;
            case ST_AT1:
                tmpchat.cmd.set(2);
                break;
            case ST_AT2:
                tmpchat.cmd.set(3);
                break;
            case ST_AT3:
                if (tmpchat.equals(xiezijing)) {
                    //蝎子精群体大招
                    for (HuLuCharacter x : xiezijing.followerList) {
                        if (x.isAlive && x.isFree.getAndSet(false)) {
                            x.cmd.set(2);
                        }
                    }
                    if (xiezijing.isAlive && xiezijing.isFree.getAndSet(false))
                        xiezijing.cmd.set(2);
                } else
                    tmpchat.cmd.set(4);
                break;
            default:
                System.out.println("load type error!");
        }
    }

    public void GameEnd(boolean monster) {//游戏结束
        isEnd.set(true);
        Platform.runLater(() -> {
            recSave.saveToFile("battle Result.xml");
            btView.endMask.setWinSide(monster);
            btView.endMask.setVisible(true);
            FadeTransition appear = new FadeTransition(Duration.seconds(2));
            appear.setFromValue(0.0);
            appear.setToValue(1.0);
            SequentialTransition sequence = new SequentialTransition(btView.endMask, appear);
            sequence.play();
        });
    }
}