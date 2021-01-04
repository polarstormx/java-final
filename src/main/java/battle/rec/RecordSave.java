package battle.rec;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class RecordSave {

    public static enum SAVE_MODE{
    	ST_MOVE,
    	ST_AT1,
    	ST_AT2,
    	ST_AT3,
    	ST_AT_GR
    }

    private boolean newround = true;

    private Document docs;
    private Element root;
    private Element round;

    public RecordSave()
    {
        init();
    }

    public void init() {            //创建并打开xml文件
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            docs = db.newDocument();
            root = docs.createElement("battle");

            String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());       //写入储存时间信息
            Element ctime = docs.createElement("ctime");
            Text m = docs.createTextNode(time);
            ctime.appendChild(m);
            root.appendChild(ctime);
            docs.appendChild(root);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void addMove(int chatId, int dstx, int dsty, SAVE_MODE saveMode)
    {
        addMove(chatId, dstx, dsty, saveMode, 0);
    }
    /**
     * 添加一次操作信息
     * @param chatId 角色ID
     * @param dstx 移动目的地横坐标
     * @param dsty 移动目的地纵坐标
     * @param  操作类型
     */
    public void addMove(int chatId, int dstx, int dsty, SAVE_MODE saveMode, int fmt)
    {
        if(newround) {
            if(round!=null)
                root.appendChild(round);
            round = docs.createElement("battle");
            newround = false;
        }
        Element move=docs.createElement("move");
        move.setAttribute("ChatId",Integer.toString(chatId));
        move.setAttribute("Y",Integer.toString(dsty));
        move.setAttribute("X",Integer.toString(dstx));
        Text m=docs.createTextNode(Integer.toString(saveMode.ordinal()));
        move.appendChild(m);
        round.appendChild(move);
    }

    /** 将战斗过程写入文件*/
    public void saveToFile(String saveto){        //写入文件
        if (saveto == null || round==null)
            return;
        root.appendChild(round);
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(docs);

            Properties p=new Properties();
            p.setProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.setOutputProperties(p);
            File f = new File(saveto);
            StreamResult result = new StreamResult(new FileOutputStream(f));

            transformer.transform(domSource, result);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
