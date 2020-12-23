package mancala;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

public class Core
{

	private static JFrame frame;
	static JLabel midLab;
	JButton btnDrag;
	JLabel firstMarker;
	JLabel secondMarker;
	static JButton startBtn;
	static boolean localPlayerTurn = true;
	static boolean sender = false;
	static ConnectionWindow cw;
	static JButton btnDrop;
	static ArrayList<JButton> btnList = new ArrayList<JButton>();
	static Core window;
	static JLabel playerNameLab;
	static JLabel otherNameLab;
	static boolean inFirst = false;
	static String otherPlayerIp;
	static String localPlayerIp;
	static String otherPlayerName;
	static String localPlayerName;
	static boolean bothePlayersInside = false;
	static Thread nThread;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{

		try
		{

//			InetAddress.getLocalHost();
			openConnectionDialog();// open the connection window
			System.out.println(InetAddress.getLocalHost());
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception ignored)
		{
		}

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window = new Core();
					window.frame.setVisible(false);
					window.frame.setEnabled(false);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	static void sending(String cmd)
	{
		try
		{
			DatagramSocket ds = new DatagramSocket();
			System.out.println("sending " + cmd);
			InetAddress ia = InetAddress.getByName(otherPlayerIp);
			DatagramPacket dp = new DatagramPacket(cmd.getBytes(), cmd.length(), ia, 3000);
			ds.send(dp);
			ds.close();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

//open thread for receiving 
	static void receiving()
	{
		nThread = new Thread(new NetThread(true));
		nThread.start();
	}

	static void translate(String cmd) throws InterruptedException
	{
		String command = cmd.substring(0, 3);
		String context = cmd.substring(3, cmd.length());
		System.out.println("Translating " + command);
		switch (command)
		{
		case "001":// inform 1st player that 2nd player connected
		{
			System.out.println("SWITCH" + cmd);
			bothePlayersInside = true;
			nThread.stop();
			wait(1000);
			sending("002" + localPlayerName);
			System.out.println("thread state " + nThread.getState());
		}
			break;
		case "002":// synchronising the midLab
		{
			System.out.println("COMMAND  " + command);
			System.out.println("CONTEXT  " + context);
			midLab.setText(context + "'s turn");
		}
			break;
		}
	}

	// launching the connection window
	static void openConnectionDialog()
	{
		cw = new ConnectionWindow();

		cw.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		cw.setVisible(true);

		cw.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.out.println("CLOSE");
				System.exit(0);
			}
		});
	}

	// first thing that get called when the game start
	static void foundPlayer(String playerName, String otherPrName, boolean enteredFirst, String locIp, String destIp)
	{
		// this for separating between the old and new logs
		System.out.println("********************************************");
		inFirst = enteredFirst;
		localPlayerIp = locIp;
		otherPlayerIp = destIp;
		localPlayerName = playerName;
		otherPlayerName = otherPrName;
//		System.out.println("local " + locIp + " dest " + otherPlayerIp);

		firstInteraction();

		cw.setVisible(false);
		window.frame.setVisible(true);
		window.frame.setEnabled(true);
		playerNameLab.setText(playerName);
		otherNameLab.setText(otherPlayerName);

	}

