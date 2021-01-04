package unit;

import battle.conf.Configs;
import interfaces.IsCharacter;

@IsCharacter(name = "蛤蟆精", side = "黑暗方", HP = 60, MP = 100, damageR = 10, damageAOE = 0, damageZXC = 30, canCure = false, isRemote = true)
public class DarkFollowers extends HuLuCharacter {
	int followerIndex = 0;

	public DarkFollowers(int stX, int stY, int edX, int edY, int fi) {
		super(stX, stY, edX, edY);
		setProps(60, 100, 10, 0, 30, "LouLo", "喽啰");
		
		this.followerIndex = fi;
		confInd = Configs.INDEX_LOULO + fi;
		
		isDarkSide = true;
		isRemote = true;
		isAOEReady = false;
	}
}