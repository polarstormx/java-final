package unit;

import battle.conf.Configs;

public class HuLuBaby extends HuLuCharacter
{
    static protected int huLuBabyNums=0;     //每个葫芦娃都是唯一的，nextid用来记录下一个未出生的葫芦娃排行
    private HuLuBabyEnum huluProperties;            //葫芦娃属性
    public HuLuBaby(int stX, int stY, int edX, int edY) throws Exception
    {
        super(stX, stY, edX, edY);
        
        
        
        confInd = Configs.INDEX_HULU + huLuBabyNums;
        if(huLuBabyNums>=7)
            throw new Exception("葫芦娃天地之精华也，孕育七个已是极限！");
        this.huluProperties=HuLuBabyEnum.values()[HuLuBaby.huLuBabyNums];
        ++huLuBabyNums;
        
        setProps(huluProperties.getHP(),
        		100,huluProperties.getCostR(),
        		huluProperties.getCostM(),
        		huluProperties.getCostZ(),
        		"brother"+(huluProperties.ordinal()+1),
        		huluProperties.getName());
        
        isRemote = huluProperties.IsRemote();
    }
}