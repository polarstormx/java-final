package battle;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.image.Image;

public class Bullet{
    public AtomicBoolean isVisible = new AtomicBoolean(false);
    public AtomicInteger posX = new AtomicInteger(0);
    public AtomicInteger posY = new AtomicInteger(0);
    public Image bulletImg;
    public Bullet(Image i){
        bulletImg = i;
    }
}