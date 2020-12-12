package mancala;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Test3
{

	// Specified server port
	public static int serverPort = 998;

	// Specified client port
	public static int clientPort = 999;

	public static int buffer_size = 1024;
	public static DatagramSocket ds;

	// an array of buffer_size
	public static byte buffer[] = new byte[buffer_size];

	// Function for server
	public static void TheServer() throws Exception
	{
		int pos = 0;
		while (true)
		{
			int c = System.in.read();
			switch (c)
			{
			case -1:

				// -1 is given then server quits and returns
				System.out.println("Server Quits.");
				return;
			case '\r':
				break; // loop broken
			case '\n':
				// send the data to client
				ds.send(new DatagramPacket(buffer, pos, InetAddress.getLocalHost(), clientPort));
				pos = 0;
				break;
			default:
				// otherwise put the input in buffer array
				buffer[pos++] = (byte) c;
			}
		}
	}

	// Function for client
	public static void TheClient() throws Exception
	{
		while (true)
		{

			// first one is array and later is its size
			DatagramPacket p = new DatagramPacket(buffer, buffer.length);

			ds.receive(p);

			// printing the data which has been sent by the server
			System.out.println(new String(p.getData(), 0, p.getLength()));
		}
	}

	// main driver function
	public static void main(String args[]) throws Exception
	{

		// if WriteServer 1 passed then this will run the server function
		// otherwise client function will run
		if (args.length == 1)
		{
			ds = new DatagramSocket(serverPort);
			TheServer();
		} else
		{
			ds = new DatagramSocket(clientPort);
			TheClient();
		}
	}
}
