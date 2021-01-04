package unit;

import battle.conf.Configs;
import interfaces.IsCharacter;

@IsCharacter(name = "蛇精", side = "黑暗方", HP = 70, MP = 100, damageR = 30, damageAOE = 50, damageZXC = 100, canCure = true, isRemote = false)
public class Snake extends HuLuCharacter {
	public Snake(int stX, int stY, int edX, int edY) {
		super(stX, stY, edX, edY);
		confInd = Configs.INDEX_SNAKE;
		setProps(70, 100, 30, 50, 100, "snake", "蛇精");
		isDarkSide = true;
		canCure = true;
	}
}