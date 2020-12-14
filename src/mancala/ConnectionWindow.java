package mancala;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ConnectionWindow extends JFrame
{

	static ConnectionWindow frame;
	private JPanel contentPane;
	private JTextField ipAdressTF;
	boolean specificIP = false;
	static JLabel statusLab;
	static float counter = 0;
	static ArrayList<String> availableIps = new ArrayList<>();
	boolean stop = false;
	static JButton searchConfirmBtn;
	static SearchThread st;
	boolean offline = false;
	static boolean start = false;

	static boolean searching = false;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args)
//	{
//		EventQueue.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				try
//				{
//					frame = new ConnectionWindow();
//					frame.setVisible(true);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	void search()
	{
		try
		{
			// don't continue if not connected to wifi
			if (InetAddress.getLocalHost().getHostAddress().equals("127.0.0.1"))
			{
				System.out.println("exit " + InetAddress.getLocalHost().getHostAddress());
				statusLab.setText("You're offline, try again!");
				stop = false;
				searchConfirmBtn.setText("Search");
				return;
			}
			// reseting stuff
			availableIps.clear();
			counter = 0;
			statusLab.setText("waiting...");
			searching = true;

			// getting the local IP address
			String ip = InetAddress.getLocalHost().getHostAddress();
			String newIp = new String();
			// changing the address to xxx.xxx.xxx.
			for (int i = ip.length(); i > 0; i--)
			{
				if (ip.charAt(i - 1) == '.')
				{
					newIp = ip.substring(0, i);
					break;
				}
			}
			st = new SearchThread();
			st.startSearch(newIp);// containing 7 threads to speed up the search (too lazy to add more)

		} catch (Exception ex)
		{
			// TODO: handle exception
		}
	}

	static void findPlayer(String avIP)
	{
		try
		{
			String fixedIp = avIP.substring(1, avIP.length());
			if (!fixedIp.equals(InetAddress.getLocalHost().getHostAddress()))// don't go in if it's our IP
			{
				DatagramSocket ds = new DatagramSocket();
				String str = "helloooooooooooo world";
				// a wild slash appeared in the ip string

				System.out.println(InetAddress.getLocalHost().getHostAddress() + " INSIDEEEEEEEEE " + fixedIp);

				InetAddress ia = InetAddress.getByName(fixedIp);

				DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
				ds.send(dp);
				ds.close();

			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	static void updateStatus()
	{
		counter += 1;
		String s = ((counter / 254) * 100) + "";
		if (counter != 254)
			statusLab.setText(s.substring(0, 4) + "%");
		else
		{
			statusLab.setText(s.substring(0, 3) + "%");
			for (int i = 0; i < availableIps.size(); i++)
			{
				System.out.println("found::: " + availableIps.get(i));
				searchConfirmBtn.setText("Search");
				searching = false;
			}
		}
	}

	static void foundPlayer(String playerIp)
	{
		if (searching)
		{
			st.halt = true;
			try
			{
				DatagramSocket ds = new DatagramSocket();
				String str = "hello baaaaaaack";
				// a wild slash appeared in the ip string
//				String fixedIp = avIP.substring(1, avIP.length());
				System.out.println("ADDRESS BACK");
				InetAddress ia = InetAddress.getByName(playerIp);

				DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
				ds.send(dp);
				ds.close();
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		}

		statusLab.setText("Found a player!");
		searchConfirmBtn.setText("Start");
		start = true;

	}

	/**
	 * Create the frame.
	 */
	public ConnectionWindow()
	{
		initialize();
	}

	private void initialize()
	{
		// start the receiving thread
		Thread nThread = new Thread(new NetThread());
		nThread.start();
		// **************************

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 195);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Search for a player");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(20);
		panel_1.setPreferredSize(new Dimension(200, 10));
		contentPane.add(panel_1);

		statusLab = new JLabel("Waiting...");
		panel_1.add(statusLab);

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_4.getLayout();
		flowLayout_3.setHgap(3);
		flowLayout_3.setVgap(7);
		panel.add(panel_4);

		JCheckBox specificIPCB = new JCheckBox("Specific IP");
		specificIPCB.setPreferredSize(new Dimension(90, 15));
		panel_4.add(specificIPCB);

		JCheckBox offlineCB = new JCheckBox("Offline");
		offlineCB.setPreferredSize(new Dimension(90, 15));
		panel_4.add(offlineCB);
		offlineCB.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
//						findPlayer();
			}
		});
		specificIPCB.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// TODO Auto-generated method stub
				if (e.getStateChange() == 1)
				{
					ipAdressTF.setEnabled(true);
					specificIP = true;
				}

				else
				{
					ipAdressTF.setEnabled(false);
					specificIP = false;
				}

			}
		});
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setVgap(13);
		panel.add(panel_2);

		searchConfirmBtn = new JButton("Search");
		panel_2.add(searchConfirmBtn);
		searchConfirmBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!start)
				{
					if (!stop)
					{// order really matters in the 3 lines bellow
						stop = true;
						searchConfirmBtn.setText("Stop");
						search();
					}

					else
					{
						st.halt = true;
						searchConfirmBtn.setText("Search");
						stop = false;
					}
				} else
				{
					TesT2.foundPlayer();
				}

			}
		});

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setVgap(13);
		flowLayout_2.setHgap(0);
		panel.add(panel_3);

		ipAdressTF = new JTextField();
		ipAdressTF.setEnabled(false);
		ipAdressTF.setMargin(new Insets(2, 10, 2, 10));
		ipAdressTF.setPreferredSize(new Dimension(20, 25));
		ipAdressTF.setColumns(10);
		panel_3.add(ipAdressTF);

		JCheckBox checkBox = new JCheckBox("New check box");
		panel_3.add(checkBox);
	}
}
