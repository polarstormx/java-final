package battle.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VolumeBar extends Pane {
    Rectangle barBorder;
    Rectangle barFill;
    int wid;
    int hgt;
    double volValue;// 血量，一开始以宽度表示
    Thread t;

    private void setBarBorder() {
        barBorder = new Rectangle(0, 0, wid, hgt);
        barBorder.setStroke(Color.gray(150.0 / 256.0));
        barBorder.setFill(null);
    }

    private void setBarFill(Color c) {
        barFill = new Rectangle(0, 0, wid, hgt);
        barFill.setFill(c);
    }

    public VolumeBar(int w, int h, Color c) {
        this.wid = w;
        this.volValue = w;
        this.hgt = h;

        setBarBorder();

        setBarFill(c);

        getChildren().add(barFill);
        getChildren().add(barBorder);
    }

    public void set(int val, int maxVal, boolean isAlive) {
        double volWid = val * wid / (double) maxVal;
        if (isAlive) {
            t = new Thread(() -> {

                barFill.setX(0);
                barFill.setWidth(volWid);
                volValue = volWid;
            });
            t.start();
        } else {
            if (t != null) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            barFill.setX(0);
            barFill.setWidth(volWid);
            volValue = volWid;
        }
    }
}