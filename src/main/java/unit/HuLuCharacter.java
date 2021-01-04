package unit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import battle.BulletController;
import battle.conf.E_ATTACK;
import ground.IntPt;
import ground.GroundView;

// 角色的基类
public class HuLuCharacter implements Runnable {

	// 是否空闲
	public AtomicBoolean isFree = new AtomicBoolean(true);
	// 当前指令（由于原子化需要没用enum）
	public AtomicInteger cmd = new AtomicInteger(0);
	// 是否可见
	
	public AtomicBoolean isVisible = new AtomicBoolean(false);
	// 是否在移动中
	public AtomicBoolean isMoving = new AtomicBoolean(false);


	
	// 角色位置
	public AtomicInteger posX = new AtomicInteger(0);
	public AtomicInteger posY = new AtomicInteger(0);
	// 角色目标位置
	protected int desX;
	protected int desY;
	
	
	// 配置下标
	public int confInd;
	// 角色的基础移速
	protected int charaBaseSpeed = 5;
	// 角色资源字符串
	public String rsrcName;
	// 角色的姓名字符串
	public String charaName;
	// 角色是否存活
	public boolean isAlive = true;
	// 是否为黑暗阵营
	public boolean isDarkSide = false;
	// 是否可以远程攻击
	public boolean isRemote = false;
	// 是否可以治疗
	public boolean canCure = false;
	// 是否是自动模式
	public boolean isAuto = false;

	
	// 下面均为战斗属性
	public int maxHP = 100;
	public int HP = 100;
	
	public int maxMP = 100;
	public int MP = 100;
	
	public int costR = 10;
	public int costM = 20;
	public int costZ = 50;
	
	// 当前是否可用AOE
	public boolean isAOEReady = true;
	// 当前是否可用ZXC
	public boolean isZXCReady = true;

	
	public HuLuCharacter(int stX, int stY, int edX, int edY) {
		IntPt stPt = GroundView.ptToRealPt(stX, stY);
		IntPt edPt = GroundView.ptToRealPt(edX, edY);
		
		posX.set(stPt.x);
		desX = edPt.x;
		
		posY.set(stPt.y);
		desY = edPt.y;
	}

	// 设置属性
	protected void setProps( int h, int m, int r, int cm, int cz, String b, String n) {
		this.maxHP = this.HP = h;
		this.maxMP = this.MP = m;

		this.costR = r;
		this.costM = cm;
		this.costZ = cz;
		
		this.rsrcName = b;
		this.charaName = n;
	}

	// 设置目标点
	public void setDesPt(IntPt desPt) {
		setDesPt(desPt.x, desPt.y);
	}


	public void setDesPt(int x, int y){ 
		// 移动到新位置
		desX = x;
		desY = y;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handleCharaCmd(); // 命令处理函数
		}
	}

	/** 在循环中等待新命令 */
	protected void handleCharaCmd() {
//		if (cmd.get() - 1 == E_ATTACK.ATTACK_REG.ordinal()) {
//			
//		}
//		else if (cmd.get() - 1 == E_ATTACK.ATTACK_REG.ordinal())
		
		switch (cmd.get()) {
		case 0:
			break; // 当前无任务
		
		case 1:
			generateViews();
			cmd.set(0);
			break; // 1 号命令为移动到目的地
		case 2:
			Attack1();
			cmd.set(0);
			break;
		case 3:
			Attack2();
			cmd.set(0);
			break;
		case 4:
			Attack3();
			cmd.set(0);
			break;
		default:
			break;
		}
	}

	private int genTime(double disX, double disY) {
		double disAll = Math.sqrt(disX * disX + disY * disY);
		return (int)disAll/charaBaseSpeed;
	}
	
	private double genMovChange(double dis, int timeCost) {
		return dis/timeCost;
	}
	
//	private void flipIsMoving() {
//		isMoving.set(!isMoving.get());
//	}
	
	public void setPos(int pX, int pY) {
		posX.set(pX);
		posY.set(pY);
	}
	
	private void genEachPos(int timeCost, double xChange, double yChange) {
		double curX = posX.get();
		double curY = posY.get();
		
		for (int i = 0; i < timeCost; i++) {
			curX += xChange;
			posX.set((int) curX);
			
			curY += yChange;
			posY.set((int) curY);
//            System.out.println(name +PositionX.get() +","+PositionY.get());
			try {
				Thread.sleep(1000 / 108);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 动画效果 可以删去
//			if (i % 6 == 0)
//				//isMoving.set(!isMoving.get());
//				flipIsMoving();
		}
	}
	
	// 产生动画
	protected void generateViews() {
		isFree.set(false);
		double disX = desX - posX.get();
		double disY = desY - posY.get();
		
//		
//
		disX = (desX - posX.get());
		disY = (desY - posY.get());
//		double distance = Math.sqrt(disX * disX + disY * disY);
//		int timeCost = (int) distance / charaBaseSpeed; // 移动动画所需时间=距离/速度
	
		int timeCost = genTime(disX, disY);
		
		//double xChange = disX/timeCost;
		//double yChange = disY/timeCost;
		double xChange = genMovChange(disX, timeCost);
		double yChange = genMovChange(disY, timeCost);
		
//		double curX = posX.get();
//		double curY = posY.get();

//        virtualField.set(this, DstX, DstY,false);
//        virtualField.set(this, PositionX.get(), PositionY.get(),true);
		
		// 把角色在ground上的位置设定好
		GroundView.set(this, posX.get(), posY.get(), desX, desY);
		
//		for (int i = 0; i < timeCost; i++) {
//			curX += xChange;
//			posX.set((int) curX);
//			
//			curY += yChange;
//			posY.set((int) curY);
////            System.out.println(name +PositionX.get() +","+PositionY.get());
//			try {
//				Thread.sleep(1000 / 108);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if (i % 6 == 0)
//				//isMoving.set(!isMoving.get());
//				flipIsMoving();
//		}
		genEachPos(timeCost, xChange, yChange);
		
		setPos(desX, desY);
		isMoving.set(false);
		isFree.set(true);
	}

	protected void Attack1() {
		BulletController.start(this, E_ATTACK.ATTACK_REG);
		isFree.set(true);
	}

	private void MPCost() {
		MP -= costM;
	}
	
	protected void Attack2() {
//		MP -= costM;
		MPCost();
		// MP 不足 不可群体攻击
		if (MP < costM)
		{
			isAOEReady = false;
			isFree.set(true);
			return;
		}
		BulletController.start(this, E_ATTACK.ATTACK_AOE_1);
		BulletController.start(this, E_ATTACK.ATTACK_AOE_2);
		BulletController.start(this, E_ATTACK.ATTACK_AOE_3);
		isFree.set(true);
	}

	protected void Attack3() {
		// 大招只可使用一次
		isZXCReady = false;
		BulletController.start(this, E_ATTACK.ATTACK_ZXC);
		isFree.set(true);
	}

	public synchronized void HPLost(int l)
	{
//        System.out.println(name + cost);
		HP = (l < 0 && HP - l > maxHP)? maxHP:HP-l;
		
		
		deathJudge();
	}

	protected void deathJudge() // 死亡
	{
		if (HP <= 0)
			this.isAlive = false;
	}
}
