package music.game.production;

public class App
{
    public static void main(String[] args)
    {
        Logger.SendLog(Logger.Header.INFO, "Hello World!!");
        Logger.SendLog(Logger.Header.ERROR, "Error!!");
        Logger.SendLog(Logger.Header.GET, "Get!!");
        Logger.SendLog(Logger.Header.SEND, "Send!!");
        Logger.SendLog(Logger.Header.MESSAGE, "Message!!");
    }
}
