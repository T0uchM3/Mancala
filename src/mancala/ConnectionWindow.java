package mancala;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ConnectionWindow extends JFrame
{

	static ConnectionWindow frame;
	private JPanel contentPane;
	static boolean specificIP = false;
	static float counter = 0;
	static ArrayList<String> availableIps = new ArrayList<>();
	static boolean stop = false;
	static SearchThread st;
	boolean offline = false;
	static boolean start = false;
	static DefaultListModel<String> listModel = new DefaultListModel<String>();

	static boolean searching = false;
	private static JTextField ipAddressTF;
	static JLabel statusLab;
	static JButton searchConfirmBtn;
	JList<String> list;
	boolean interruptSearch = false;
	Thread nThread;
	static boolean invited = false;
	static String otherPlayerName;
	static String localIp;
	static boolean goingFirst = false;
	static String destinationIp;

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
			// don't continue if not connected to network
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
			listModel.clear();

			// getting the local IP address
			localIp = InetAddress.getLocalHost().getHostAddress();
			String newIp = new String();
			// changing the address to xxx.xxx.xxx.
			for (int i = localIp.length(); i > 0; i--)
			{
				if (localIp.charAt(i - 1) == '.')
				{
					newIp = localIp.substring(0, i);
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

	static void findPlayer(String avIP)// this get triggered everytime the threads find an available IP
	{
		try
		{
			String fixedIp = avIP.substring(1, avIP.length());
			if (fixedIp.equals(localIp))
				fixedIp = fixedIp + "      YOU";
			updateList(fixedIp);
			if (!fixedIp.equals(InetAddress.getLocalHost().getHostAddress()))// don't go in if it's our IP
			{
//				DatagramSocket ds = new DatagramSocket();
//				String str = "helloooooooooooo world";
//				// a wild slash appeared in the ip string
//
//				System.out.println(InetAddress.getLocalHost().getHostAddress() + " INSIDEEEEEEEEE " + fixedIp);
//
//				InetAddress ia = InetAddress.getByName(fixedIp);
//
//				DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
//				ds.send(dp);
//				ds.close();
			}

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

	}

//this for generating the percentage and stuff 
	static void updateStatus()
	{
		counter += 1;
		String s = ((counter / 254) * 100) + "";
		if (counter != 254)
			statusLab.setText(s.substring(0, 4) + "%");
		else
		{// changing the last "tick" to become "100%" instead of "100.%"
			statusLab.setText(s.substring(0, 3) + "%");
			searching = false;
			stop = false;
			if (!specificIP)
				searchConfirmBtn.setText("Search");
			else
				searchConfirmBtn.setText("Connect");
			for (int i = 0; i < availableIps.size(); i++)
			{
				System.out.println("found::: " + availableIps.get(i));

			}
		}
	}

	public static void wait(int ms)
	{
		try
		{
			Thread.sleep(ms);
		} catch (InterruptedException ex)
		{
			Thread.currentThread().interrupt();
		}
	}

//NetThread start this, when it get an invite from other player 
	static void foundPlayer(String playerIp, String message)
	{
		// just removing the slash at the start (weird stuff)
		destinationIp = playerIp.substring(1, playerIp.length());

		System.out.println("found player");
//		if (searching)
//		{
//			st.halt = true;
		searchConfirmBtn.setEnabled(true);

		if (message.substring(0, 1).equals("*"))
		{
			statusLab.setText(message.substring(1, message.length()) + " accepted!");
			searchConfirmBtn.setText("Start");
			otherPlayerName = message.substring(1, message.length());
		}

		else
		{
			statusLab.setText(message + " invited you!");
			searchConfirmBtn.setText("Accept");
			otherPlayerName = message;
			goingFirst = true;
		}

		invited = true;
		start = true;
		try
		{
			localIp = InetAddress.getLocalHost().getHostAddress();
//			wait(1000);
//			DatagramSocket ds = new DatagramSocket();
//			String str = "hello baaaaaaack";
//			// a wild slash appeared in the ip string
//			String fixedIp = playerIp.substring(1, playerIp.length());
//
//			System.out.println("returning it to " + fixedIp);
//			InetAddress ia = InetAddress.getByName(fixedIp);
//
//			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
//			ds.send(dp);
//			ds.close();

		} catch (Exception e)
		{
			// TODO: handle exception
		}

//		}
	}

	void sendInv()
	{
		try
		{

			DatagramSocket ds = new DatagramSocket();
			String str;
			if (invited)
			{
				str = "*" + playerNameTF.getText();
			} else
			{
				str = playerNameTF.getText();

			}
			System.out.println("GOING");
			InetAddress ia = InetAddress.getByName(ipAddressTF.getText());

			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
			ds.send(dp);
			ds.close();
			nThread.start();

//			invited = false;
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	void searchBtnEvent()
	{

		if (invited)
		{
//			sendInv();
			System.out.println("c");
			if (goingFirst)
				sendInv();
			Core.foundPlayer(playerNameTF.getText(), otherPlayerName, goingFirst, localIp, destinationIp);
			if (!goingFirst)// no reason to move further that this while going in 2nd
				return;

			// we keep going (bellow) to send back a message to who invited us to inform
			// them that we're good
		}

		if (!specificIP)
		{
			if (!start)// "start" is true when NetThread catch something
			{
				if (!stop)// when the button text = "Search"
				{// order really matters in the 3 lines bellow
					stop = true;
					searchConfirmBtn.setText("Stop");
					search();
					System.out.println("0");
				} else// when the button text = "stop"
				{
					st.halt = true;// stop the searching threads
					stop = false;
					if (!specificIP)// when ipAdressTF is empty
					{
						System.out.println("1");
						searchConfirmBtn.setText("Search");
					} else// when not
					{
						System.out.println("2");
						searchConfirmBtn.setText("Invite");
//						invite = true;
					}

				}
			} else// this seems unreachable??
					// a player has found us, the search button change to "start" to goto the game
			{
				System.out.println("X");
				Core.foundPlayer(playerNameTF.getText(), otherPlayerName, goingFirst, localIp, destinationIp);
			}
		} else
		{

			if (stop)
			{
				st.halt = true;// stop the searching threads
				stop = false;
				searchConfirmBtn.setText("Invite");

				System.out.println("a");
			} else// this and "if(invited)" will both run when a player receive an invitation, but
					// only this will run when sending the initial invite
			{
				System.out.println("b");

//				if (!start)
				if (!goingFirst)
				{
					sendInv();// they invited us, so we inform them of our decision
					wait(500);// delaying the thread launch make the return invite possible after search
					chainCommands();// run a receiver just after sending an invite
				}

			}

		}

	}

	void chainCommands()
	{
		nThread = new Thread(new NetThread(false));
		nThread.start();
		System.out.println("Starting Thread");

	}

//concern the checkBox
	void interceptEvent(ItemEvent e)
	{

		// TODO Auto-generated method stub
		if (e.getStateChange() == 1)
		{
			searchConfirmBtn.setEnabled(false);
			nThread = new Thread(new NetThread(false));
			nThread.start();
			System.out.println("Starting Thread");
		}

		else
		{
			searchConfirmBtn.setEnabled(true);
			nThread.stop();
			System.out.println("Stopping Thread");

		}

	}

	static void updateList(String ip)
	{
		listModel.addElement(ip);
	}

//concerning the GUI ip list
	void listSelectionEvent(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			if (list.getSelectedValue().contains("YOU"))
				ipAddressTF.setText(list.getSelectedValue().substring(0, list.getSelectedValue().length() - 3).trim());
			else
				ipAddressTF.setText(list.getSelectedValue());
			specificIP = true;
			if (!stop)
				searchConfirmBtn.setText("Invite");
		}
	}

//this concerning the ip text field 
	DocumentListener dl = new DocumentListener()
	{

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			if (stop)
				return;
			if (ipAddressTF.getText().length() > 0)
			{
				searchConfirmBtn.setText("Invite");
				specificIP = true;
			}

			else
			{
				searchConfirmBtn.setText("Search");
				specificIP = false;
			}

		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			if (stop)
				return;
			if (ipAddressTF.getText().length() > 0)
			{
				searchConfirmBtn.setText("Invite");
				specificIP = true;
			}

			else
			{
				searchConfirmBtn.setText("Search");
				specificIP = false;
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			if (stop)
				return;
			if (ipAddressTF.getText().length() > 0)
			{
				searchConfirmBtn.setText("Invite");
				specificIP = true;
			}

			else
			{
				searchConfirmBtn.setText("Search");
				specificIP = false;
			}
		}
	};
	private JTextField playerNameTF;

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

//		nThread = new Thread(new NetThread());
//		nThread.start();

		// **************************

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 209);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]
		{ 424, 0 };
		gbl_contentPane.rowHeights = new int[]
		{ 48, 120, 0 };
		gbl_contentPane.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[]
		{ 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Search for a player");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		JSplitPane splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 1;
		contentPane.add(splitPane, gbc_splitPane);

		JPanel panel_4 = new JPanel();
		splitPane.setRightComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		searchConfirmBtn = new JButton("Search");
		searchConfirmBtn.setPreferredSize(new Dimension(65, 25));
		panel_4.add(searchConfirmBtn, BorderLayout.SOUTH);
		searchConfirmBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				searchBtnEvent();
			}
		});

		statusLab = new JLabel("Waiting...");
		statusLab.setPreferredSize(new Dimension(48, 25));
		statusLab.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(statusLab, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel_4.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(3, 1, 10, -10));

		JLabel placeHolder_1 = new JLabel("   ");
		panel_2.add(placeHolder_1);

		JCheckBox intercept = new JCheckBox("Open up^");
		intercept.setToolTipText(
				"(Used mainly for debugging) it control whether the program will be able to intercept incoming netwok packets");
		intercept.setPreferredSize(new Dimension(90, 15));
		panel_2.add(intercept);
		intercept.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{
				interceptEvent(e);
			}
		});

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));

		playerNameTF = new JTextField();
		playerNameTF.setHorizontalAlignment(SwingConstants.CENTER);
		playerNameTF.setText("anon" + ThreadLocalRandom.current().nextInt(0, 68 + 1));
		panel_1.add(playerNameTF);
		playerNameTF.setColumns(10);

		ipAddressTF = new JTextField();
		ipAddressTF.setHorizontalAlignment(SwingConstants.CENTER);
		ipAddressTF.setToolTipText("Local ip address, DELETE it if you want to search for other IP address");
		ipAddressTF.setPreferredSize(new Dimension(120, 40));
		ipAddressTF.setMargin(new Insets(2, 10, 2, 10));
		ipAddressTF.setColumns(10);
		panel_1.add(ipAddressTF);
		ipAddressTF.getDocument().addDocumentListener(dl);
		try
		{
			ipAddressTF.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception ex1)
		{
			// TODO Auto-generated catch block
			ex1.printStackTrace();
		}

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setMinimumSize(new Dimension(150, 23));
		scrollPane_1.setPreferredSize(new Dimension(200, 40));
		splitPane.setLeftComponent(scrollPane_1);

		list = new JList<String>();
		scrollPane_1.setViewportView(list);
		list.setModel(listModel);
		list.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				// TODO Auto-generated method stub
				listSelectionEvent(e);
			}
		});

		JLabel lblNewLabel_1 = new JLabel("Detected IPs");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblNewLabel_1);
	}
}
