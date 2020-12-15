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
import java.net.UnknownHostException;
import java.util.ArrayList;

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
	boolean stop = false;
	static SearchThread st;
	boolean offline = false;
	static boolean start = false;
	static DefaultListModel<String> listModel = new DefaultListModel<String>();

	static boolean searching = false;
	private JTextField ipAddressTF;
	static JLabel statusLab;
	static JButton searchConfirmBtn;
	JList<String> list;
	boolean interruptSearch = false;

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
			listModel.clear();

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

	static void findPlayer(String avIP)// this get triggered everytime the threads find an available IP
	{
		try
		{
			String fixedIp = avIP.substring(1, avIP.length());
			updateList(fixedIp);
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
		{// changing the last "tick" to become "100%" instead of "100.%"
			statusLab.setText(s.substring(0, 3) + "%");
			searching = false;
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

	static void foundPlayer(String playerIp)
	{
		System.out.println("found player");
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

	void searchBtnEvent()
	{
		if (!specificIP)
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
					stop = false;
					if (!specificIP)
					{
						searchConfirmBtn.setText("Search");
					} else
					{
						searchConfirmBtn.setText("Connect");
						interruptSearch = true;
					}

				}
			} else// a player has found us, the search button change to "start" to goto the game
			{
				TesT2.foundPlayer();
			}
		} else
		{
			interruptSearch = false;
			specificIP = false;
			try
			{
				DatagramSocket ds = new DatagramSocket();
				String str = "hinllo wurld";
				System.out.println("ADDRESS BACK");
				InetAddress ia = InetAddress.getByName(ipAddressTF.getText());

				DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
				ds.send(dp);
				ds.close();
			} catch (Exception e)
			{
				// TODO: handle exception
			}

		}

	}

	void interceptEvent(ItemEvent e)
	{

		// TODO Auto-generated method stub
		if (e.getStateChange() == 1)
		{

		}

		else
		{

		}

	}

	static void updateList(String ip)
	{
		listModel.addElement(ip);
	}

	void listSelectionEvent(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{

			ipAddressTF.setText(list.getSelectedValue());
			specificIP = true;
		}
	}

	DocumentListener dl = new DocumentListener()
	{

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			if (stop)
				return;
			if (ipAddressTF.getText().length() > 0)
			{
				searchConfirmBtn.setText("Connect");
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
				searchConfirmBtn.setText("Connect");
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
				searchConfirmBtn.setText("Connect");
				specificIP = true;
			}

			else
			{
				searchConfirmBtn.setText("Search");
				specificIP = false;
			}
		}
	};

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

		JCheckBox intercept = new JCheckBox("Intercept ");
		intercept.setToolTipText(
				"(Used mainly for debugging) it control whether the program will be able to intercept incoming netwok packets");
		intercept.setSelected(true);
		intercept.setPreferredSize(new Dimension(90, 15));
		panel_2.add(intercept);
		intercept.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent e)
			{

			}
		});

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));

		JLabel placeHolder = new JLabel("   ");
		panel_1.add(placeHolder);

		ipAddressTF = new JTextField();
		ipAddressTF.setToolTipText("Local ip address, DELETE it if you want to search for other IP address");
		ipAddressTF.setPreferredSize(new Dimension(120, 40));
		ipAddressTF.setMargin(new Insets(2, 10, 2, 10));
		ipAddressTF.setColumns(10);
		panel_1.add(ipAddressTF);
		ipAddressTF.getDocument().addDocumentListener(dl);
		try
		{
			ipAddressTF.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
