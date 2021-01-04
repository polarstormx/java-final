package unit;

import battle.conf.Configs;
import interfaces.IsCharacter;

@IsCharacter(name = "爷爷", side = "正义方", HP = 80, MP = 100, damageR = -30, damageAOE = -30, damageZXC = -100, canCure = true, isRemote = true)
public class OldMan extends HuLuCharacter {
	public OldMan(int sx, int sy, int nx, int ny) {
		super(sx, sy, nx, ny);
		confInd = Configs.INDEX_OLDMAN;
		setProps(80, 100, -30, -30, -100, "grandpa", "爷爷");
		isRemote = true;
		canCure = true;
	}
}