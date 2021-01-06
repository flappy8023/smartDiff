import java.net.InetAddress;

import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.util.BitVector;


public class JYDAMEquip {

    String ip = "192.168.1.232";
    int port = 10000;
    int slaveID = 254;
    RTUTCPMasterConnection Conn;
    int ErrorCnt = 0;

    public void Init(String _ip, int _port, int _slaveID) {
        ip = _ip;
        port = _port;
        slaveID = _slaveID;
    }

    public boolean BeginConnect() {
        try {
            InetAddress addr = InetAddress.getByName(ip);

            // creat new tcp
            Conn = new RTUTCPMasterConnection(addr, port);
            Conn.setTimeout(3000);
            Conn.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return IsConnect();
    }

    public boolean IsConnect() {
        if (ErrorCnt > 10) {
            ErrorCnt = 0;
            if (Conn == null) return false;
            Conn.close();
            Conn = null;
        }
        if (Conn == null) return false;
        return Conn.isConnected();
    }

    public boolean DisConnect() {
        if (Conn == null) return false;
        Conn.close();
        return IsConnect();
    }

    //read the status  of  do . the count is  the number of do channels
    public BitVector readDO(int count) {
        BitVector data = null;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return data;
            }

            if (count == 0) return data;

            //默认第一个继电器的地�?�?0
            ReadCoilsRequest req = new ReadCoilsRequest(0, count);

            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            trans.execute();

            ReadCoilsResponse res = ((ReadCoilsResponse) trans.getResponse());

            data = res.getCoils();

            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    //read the coils status�?  读取单个继电器状�?
    public int readSignalDO(int regAddr) {
        int data = 0;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return -1;
            }

            ReadCoilsRequest req = new ReadCoilsRequest(regAddr, 1);

            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            trans.execute();

            ReadCoilsResponse res = ((ReadCoilsResponse) trans.getResponse());

            if (res.getCoils().getBit(0)) {
                data = 1;
            }

            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    /**
     * 写入数据到真机的DO类型的寄存器上面
     *
     * @param ip
     * @param port
     * @param slaveId
     * @param address
     * @param value
     */
    public void writeSignalDO(int regAddr, int value) {
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return;
            }

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            WriteCoilRequest req = new WriteCoilRequest(regAddr, value == 0 ? false : true);

            req.setUnitID(slaveID);
            trans.setRequest(req);

            trans.execute();
            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }
    }

    /**
     * 查询Function 为Input Status的寄存器
     * 读取光�?�状�?
     *
     * @param ip
     * @param address
     * @param count
     * @param slaveId
     * @return
     * @throws ModbusIOException
     * @throws ModbusSlaveException
     * @throws ModbusException
     */
    public BitVector readDI(int count) {
        BitVector data = null;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return data;
            }
            if (count == 0) return data;

            // 第一个参数是寄存器的地址，第二个参数时读取多少个
            ReadInputDiscretesRequest req = new ReadInputDiscretesRequest(0, count);

            // 这里设置的Slave Id, 读取的时候这个很重要
            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            // 执行查询
            trans.execute();

            // 得到结果
            ReadInputDiscretesResponse res = (ReadInputDiscretesResponse) trans.getResponse();

            data = res.getDiscretes();

            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    public int readSignalDI(int regAddr) {
        int data = 0;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return -1;
            }

            // 第一个参数是寄存器的地址，第二个参数时读取多少个
            ReadInputDiscretesRequest req = new ReadInputDiscretesRequest(regAddr, 1);

            // 这里设置的Slave Id, 读取的时候这个很重要
            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            // 执行查询
            trans.execute();

            // 得到结果
            ReadInputDiscretesResponse res = (ReadInputDiscretesResponse) trans.getResponse();

            if (res.getDiscretes().getBit(0)) {
                data = 1;
            }

            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    public int[] readAI(int count) {
        int[] data = null;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return data;
            }

            if (count == 0) return data;

            //这里重点说明下，这个地址和数量一定要对应起来
            ReadInputRegistersRequest req = new ReadInputRegistersRequest(0, count);

            //这个SlaveId�?定要正确
            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            trans.execute();

            ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();

            InputRegister[] rst = res.getRegisters();
            if (rst.length != count) return data;
            data = new int[count];
            for (int i = 0; i < count; i++)
                data[i] = rst[i].getValue();

            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    public int readSignalAI(int regAddr) {
        int data = 0;

        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return -1;
            }

            //这里重点说明下，这个地址和数量一定要对应起来
            ReadInputRegistersRequest req = new ReadInputRegistersRequest(regAddr, 1);

            //这个SlaveId�?定要正确
            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            trans.execute();

            ReadInputRegistersResponse res = (ReadInputRegistersResponse) trans.getResponse();

            data = res.getRegisterValue(0);
            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

    public int readRegister(int regAddr) {
        int data = 0;
        try {
            if (IsConnect() == false) {
                if (BeginConnect() == false) return -1;
            }
            ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(regAddr, 1);
            req.setUnitID(slaveID);

            ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(Conn);

            trans.setRequest(req);

            trans.execute();

            ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();

            data = res.getRegisterValue(0);
            ErrorCnt = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorCnt++;
        }

        return data;
    }

}
