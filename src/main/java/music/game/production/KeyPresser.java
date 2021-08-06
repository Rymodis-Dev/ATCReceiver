package music.game.production;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyPresser
{
    public static void PressCorrespondingKey(int panelNum) throws Exception
    {
        var robot = new Robot();

        switch (panelNum)
        {
            case 1:
                robot.keyPress(KeyEvent.VK_D);
                break;

            case 2:
                robot.keyPress(KeyEvent.VK_F);
                break;

            case 3:
                robot.keyPress(KeyEvent.VK_G);
                break;

            case 4:
                robot.keyPress(KeyEvent.VK_H);
                break;

            case 5:
                robot.keyPress(KeyEvent.VK_J);
                break;

            case 6:
                robot.keyPress(KeyEvent.VK_K);
                break;

            default:
                break;
        }
    }
}
