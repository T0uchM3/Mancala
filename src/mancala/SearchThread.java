package mancala;

import java.net.InetAddress;

public class SearchThread
{
	public boolean halt = false;

	public void startSearch(String newIp)
	{
		Thread t1 = new Thread()
		{

			public void run()
			{
				for (int i = 1; i <= 36; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.availableIps.add(addrs.toString());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t1.start();

		Thread t2 = new Thread()
		{

			public void run()
			{
				for (int i = 37; i <= 72; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t2.start();

		Thread t3 = new Thread()
		{

			public void run()
			{
				for (int i = 73; i <= 108; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t3.start();
		Thread t4 = new Thread()
		{

			public void run()
			{
				for (int i = 109; i <= 144; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t4.start();
		Thread t5 = new Thread()
		{

			public void run()
			{
				for (int i = 145; i <= 180; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t5.start();
		Thread t6 = new Thread()
		{

			public void run()
			{
				for (int i = 181; i <= 216; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t6.start();
		Thread t7 = new Thread()
		{

			public void run()
			{
				for (int i = 217; i <= 254; i++)
				{
					try
					{
						System.out.println(InetAddress.getByName(newIp + new Integer(i).toString()));
						InetAddress addrs = InetAddress.getByName(newIp + new Integer(i).toString());
						ConnectionWindow.updateStatus();
						if (addrs.isReachable(1000))
						{
							ConnectionWindow.availableIps.add(addrs.toString());
							System.out.println("Available: " + addrs.getHostAddress());
							ConnectionWindow.findPlayer(addrs.toString());
//							break;
						}
						if (halt)
							break;
					} catch (Exception e)
					{
						// TODO: handle exception
					}

				}
			}
		};
		t7.start();
	}

}
