package ui;

import java.awt.*;

//Image display for schedule summary
public class EnoughFreeTimeDisplay extends Canvas {

    //EFFECTS: displays image
    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image myImage = t.getImage("./data/kalm.png");
        g.drawImage(myImage, 120, 100, this);
    }
}
