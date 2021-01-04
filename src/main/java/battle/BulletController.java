package battle;

import battle.conf.Configs;
import battle.conf.E_ATTACK;
import battle.view.ViewBundle;
import ground.IntPt;
import ground.GroundView;
import unit.HuLuCharacter;

public class BulletController{

	// 此处修改可以将技能范围及方向进行修正
    public static void start(HuLuCharacter chara, E_ATTACK type)
    {
        Bullet bulletView;
        int charaPosX=chara.posX.get();
        int charaPosY=chara.posY.get();
        IntPt bulletPos = GroundView.realPtToPt(charaPosX+Configs.TILE_LEN/2, charaPosY+Configs.TILE_LEN/2);
        
        
        int damage = 0;
        HuLuCharacter attackDes = null;
        switch (type)
        {
		case ATTACK_AOE_1:
			bulletView = ViewBundle.Attack2_1.get(chara.confInd);
			damage = chara.costM;
			bulletPos.x += chara.isDarkSide ? -1 : 1;
			break;
		case ATTACK_AOE_2:
			bulletView = ViewBundle.Attack2_2.get(chara.confInd);
			damage = chara.costM;
			bulletPos.y += 1;
			break;
		case ATTACK_AOE_3:
			bulletView = ViewBundle.Attack2_3.get(chara.confInd);
			damage = chara.costM;
			bulletPos.y -= 1;
			break;
		case ATTACK_REG:
			bulletView = ViewBundle.Attack1.get(chara.confInd);
			damage = chara.costR;
			bulletPos.x += chara.isDarkSide ? -1 : 1;
			break;
		case ATTACK_ZXC:
			bulletView = ViewBundle.Attack3.get(chara.confInd);
			damage = chara.costZ;
			bulletPos.x += chara.isDarkSide ? -1 : 1;
			break;

		default:
			return;
        }
        IntPt realPos = GroundView.ptToRealPt(bulletPos.x, bulletPos.y);
        bulletView.posX.set(realPos.x);
        bulletView.posY.set(realPos.y);
        bulletView.isVisible.set(true);

        //不是远程攻击
        //或者是爆炸伤害
        if(!chara.isRemote || type == E_ATTACK.ATTACK_AOE_1 || type == E_ATTACK.ATTACK_AOE_2 || type == E_ATTACK.ATTACK_AOE_3)
        {
            if(bulletPos.y>=0 && bulletPos.y<GroundView.grHght && bulletPos.x>=0 && bulletPos.x<GroundView.grWidth)
                attackDes = GroundView.charaMap[bulletPos.y][bulletPos.x];
            if(attackDes != null && ((attackDes.isDarkSide!=chara.isDarkSide && !chara.canCure) || (attackDes.isDarkSide==chara.isDarkSide && chara.canCure)))
                attackDes.HPLost(damage);
            try {
                if (type == E_ATTACK.ATTACK_AOE_1 || type == E_ATTACK.ATTACK_AOE_2 || type == E_ATTACK.ATTACK_AOE_3)
                {
                    if (chara.isAuto)
                        Thread.sleep(120);
                    else
                        Thread.sleep(600);
                }
                else
                {
                    if (chara.isAuto)
                        Thread.sleep(300);
                    else
                        Thread.sleep(1800);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bulletView.isVisible.set(false);
            return;
        }
        int desPosX = bulletPos.x;
        int timeCost;
        double posChangePerFrame;
        if(!chara.isDarkSide)
        {
            while (true) {
                if (desPosX < GroundView.grWidth &&
                        (GroundView.charaMap[bulletPos.y][desPosX] == null ||
                                !GroundView.charaMap[bulletPos.y][desPosX].isAlive ||
                                (GroundView.charaMap[bulletPos.y][desPosX].isDarkSide == chara.canCure)))
                    desPosX++;
                else
                    break;
            }
            desPosX = Math.min(desPosX, GroundView.grWidth-1);
            realPos.x -= Configs.TILE_LEN/2;
            timeCost = 2+(desPosX - bulletPos.x + 1) * 15;     //攻击飞行时间和距离成正比
            posChangePerFrame=(desPosX - bulletPos.x + 0.5)*Configs.TILE_LEN/(double)timeCost;
        }
        else
        {
            while (true) {
                if (desPosX > 0 &&
                        (GroundView.charaMap[bulletPos.y][desPosX] == null ||
                                !GroundView.charaMap[bulletPos.y][desPosX].isAlive ||
                                (GroundView.charaMap[bulletPos.y][desPosX].isDarkSide != chara.canCure)))
                    desPosX--;
                else
                    break;
            }
            desPosX = Math.max(0,desPosX);
            realPos.x += Configs.TILE_LEN/2;
            timeCost = 2+(bulletPos.x - desPosX +1) * 5;
            if(chara.isAuto)
                timeCost /= 4;
            posChangePerFrame = -(bulletPos.x - desPosX)*Configs.TILE_LEN/(double)timeCost;
        }
        attackDes=GroundView.charaMap[bulletPos.y][desPosX];

        for(int i=0;i<timeCost;i++)
        {
            bulletView.posX.set(realPos.x);
            bulletView.posY.set(realPos.y);
            realPos.x += posChangePerFrame;
            try {
                Thread.sleep(1000/36);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bulletView.isVisible.set(false);
        if(attackDes != null)
            attackDes.HPLost(damage);
    }
}