	static void firstInteraction()
	{
		if (inFirst)
		{
			System.out.println("First Player In");
			midLab.setText(localPlayerName + "'s turn");
			wait(500);
			receiving();

		} else
		{
			System.out.println("Second Player In");
			sending("001");
			wait(500);
			receiving();
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

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public Core() throws Exception
	{
		initialize();
	}

	static void updateValueNet(String val)
	{
		midLab.setText(val);
		System.out.println(val);
	}

	static void updateValue(String btnDragText, String btnDragName, String btnDropName, String btnDropText)
	{
		for (int i = 0; i < btnList.size(); i++)
		{
			if (btnList.get(i).getName() == btnDragName)
				btnList.get(i).setText(btnDragText);
			if (btnList.get(i).getName() == btnDropName)
				btnList.get(i).setText(btnDropText);
		}
	}

	void turnManagment()
	{

	}

	MouseMotionListener mml = new MouseMotionListener()
	{

		@Override
		public void mouseMoved(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			// TODO Auto-generated method stub
			JComponent jc = (JComponent) e.getSource();
			TransferHandler th = jc.getTransferHandler();
			th.exportAsDrag(jc, e, TransferHandler.COPY);
			for (int i = 0; i < btnList.size(); i++)
			{
				if (jc.getName() == btnList.get(i).getName())
				{
					btnList.get(i).setTransferHandler(new ValueExportTransferHandler(""));
					for (int j = 0; j < btnList.size(); j++)
					{
						if (btnList.get(j).getName() != btnList.get(i).getName())
						{
							btnList.get(j).setTransferHandler(new ValueImportTransferHandler());
						}

					}
				}
			}
		}
	};
	MouseListener ml = new MouseListener()
	{

		@Override
		public void mouseReleased(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e)
		{

			// TODO Auto-generated method stub
			if (e.getModifiersEx() == InputEvent.CTRL_DOWN_MASK)
				System.out.println("IN");
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}
	};

	/**
	 * Initialise the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws Exception
	{
		frame = new JFrame();
		frame.setEnabled(false);
		frame.setAutoRequestFocus(false);
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);

		secondMarker = new JLabel(">");
		panel.add(secondMarker);

		otherNameLab = new JLabel("New label");
		panel.add(otherNameLab);

		JLabel firstMarker_2 = new JLabel("< ");
		panel.add(firstMarker_2);
//		txtPlayerTwo.addMouseListener(ml);
//		txtPlayerTwo.setTransferHandler(new TransferHandler("text"));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(UIManager.getBorder("CheckBox.border"));
		panel_1.setBackground(UIManager.getColor("CheckBox.darkShadow"));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.ipadx = 1;
		gbc_panel_1.weighty = 5.0;
		gbc_panel_1.insets = new Insets(0, 2, 5, 2);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(0, 6, 2, 0));
		JButton btn11 = new JButton("5");
		btn11.setBorderPainted(false);
		btn11.setBackground(new Color(245, 245, 245));
		panel_1.add(btn11);
		btn11.setName("btn11");
		btnList.add(btn11);
		btn11.addMouseMotionListener(mml);
		btn11.addMouseListener(ml);
		btn11.setTransferHandler(new ValueExportTransferHandler(""));
//		btn11.setTransferHandler(new ValueImportTransferHandler());

		JButton btn12 = new JButton("5");
		btn12.setBorderPainted(false);
		btn12.setBackground(new Color(245, 245, 245));
		panel_1.add(btn12);
		btn12.setName("btn12");
		btnList.add(btn12);
		btn12.addMouseMotionListener(mml);
		btn12.addMouseListener(ml);
		btn12.setTransferHandler(new ValueExportTransferHandler(""));
//		btn12.setTransferHandler(new ValueImportTransferHandler());

		JButton btn13 = new JButton("5");
		btn13.setBorderPainted(false);
		btn13.setBackground(new Color(245, 245, 245));
		panel_1.add(btn13);
		btn13.setName("btn13");
		btnList.add(btn13);
		btn13.addMouseMotionListener(mml);
		btn13.addMouseListener(ml);
		btn13.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn14 = new JButton("5");
		btn14.setBorderPainted(false);
		btn14.setBackground(new Color(245, 245, 245));
		panel_1.add(btn14);
		btn14.setName("btn14");
		btnList.add(btn14);
		btn14.addMouseMotionListener(mml);
		btn14.addMouseListener(ml);
		btn14.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn15 = new JButton("5");
		btn15.setBorderPainted(false);
		btn15.setBackground(new Color(245, 245, 245));
		panel_1.add(btn15);
		btn15.setName("btn15");
		btnList.add(btn15);
		btn15.addMouseMotionListener(mml);
		btn15.addMouseListener(ml);
		btn15.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn16 = new JButton("5");
		btn16.setBorderPainted(false);
		btn16.setBackground(new Color(245, 245, 245));
		panel_1.add(btn16);
		btn16.setName("btn16");
		btnList.add(btn16);
		btn16.addMouseMotionListener(mml);
		btn16.addMouseListener(ml);
		btn16.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frame.getContentPane().add(panel_2, gbc_panel_2);

		JButton btnNewButton = new JButton("Rock");
		btnNewButton.setVisible(false);
		panel_2.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Thread nThread = new Thread(new NetThread(true));
				nThread.start();
			}
		});

		midLab = new JLabel("Who go first?");
		panel_2.add(midLab);
		midLab.setTransferHandler(new ValueImportTransferHandler());
		ButtonGroup bg = new ButtonGroup();

		startBtn = new JButton("Roll");
		startBtn.setVisible(false);
		startBtn.setBackground(new Color(224, 255, 255));
		startBtn.setBorder(UIManager.getBorder("CheckBox.border"));
		startBtn.setEnabled(true);
		panel_2.add(startBtn);
		startBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{

				System.out.println("Thread state::: " + nThread.getState());
				sending("002");

//				// random selecting who go first
//				if ((Math.random() < 0.5) == false)
//				{
//					localPlayerTurn = false;
//					midLab.setText(otherPlayerName + "'s turn");
//				} else
//				{
//					localPlayerTurn = true;
//					midLab.setText(localPlayerName + "'s turn");
//				}

//				try
//				{
//					DatagramSocket ds = new DatagramSocket();
//					String str;
//					InetAddress ia = InetAddress.getByName(otherPlayerIp);
//					str = "002";
//					DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
//					ds.send(dp);
//					ds.close();
//				} catch (Exception exx)
//				{
//					// TODO: handle exception
//				}
			}
		});
		/************************** NETWORK **************************/

//		DatagramSocket ds = new DatagramSocket(3000);
//		byte[] buf = new byte[1024];
//		DatagramPacket dp = new DatagramPacket(buf, 1024);
//		ds.receive(dp);e
//		String strRecv = new String(dp.getData(), 0, dp.getLength()) + " from " + dp.getAddress().getHostAddress() + ":"
//				+ dp.getPort();
//		midLab.setText(strRecv);
////		System.out.println(strRecv);
//		ds.close();

		/******************************************************/
//		lblNewLabel.setTransferHandler(new ValueExportTransferHandler(Integer.toString(index + 1)));

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(UIManager.getColor("CheckBox.darkShadow"));
		panel_3.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.ipadx = 1;
		gbc_panel_3.weighty = 5.0;
		gbc_panel_3.insets = new Insets(0, 2, 5, 2);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		frame.getContentPane().add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(0, 6, 2, 0));

		JButton btn01 = new JButton("5");
		btn01.setBorderPainted(false);
		btn01.setBackground(new Color(245, 245, 245));
		panel_3.add(btn01);
		btn01.setName("btn01");
		btnList.add(btn01);

		btn01.addMouseMotionListener(mml);
		btn01.addMouseListener(ml);
		btn01.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn02 = new JButton("5");
		btn02.setBorderPainted(false);
		btn02.setBackground(new Color(245, 245, 245));
		panel_3.add(btn02);
		btn02.setName("btn02");
		btnList.add(btn02);
		btn02.addMouseMotionListener(mml);
		btn02.addMouseListener(ml);
		btn02.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn03 = new JButton("5");
		btn03.setBorderPainted(false);
		btn03.setBackground(new Color(245, 245, 245));
		panel_3.add(btn03);
		btn03.setName("btn03");
		btnList.add(btn03);
		btn03.addMouseMotionListener(mml);
		btn03.addMouseListener(ml);
		btn03.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn04 = new JButton("5");
		btn04.setBorderPainted(false);
		btn04.setBackground(new Color(245, 245, 245));
		panel_3.add(btn04);
		btn04.setName("btn04");
		btnList.add(btn04);
		btn04.addMouseMotionListener(mml);
		btn04.addMouseListener(ml);
		btn04.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn05 = new JButton("5");
		btn05.setBorderPainted(false);
		btn05.setBackground(new Color(245, 245, 245));
		panel_3.add(btn05);
		btn05.setName("btn05");
		btnList.add(btn05);
		btn05.addMouseMotionListener(mml);
		btn05.addMouseListener(ml);
		btn05.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn06 = new JButton("5");
		btn06.setBorderPainted(false);
		btn06.setBackground(new Color(245, 245, 245));
		panel_3.add(btn06);
		btn06.setName("btn06");
		btnList.add(btn06);
		btn06.addMouseMotionListener(mml);
		btn06.addMouseListener(ml);
		btn06.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 4;
		frame.getContentPane().add(panel_4, gbc_panel_4);

		firstMarker = new JLabel(">");
		panel_4.add(firstMarker);

		playerNameLab = new JLabel("New label");
		panel_4.add(playerNameLab);

		JLabel firstMarker_1 = new JLabel("<");
		panel_4.add(firstMarker_1);
		frame.setBounds(100, 100, 552, 228);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		txtPlayerOne.addMouseListener(ml);
//		txtPlayerTwo.setTransferHandler(new TransferHandler("text"));
	}

