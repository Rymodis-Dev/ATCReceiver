package music.game.production;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialReceiver
{
    private SerialPort CommSerialPort = null;

    public void Run(SerialPort port, int baudRate)
    {
        this.CommSerialPort = port;

        if (this.CommSerialPort == null)
        {
            Logger.SendLog(Logger.Header.ERROR, "Failed to set serial port");
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
                    ReadSerialConnection();
            }
        });

        this.CommSerialPort.setBaudRate(baudRate);
        this.CommSerialPort.setNumDataBits(8);
        this.CommSerialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        this.CommSerialPort.setParity(SerialPort.NO_PARITY);
        this.CommSerialPort.openPort();
        this.CommSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

        Logger.SendLog(Logger.Header.MESSAGE, "Serial comm is running.");
        Logger.SendLog(Logger.Header.INFO,
                "PortName: " + this.CommSerialPort.getSystemPortName() +
                        ", BaudRate: " + this.CommSerialPort.getBaudRate() +
                        ", NumDataBits: " + this.CommSerialPort.getNumDataBits() +
                        ", Parity: " + this.CommSerialPort.getParity());

        while (true){}
    }

    private void ReadSerialConnection()
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
                Logger.SendLog(Logger.Header.GET, "Tapped panel number: " + panelNum);
                KeyPresser.PressCorrespondingKey(panelNum);
            }
        }
        catch (Exception ex)
        {
            Logger.SendLog(Logger.Header.ERROR, ex.toString());
        }
    }

    public void Close()
    {
        if (this.CommSerialPort != null &&
            this.CommSerialPort.isOpen())
        {
            this.CommSerialPort.removeDataListener();
            this.CommSerialPort.closePort();
            Logger.SendLog(Logger.Header.MESSAGE, "Serial comm is closed.");
        }
    }

    public SerialPort[] GetSerialPorts()
    {
        return SerialPort.getCommPorts();
    }
}
