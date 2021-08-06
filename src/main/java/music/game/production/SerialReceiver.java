package music.game.production;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.ByteBuffer;

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
        this.CommSerialPort.addDataListener(new SerialPortDataListener()
        {
            @Override
            public int getListeningEvents()
            {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event)
            {
                try
                {
                    int evt = event.getEventType();

                    if (evt == SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    {
                        int byteToRead = CommSerialPort.bytesAvailable();

                        if (byteToRead == -1)
                        {
                            Close();
                            Logger.SendLog(Logger.Header.ERROR, "Port is closed");
                            return;
                        }

                        var data = new byte[byteToRead];
                        CommSerialPort.readBytes(data, byteToRead);
                        var result = ByteBuffer.wrap(data).getInt();

                        Logger.SendLog(Logger.Header.GET, "Get panel ID: " + result);
                    }
                }
                catch (Exception e)
                {
                    Logger.SendLog(Logger.Header.ERROR, e.getMessage());
                    Close();
                }


                var data = event.getReceivedData();
                int result = -1;

                try
                {
                    result = ByteBuffer.wrap(data).getInt();
                }
                catch (Exception e)
                {
                    Logger.SendLog(Logger.Header.ERROR, e.getMessage());
                }

                if (result != -1)
                {
                    Logger.SendLog(Logger.Header.GET, "Get \"" + result + "\"");
                    // send keys
                }
            }
        });

        this.CommSerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        this.CommSerialPort.openPort();

        Logger.SendLog(Logger.Header.MESSAGE, "Serial comm is running.");
        Logger.SendLog(Logger.Header.INFO,
                "PortName: " + this.CommSerialPort.getSystemPortName() +
                        ", BaudRate: " + this.CommSerialPort.getBaudRate() +
                        ", NumDataBits: " + this.CommSerialPort.getNumDataBits() +
                        ", Parity: " + this.CommSerialPort.getParity());
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
