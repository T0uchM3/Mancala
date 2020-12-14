package mancala;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class NetThread implements Runnable
{

	@Override
	public void run()
	{
		try
		{
			System.out.println("Thread Started");
			DatagramSocket ds = new DatagramSocket(3000);// no parameter means it take an available port
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String strRecv = new String(dp.getData(), 0, dp.getLength()) + " from " + dp.getAddress().getHostAddress()
					+ ":" + dp.getPort();
			TesT2.updateValueNet(strRecv);
			ConnectionWindow.foundPlayer(dp.getAddress().toString());
//			System.out.println(strRecv);
			ds.close();
		} catch (Exception ex)
		{

		}
		// TODO Auto-generated method stub

	}

}
