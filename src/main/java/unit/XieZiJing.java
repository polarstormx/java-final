package unit;

import battle.BattleController;
import battle.BulletController;
import battle.conf.Configs;
import battle.conf.E_ATTACK;
import ground.IntPt;
import interfaces.IsCharacter;
import ground.GroundView;

@IsCharacter(name = "蝎子精", side = "黑暗方", HP = 120, MP = 100, damageR = 20, damageAOE = 40, damageZXC = 80, canCure = false, isRemote = true)
public class XieZiJing extends HuLuCharacter {
    public static final int followerSize = 13;
    //public int curFMTInd = -1;
	private final int[][] posList = new int[][] { { 0, -3 }, { 0, -2 }, { 0, -1 }, { 0, 1 }, { 0, 2 }, { 0, 3 },
			{ 1, -3 }, { 1, -2 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 } };
	// 小喽啰位置

	@Override
	protected void Attack1() {
		for (HuLuCharacter x : followerList) {
			if (x.isAlive && x.isFree.getAndSet(false))
				x.cmd.set(2);
		}
		super.Attack1();
	}

	@Override
	protected void Attack2() {
		MP -= costM;
		if (MP < costM) // MP不足时不能再使用对群攻击
		{
			isAOEReady = false;
			isFree.set(true);
		} else {
			BulletController.start(this, E_ATTACK.ATTACK_AOE_1);
			BulletController.start(this, E_ATTACK.ATTACK_AOE_2);
			BulletController.start(this, E_ATTACK.ATTACK_AOE_3);
		}

		isFree.set(true);
	}

	@Override
	protected void Attack3() {
		for (HuLuCharacter x : followerList) {
			if (x.isAlive && x.isFree.getAndSet(false))
				x.cmd.set(4);
		}
		super.Attack3();
	}
            
            
	public DarkFollowers[] followerList = new DarkFollowers[13];
	// 蝎子精带有13个小喽啰，恰好可以与其站成两排

    public XieZiJing(int stX, int stY, int edX, int edY) {
        super(stX, stY, edX, edY);
        confInd = Configs.INDEX_XIEZIJING;
        setProps(120, 100, 20, 40, 80, "XieZiJing", "蝎子精");
        isDarkSide = true;
        isRemote = true;
		for (int i = 0; i < followerList.length; ++i) {
			IntPt temPt = BattleController.generateRandomMarginPt();
			followerList[i] = new DarkFollowers(temPt.x, temPt.y, edX, edY, i);
		
		
		}
	}

    private void followerMove(IntPt pt, DarkFollowers fl) {
    	fl.setDesPt(pt);
    	fl.cmd.set(1);
    }
    
    private int genLouLoVpos(int i, int xory, int vpos) {
    	return vpos - posList[i][xory];
    }
    
    // 小喽啰和自身的初始位置从这里设置
    public void initPos() {
        isFree.set(false);
        // 计算蝎子精在中间站
        int vStPtX = GroundView.grWidth - 4;
        int vStPtY = (GroundView.grHght - 1) / 2;
        
        IntPt realStPt = GroundView.ptToRealPt(vStPtX, vStPtY);
        setDesPt(realStPt.x, realStPt.y);
        cmd.set(1);
        
        // 根据相对位置运算初始位置
        for (int i = 0; i < followerList.length; i++) {
//            int louLoVX = vStPtX - posList[i][0];
        	int louLoVX = genLouLoVpos(i, 0, vStPtX);
//            int louLoVY = vStPtY - posList[i][1];
        	int louLoVY = genLouLoVpos(i, 1, vStPtY);
            if (followerList[i].isAlive) {
//                followerList[i].moveTo(GroundView.ptToRealPt(louLoVX, louLoVY));
//                followerList[i].cmd.set(1);
            	followerMove(GroundView.ptToRealPt(louLoVX, louLoVY),followerList[i]);
            }
        }
    }


}