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
        else
        {
            Logger.SendLog(Logger.Header.MESSAGE, "Serial comm is running.");
            Logger.SendLog(Logger.Header.INFO, "PortName: " + this.CommSerialPort.getSystemPortName() + ", BaudRate: " + this.CommSerialPort.getBaudRate());
        }

        this.CommSerialPort.setBaudRate(baudRate);
        this.CommSerialPort.openPort();
    }

    public void Close()
    {
        if (this.CommSerialPort != null &&
            this.CommSerialPort.isOpen())
            this.CommSerialPort.closePort();
    }

    public SerialPort[] GetSerialPorts()
    {
        return SerialPort.getCommPorts();
    }
}
