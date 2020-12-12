package TesTPack;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Recieve
{
	public static void main(String[] args) throws Exception
	{
		DatagramSocket ds = new DatagramSocket(3000);// no parameter means it take an available port
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, 1024);
		ds.receive(dp);
		String strRecv = new String(dp.getData(), 0, dp.getLength()) + " from " + dp.getAddress().getHostAddress() + ":"
				+ dp.getPort();
		System.out.println(strRecv);
		ds.close();
	}
}
