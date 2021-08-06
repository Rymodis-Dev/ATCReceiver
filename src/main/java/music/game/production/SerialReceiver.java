package music.game.production;

import com.fazecast.jSerialComm.SerialPort;

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

        ReadNoneBlocking();
    }

    private void ReadNoneBlocking()
    {
        try
        {
            while (true)
            {
                var bytesAvailable = this.CommSerialPort.bytesAvailable();

                if (bytesAvailable < 1)
                {
                    //Logger.SendLog(Logger.Header.INFO, "Data not received.");
                    continue;
                }

                var buffer = new byte[bytesAvailable];
                var numRead = this.CommSerialPort.readBytes(buffer, buffer.length);
                Logger.SendLog(Logger.Header.GET, "Get "+ numRead + "bytes");
            }
        }
        catch (Exception e)
        {
            Logger.SendLog(Logger.Header.ERROR, e.toString());
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