	public static class ValueExportTransferHandler extends TransferHandler
	{

		public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
		private String value;

		public ValueExportTransferHandler(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}

		@Override
		public int getSourceActions(JComponent c)
		{
			return DnDConstants.ACTION_COPY_OR_MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c)
		{
			Transferable t = new StringSelection(getValue());
			return t;
		}

		@Override
		protected void exportDone(JComponent source, Transferable data, int action)
		{
			super.exportDone(source, data, action);
			// we triggering the number updates from here
			int dragVal = Integer.parseInt(((JButton) source).getText());
			int dropVal = Integer.parseInt(Core.btnDrop.getText());
			// we don't go under 0
			if (dragVal != 0)
				Core.updateValue((dragVal - 1) + "", source.getName(), Core.btnDrop.getName(), (dropVal + 1) + "");
			System.out.println(source.getName() + "  >>>  " + Core.btnDrop.getName());
		}

	}

	public static class ValueImportTransferHandler extends TransferHandler
	{

		public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;

		public ValueImportTransferHandler()
		{
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport support)
		{
			return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
		}

		@Override
		public boolean importData(TransferHandler.TransferSupport support)
		{
			boolean accept = false;
			if (canImport(support))
			{
				try
				{
					Transferable t = support.getTransferable();
					Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
					if (value instanceof String)
					{
						Component component = support.getComponent();
						if (component instanceof JLabel)
						{
							((JLabel) component).setText(value.toString());
							accept = true;
						}
						if (component instanceof JButton)
						{

//							((JButton) component).setText(value.toString());
							Core.btnDrop = (JButton) component;
							System.out.println("Drop target " + component.getName());
						}
					}
				} catch (Exception exp)
				{
					exp.printStackTrace();
				}
			}
			return accept;
		}
	}
}
