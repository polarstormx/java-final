package battle.rec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import battle.BattleController;
import battle.rec.RecordSave.SAVE_MODE;
import ground.IntPt;
import ground.GroundView;
import unit.HuLuCharacter;
import unit.XieZiJing;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;


public class AutoPlayer implements Runnable {
    private String readfrom = null;
    private Document docs;
    private Element root;
    private BattleController bmanager;
    private XieZiJing scorpion;

    protected ArrayList<HuLuCharacter> creatures = new ArrayList<>();

    public AutoPlayer(String file, BattleController bmanager) {
        this.bmanager = bmanager;
        readfrom = file;
    }

    public void add(HuLuCharacter chat)
    {
        if(chat instanceof XieZiJing)
            scorpion = (XieZiJing) chat;
        creatures.add(chat);
    }

    public void init() {        //打开保存游戏信息的xml文件
        if (readfrom == null)
            return;
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            docs = db.parse(readfrom);
            root=docs.getDocumentElement();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("init ready");
    }

    public void waitforReady()      //等待当前动作结束
    {
        while(true)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean ready=true;
            for(HuLuCharacter x : creatures)
            {
                if(!x.isFree.get() && x.isAlive)
                {
                    ready=false;
                    
                    break;
                }
            }
            if(ready)
                break;
        }
    }

    @Override
    public void run() {
        init();
        if(docs==null)
            return;
        waitforReady();
        
        for(HuLuCharacter x:creatures)
            x.isAuto = true;

        NodeList rounds=root.getChildNodes();
        NodeList battle=rounds.item(1).getChildNodes();
        for(int j=0;j<battle.getLength();j++) {          //复现每一回合所有的操作
            Node thismove=battle.item(j);

            int Chatno=Integer.parseInt(thismove.getAttributes().getNamedItem("ChatId").getNodeValue());        //被操作的人物
            int type = Integer.parseInt(thismove.getFirstChild().getNodeValue());
            RecordSave.SAVE_MODE svMd = SAVE_MODE.values()[type];
            IntPt dst = new IntPt(0,0);          //移动的目的地
            dst.x = Integer.parseInt(thismove.getAttributes().getNamedItem("X").getNodeValue());
            dst.y = Integer.parseInt(thismove.getAttributes().getNamedItem("Y").getNodeValue());

            HuLuCharacter tmpchat=null;
            for(HuLuCharacter x:creatures)         //从人物列表中找到本次操作的人物
            {
                if(x.confInd==Chatno)
                    tmpchat=x;
            }

            switch(svMd)
            {
                case ST_MOVE:
                    tmpchat.setDesPt(GroundView.ptToRealPt(dst.x,dst.y));
                    tmpchat.cmd.set(1);
                    break;
                case ST_AT1:
                    tmpchat.cmd.set(2);
                    break;
                case ST_AT2:
                    tmpchat.cmd.set(3);
                    break;
                case ST_AT3:
                    tmpchat.cmd.set(4);
                    break;
                case ST_AT_GR:
                    for(HuLuCharacter x: scorpion.followerList)
                    {
                        if(x.isAlive && x.isFree.getAndSet(false)) {
                            x.cmd.set(2);
                        }
                    }
                    if(scorpion.isAlive && scorpion.isFree.getAndSet(false))
                        scorpion.cmd.set(2);
                    break;
                default:
                    System.out.println("load type error!");
            }

            waitforReady();
            GroundView.mapLock.lock();
            for(HuLuCharacter x:creatures)
                if(x.isAlive)
                    GroundView.charaMap[GroundView.realYToY(x.posY.get())][GroundView.realXToX(x.posX.get())] = x;
            GroundView.mapLock.unlock();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(HuLuCharacter x:creatures)
            x.isAuto = false;

        bmanager.isAutoPlaying = false;
        bmanager.isPlayerAccessible.set(false);
    }
}
