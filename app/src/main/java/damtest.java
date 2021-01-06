import android.util.Log;

import net.wimpi.modbus.util.BitVector;

public class damtest {
	private static final String TAG = "FFFFFFFFFFFFFFFFFF";
public static void main(String[] args) throws Exception{
		Test2();
    }
	public static void Test2() throws Exception
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				JYDAMEquip equip = new JYDAMEquip();

				//please input the real number of the channel
				int MaxDONum = 8;//�̵�������
				//��ʼ���豸
				equip.Init("192.168.10.1", 10000, 254); //ip �˿�Ϊ10000   254Ϊ�豸�Ĺ㲥��ַ
				equip.BeginConnect();

				int cnt=0;
				while(true)
				{
					if(MaxDONum>0)
					{
//						cnt++;
//						if((cnt&0x01)==0x01)
						{
							Log.d(TAG,"�򿪵�һ·DO");
							equip.writeSignalDO(1, 1);//
						}
//						else
//						{
//							Log.d(TAG,"�رյ�һ·DO");
//							equip.writeSignalDO(8, 0);//
//						}
					}

					//��ȡDO �̵���
//					BitVector DOVal=null;
//					if(MaxDONum>0)DOVal= equip.readSignalDO(MaxDONum);
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					int a = equip.readSignalDO(0);
					


//					if(MaxDONum>0)PrintfDODIVal("DO Status:",DOVal,MaxDONum);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//ע�⣬���Ե�ʱ�� ���ܳ�������ʧ�ܡ����ֶ�ֹ֮ͣǰ�ĵ��ԡ�
				//equip.DisConnect();
			}
		}).start();

	}
	
	public static void PrintfDODIVal(String prev,BitVector val,int size)
	{
		Log.d(TAG,prev);
		if(val==null)return;
		for(int i=0;i<size;i++)
			Log.d(TAG,"  "+(val.getBit(i)?"1":"0"));
		Log.d(TAG,"");
		
	}
}
