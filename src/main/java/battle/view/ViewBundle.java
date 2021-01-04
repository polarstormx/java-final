package battle.view;

import java.util.ArrayList;

import battle.Bullet;
import battle.conf.Configs;
import unit.XieZiJing;

public final class ViewBundle {

    //每个人物及他们攻击效果的UI对象，使用相同下标
    public static ArrayList<Bullet> Attack1 = new ArrayList<>();
    public static ArrayList<Bullet> Attack2_1 = new ArrayList<>();
    public static ArrayList<Bullet> Attack2_2 = new ArrayList<>();
    public static ArrayList<Bullet> Attack2_3 = new ArrayList<>();
    public static ArrayList<Bullet> Attack3 = new ArrayList<>();


    public static void init() {

        int ind = 0;

        for (; ind < Configs.imgNameList.length; ind++) {
            Attack1.add(new Bullet(Configs.attack1Imgs.get(ind)));

            Attack2_1.add(new Bullet(Configs.attack2Imgs.get(ind)));
            Attack2_2.add(new Bullet(Configs.attack2Imgs.get(ind)));
            Attack2_3.add(new Bullet(Configs.attack2Imgs.get(ind)));

            Attack3.add(new Bullet(Configs.attack3Imgs.get(ind)));


        }
        ind--;
        for (int i = 0; i < XieZiJing.followerSize - 1; ++i) {
            Attack1.add(new Bullet(Configs.attack1Imgs.get(ind)));

            Attack2_1.add(new Bullet(Configs.attack2Imgs.get(ind)));
            Attack2_2.add(new Bullet(Configs.attack2Imgs.get(ind)));
            Attack2_3.add(new Bullet(Configs.attack2Imgs.get(ind)));

            Attack3.add(new Bullet(Configs.attack3Imgs.get(ind)));
        }
    }
}

