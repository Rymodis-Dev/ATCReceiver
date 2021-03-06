package music.game.production;

import java.util.Scanner;

public class App
{
    public static void main(String[] args)
    {
        var scanner = new Scanner(System.in);
        var receiver = new SerialReceiver();
        var ports = receiver.getSerialPorts();
        var portIndex = -1;
        var baudRate = 9600;

        if (ports.length < 1)
        {
            Logger.sendLog(Logger.Header.MESSAGE, "Valid ports was not found.");
            return;
        }

        Logger.sendLog(Logger.Header.MESSAGE, "Please select the port you want to use.");
        for (int i = 0; i < ports.length; i++)
            System.out.println(i + " - " + ports[i].getSystemPortName());

        while (true)
        {
            System.out.print(">");
            try
            {
                portIndex = Integer.parseInt(scanner.nextLine());
            }
            catch (Exception e)
            {
                Logger.sendLog(Logger.Header.ERROR, e.toString());
                continue;
            }

            if (portIndex > -1 && portIndex < ports.length)
                break;

            Logger.sendLog(Logger.Header.ERROR, "This number cannot be selected.");
        }

        Logger.sendLog(Logger.Header.MESSAGE, "Please enter the baud rate. (Default: 9600(bps))");
        System.out.print(">");
        try
        {
            var input = Integer.parseInt(scanner.nextLine());

            if (input > 0)
                baudRate = input;
        }
        catch (Exception ignored){}

        receiver.run(ports[portIndex], baudRate);
        receiver.close();
    }
}
