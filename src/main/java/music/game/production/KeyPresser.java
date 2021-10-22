package music.game.production;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyPresser
{
    public static void pressCorrespondingKey(int panelNum) throws Exception
    {
        var robot = new Robot();

        switch (panelNum)
        {
            case 1:
                robot.keyPress(KeyEvent.VK_D);
                robot.keyRelease(KeyEvent.VK_D);
                break;

            case 2:
                robot.keyPress(KeyEvent.VK_F);
                robot.keyRelease(KeyEvent.VK_F);
                break;

            case 3:
                robot.keyPress(KeyEvent.VK_G);
                robot.keyRelease(KeyEvent.VK_G);
                break;

            case 4:
                robot.keyPress(KeyEvent.VK_H);
                robot.keyRelease(KeyEvent.VK_H);
                break;

            case 5:
                robot.keyPress(KeyEvent.VK_J);
                robot.keyRelease(KeyEvent.VK_J);
                break;

            case 6:
                robot.keyPress(KeyEvent.VK_K);
                robot.keyRelease(KeyEvent.VK_K);
                break;

            default:
                break;
        }
    }
}
