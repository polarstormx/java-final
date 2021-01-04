package unit;

/** 葫芦娃兄弟们属性的枚举类型 */
public enum HuLuBabyEnum {
	HONG_WA, CHENG_WA, HUANG_WA, LV_WA, QING_WA, LAN_WA, ZI_WA;

	static protected String[] nameList = { "大娃", "二娃", "三娃", "四娃", "五娃", "六娃", "七娃" };
	static protected String[] colorList = { "红色", "橙色", "黄色", "绿色", "青色", "蓝色", "紫色" };
	static protected String[] imgName = { "brother1.PNG", "brother2.PNG", "brother3.PNG", "brother4.PNG",
			"brother5.PNG", "brother6.PNG", "brother7.PNG" };
	// 各项属性
	static protected int[] HP = { 120, 90, 80, 60, 100, 150, 60 };
	static protected int[] costR = { 10, 10, 5, 20, 20, 30, 10 };
	static protected int[] costM = { 20, 40, 20, 40, 30, 20, 20 };
	static protected int[] costZ = { 90, 70, 80, 90, 90, 60, 50 };
	
	// 是否是远程单位
	static protected boolean[] isRemote = { false, true, true, false, false, false, false };
	
	
	public String getName() {
		return nameList[ordinal()];
	}

	public String getColor() {
		return colorList[ordinal()];
	}

	public boolean IsRemote() {
		return isRemote[ordinal()];
	}

	public int getHP() {
		return HP[ordinal()];
	}

	public int getCostR() {
		return costR[ordinal()];
	}

	public int getCostM() {
		return costM[ordinal()];
	}

	public int getCostZ() {
		return costZ[ordinal()];
	}
}
