package battle.view;

import battle.conf.Configs;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewTools {

    static double xOffset;
    static double yOffset;

    public static void setImgFit(ImageView imgV, double x, double y) {
        imgV.setFitWidth(x);
        imgV.setFitHeight(y);
    }

    public static void setImgLayout(ImageView imgV, double x, double y) {
        imgV.setLayoutX(x);
        imgV.setLayoutY(y);
    }

    public static void setLabelPara(Label l, double mx, double my, double lx, double ly) {
        l.setMaxSize(mx, my);
        l.setLayoutX(lx);
        l.setLayoutY(ly);
    }

    public static void setLabelPara(Label l, double lx, double ly) {
        l.setLayoutX(lx);
        l.setLayoutY(ly);
    }

    public static void addPaneChildren(Pane p, Node sc) {
        p.getChildren().add(sc);
    }

    public static void setImgXY(ImageView imgV, double x, double y) {
        imgV.setX(x);
        imgV.setY(y);
    }

    public static void setPaneLayout(Pane p, double x, double y) {
        p.setLayoutX(x);
        p.setLayoutY(y);
    }

    public static void mousePressAndDrag(Stage s, Pane p) {
        p.setBackground(null);
        p.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        p.setOnMouseDragged((MouseEvent e) -> {
            s.setX(e.getScreenX() - xOffset);
            s.setY(e.getScreenY() - yOffset);
        });
    }

    public static void showScene(Stage s, Pane p) {
        Scene scene = new Scene(p, Configs.WINDOWS_WID, Configs.WINDOWS_HGT);
        scene.setFill(null);
        s.setScene(scene);
        s.initStyle(StageStyle.TRANSPARENT);
        s.show();

    }

    public static void setNodeHighLight(Label l, ImageView imV, Configs.IMG_INDEX imi, Configs.IMG_INDEX imih) {
        l.setOnMouseEntered((MouseEvent e) -> {
            imV.setImage(Configs.sysImgs.get(imih.ordinal()));
        });

        l.setOnMouseExited((MouseEvent e) -> {
            imV.setImage(Configs.sysImgs.get(imi.ordinal()));
        });
    }
}
