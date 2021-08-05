package music.game.production;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    private static final String red    = "\u001b[00;31m";
    private static final String green  = "\u001b[00;32m";
    private static final String yellow = "\u001b[00;33m";
    private static final String pink   = "\u001b[00;35m";
    private static final String cyan   = "\u001b[00;36m";
    private static final String end    = "\u001b[00m";

    public enum Header
    {
        INFO,
        ERROR,
        GET,
        SEND,
        MESSAGE
    }

    public static void SendLog(Header header, String message)
    {
        var colorStr = "";
        var endStr = end;

        if (header == Header.INFO) colorStr = cyan;
        else if (header == Header.ERROR) colorStr = red;
        else if (header == Header.GET) colorStr = yellow;
        else if (header == Header.SEND) colorStr = pink;
        else if (header == Header.MESSAGE) colorStr = green;
        else endStr = "";

        var now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(colorStr + "[" + now + "][" + header + "] - " + message + endStr);
    }
}
