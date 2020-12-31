package mancala;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.SwingUtilities;

public class NetThread implements Runnable
{
	boolean fromCore = false;
	public static String command;

	public NetThread(boolean source)
	{
		this.fromCore = source;
	}

	@Override
	public void run()
	{
		try
		{
			System.out.println("NetThread Started");
			DatagramSocket ds = new DatagramSocket(3000);// no parameter means it take an available port
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			System.out.println("Recieved");
			String strRecv = new String(dp.getData(), 0, dp.getLength()) + " from " + dp.getAddress().getHostAddress()
					+ ":" + dp.getPort();
			String msg = new String(dp.getData(), 0, dp.getLength());
//			TesT2.updateValueNet(strRecv);
			if (!fromCore)
			{

				SwingUtilities.invokeLater(new Runnable()// running stuff on main thread!
				{

					@Override
					public void run()
					{
						try
						{
							ConnectionWindow.foundPlayer(dp.getAddress().toString(), msg);
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}

			else
			{
				SwingUtilities.invokeLater(new Runnable()// running stuff on main thread!
				{

					@Override
					public void run()
					{
						try
						{
							Core.translate(msg);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				});

			}

			System.out.println(strRecv);
			ds.close();
		} catch (Exception ex)
		{

		}
		// TODO Auto-generated method stub

	}

}
