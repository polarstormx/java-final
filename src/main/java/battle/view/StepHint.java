package battle.view;

import battle.conf.Configs;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StepHint extends Pane {
    Text text;
    private boolean isDarkSide;

    public StepHint() {

        text = new Text("葫芦娃阵营剩余步数：3");
        text.setFont(new Font(32));
        text.setLayoutX(Configs.HINT_R);
        text.setLayoutY(Configs.HINT_R);
        getChildren().add(text);
    }

    public void setSide(boolean sd) {
        this.isDarkSide = sd;
    }

    public void setVal(int value, boolean sd) {

        setSide(sd);
        Platform.runLater(() -> {


            if (isDarkSide) {
                text.setText("蛇精阵营剩余步数：" + value);
            } else {
                text.setText("葫芦娃阵营剩余步数：" + value);
            }
        });
    }
}