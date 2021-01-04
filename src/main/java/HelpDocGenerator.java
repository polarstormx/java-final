

import interfaces.IsCharacter;
import unit.DarkFollowers;
import unit.HuLuBabyEnum;
import unit.OldMan;
import unit.XieZiJing;
import unit.Snake;

import java.io.*;

public class HelpDocGenerator{
//    static HintGenerater generater;
    static Class<?>[] clazzlist = {Snake.class, XieZiJing.class, DarkFollowers.class, OldMan.class};
    static StringBuilder helpStrBuilder = new StringBuilder("");
//    static{
//        generater = new HintGenerater();
//    }
    private HelpDocGenerator(){ }
    
    private static void generateDarkHelpDoc() {
        for(Class<?> clazz:clazzlist)
        {
            if(clazz.isAnnotationPresent(IsCharacter.class))
            {
                IsCharacter annotation = (IsCharacter) clazz.getAnnotation(IsCharacter.class);
                helpStrBuilder.append(annotation.name()+"\t("+annotation.side()+"):");
                helpStrBuilder.append("\n\t技能类型: \t" + (annotation.canCure()?"可以治疗，不可以普通攻击\t":"可以普通攻击，不可治疗\t") + (annotation.isRemote()?"":"不") + "具有远程能力");
                helpStrBuilder.append("\n\tHP\t血量: "+annotation.HP());
                helpStrBuilder.append("\n\tMP\t魔法值: "+annotation.MP());
                helpStrBuilder.append("\n\t普通攻击伤害: "+annotation.damageR());
                helpStrBuilder.append("\n\t群体伤害: "+annotation.damageAOE());
                helpStrBuilder.append("\n\t大招伤害: "+annotation.damageZXC());
                helpStrBuilder.append("\n\n");
            }
        }
    }
    
    private static void generateJustHelpDoc() {
    	HuLuBabyEnum[] brothers = HuLuBabyEnum.values();
        for(HuLuBabyEnum boy:brothers)
        {
            helpStrBuilder.append(boy.getName()+"\t(正义方):");
            helpStrBuilder.append("\n\t技能类型: " + "可以普通攻击\t" + (boy.IsRemote()?"":"不") + "具有远程能力");
            helpStrBuilder.append("\n\tHP\t血量: "+boy.getHP());
            helpStrBuilder.append("\n\tMP\t魔法值: "+100);
            helpStrBuilder.append("\n\t普通攻击伤害: "+boy.getCostR());
            helpStrBuilder.append("\n\t群体伤害: "+boy.getCostM());
            helpStrBuilder.append("\n\t大招伤害: "+boy.getCostZ());
            helpStrBuilder.append("\n\n");
        }
    }
    
    private static void saveToFile() {
        File file = new File("Help Doc.txt");
        try{
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(helpStrBuilder.toString());
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static void generateHelpDoc(){
//        if(generater==null)
//            return false;
        
        
        generateDarkHelpDoc();
        
        generateJustHelpDoc();
        
        saveToFile();
        

//        generater = null;
//        return true;
    }
}