package music.game.production;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialReceiver
{
    private SerialPort CommSerialPort = null;
    private int PanelNoLastTime = 0;

    public void run(SerialPort port, int baudRate)
    {
        this.CommSerialPort = port;

        if (this.CommSerialPort == null)
        {
            Logger.sendLog(Logger.Header.ERROR, "Failed to set serial port");
            return;
        }

        this.CommSerialPort.addDataListener(new SerialPortDataListener()
        {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    readSerialConnection();
            }
        });

        this.CommSerialPort.setBaudRate(baudRate);
        this.CommSerialPort.setNumDataBits(8);
        this.CommSerialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        this.CommSerialPort.setParity(SerialPort.NO_PARITY);
        this.CommSerialPort.openPort();
        this.CommSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

        Logger.sendLog(Logger.Header.MESSAGE, "Serial comm is running.");
        Logger.sendLog(Logger.Header.INFO,
                "PortName: " + this.CommSerialPort.getSystemPortName() +
                        ", BaudRate: " + this.CommSerialPort.getBaudRate() +
                        ", NumDataBits: " + this.CommSerialPort.getNumDataBits() +
                        ", Parity: " + this.CommSerialPort.getParity());

        while (true){}
    }

    private void readSerialConnection()
    {
        try
        {
            var bytesAvailable = this.CommSerialPort.bytesAvailable();

            if (bytesAvailable < 1)
                return;

            var buffer = new byte[5];
            this.CommSerialPort.readBytes(buffer, Math.min(buffer.length, bytesAvailable));
            var resultStr = new String(buffer, 0, 1);

            if (!resultStr.equals("\n") && !resultStr.equals("\r"))
            {
                var panelNum = Integer.parseInt(resultStr);

                if (panelNum == this.PanelNoLastTime)
                {
                    // wait 0.095s
                    Thread.sleep(95);
                    this.PanelNoLastTime = 0;
                    return;
                }

                Logger.sendLog(Logger.Header.GET, "Tapped panel number: " + panelNum);

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try { KeyPresser.pressCorrespondingKey(panelNum); }
                        catch (Exception e) { Logger.sendLog(Logger.Header.ERROR, e.getMessage()); }
                    }
                }).start();

                this.PanelNoLastTime = panelNum;
            }
        }
        catch (Exception ex)
        {
            Logger.sendLog(Logger.Header.ERROR, ex.toString());
        }
    }

    public void close()
    {
        if (this.CommSerialPort != null &&
            this.CommSerialPort.isOpen())
        {
            this.CommSerialPort.removeDataListener();
            this.CommSerialPort.closePort();
            Logger.sendLog(Logger.Header.MESSAGE, "Serial comm is closed.");
        }
    }

    public SerialPort[] getSerialPorts()
    {
        return SerialPort.getCommPorts();
    }
}
