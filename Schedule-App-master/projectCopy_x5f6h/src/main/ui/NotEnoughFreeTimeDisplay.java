package ui;


import java.awt.*;

//Image shown to user from schedule summary data
public class NotEnoughFreeTimeDisplay extends Canvas {

    //EFFECTS: displays image
    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image myImage = t.getImage("./data/pic.png");
        g.drawImage(myImage, 120, 100, this);
    }

}