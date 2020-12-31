package mancala;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
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
import java.util.Timer;

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
	JButton btnDrag;
	static boolean playable = false;
	static boolean sender = false;
	static ConnectionWindow conWin;
	static JButton btnDrop;
	static ArrayList<JButton> btnList = new ArrayList<JButton>();
	static Core window;
	static boolean inFirst = false;
	static String otherPlayerIp;
	static String localPlayerIp;
	static String otherPlayerName;
	static String localPlayerName;
	static boolean bothePlayersInside = false;
	static Thread nThread;
	static JLabel turnInd11;
	static JLabel turnInd12;
	static JLabel turnInd13;
	static JLabel turnInd14;
	static Timer timer;
	static JLabel playerNameLab;
	static JLabel turnInd1;
	static JLabel turnInd2;
	static JLabel turnInd3;
	static JLabel turnInd4;
	static JLabel otherNameLab;
	static JLabel otherNameScore;
	static JLabel localScore;
	static JLabel otherScore;
	static JLabel midLab;
	static JLabel arrowDown;
	static JLabel arrowUp;

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

		System.out.println("///Translating//// " + command);
		switch (command)
		{
		case "001":// inform 1st player that 2nd player connected
		{
			System.out.println("SWITCH" + cmd);
			bothePlayersInside = true;
			midLab.setText("Your turn");
			nThread.stop();
			wait(1000);
			sending("002" + localPlayerName);
			System.out.println("thread state " + nThread.getState());
		}
			break;
		case "002":// synchronising the midLab //this will run on second player's side
		{
			System.out.println("COMMAND  " + command);
			System.out.println("CONTEXT  " + context);
			midLab.setText(context + "'s turn");
			bothePlayersInside = true;

			wait(500);
			receiving();

			timer = new Timer();
			timer.schedule(new BlinkThread(2), 0, 500);
		}
			break;

		case "003":// sync values//this will "periodically" be running on both sides
		{
			System.out.println("COMMAND  " + command);
			System.out.println("CONTEXT  " + context);
			int hashPos = context.indexOf('#');
			String score = context.substring(hashPos + 1, context.length());
//			System.out.println("SCORE " + score);
//			System.out.println(" new CONTEXT  " + context.substring(0, hashPos));
			otherScore.setText(score);
			innerDistribution(context.substring(0, hashPos));
			midLab.setText("Your turn");
			wait(500);
			receiving();

			playable = true;
			timer.cancel();
			timer = new Timer();
			timer.schedule(new BlinkThread(1), 0, 500);
		}
			break;
		}
	}

	// launching the connection window
	static void openConnectionDialog()
	{
		conWin = new ConnectionWindow();

		conWin.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		conWin.setVisible(true);

		conWin.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.out.println("CLOSE");
				System.exit(0);
			}
		});
	}

	// ********************
	// *********** first thing that get called when the game start
	// *******************
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

		conWin.setVisible(false);
		window.frame.setVisible(true);
		window.frame.setEnabled(true);
		playerNameLab.setText(playerName);
		otherNameLab.setText(otherPlayerName);
		otherNameScore.setText(otherPlayerName + "'s score: ");

		// *******blinking effect*****
		Timer timer2 = new Timer();
		timer2.schedule(new BlinkThread(0), 0, 1000);
	}

	static void firstInteraction()
	{
		if (inFirst)
		{
			System.out.println("First Player In");
			midLab.setText("Waiting for " + otherPlayerName + " to connect");
			wait(500);
			receiving();
			playable = true;

			timer = new Timer();
			timer.schedule(new BlinkThread(1), 0, 500);

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

	static void innerDistribution(String bName)
	{
		JButton seletedBtn = null;
		// getting object button from a name
		for (int u = 0; u < btnList.size(); u++)
		{
			if (btnList.get(u).getName().equals(bName))
			{
				seletedBtn = btnList.get(u);
			}
		}
//*************
		System.out.println("CURRENT BTN NUM:: " + seletedBtn.getName().substring(3, seletedBtn.getName().length()));
		int amount = Integer.parseInt(seletedBtn.getText());
		int val = Integer.parseInt(seletedBtn.getName().substring(3, seletedBtn.getName().length()));

		seletedBtn.setText("0");
		for (int i = amount; i > 0; i--)// if the button contain 6, this will run 6 times
		{
			if (val == 12)
				val = 1;
			else
				val++;

			String newPosName = "btn" + val;
			if (newPosName.equals(seletedBtn.getName()))// this for skipping the initial button
			{
				val++;
				newPosName = "btn" + val;
			}

//			System.out.println("PAAAAA " + amount + " " + i);

			System.out.println(newPosName + "  " + seletedBtn.getName());

			for (int j = 0; j < btnList.size(); j++)
			{
				if (btnList.get(j).getName().equals(newPosName))
				{
					btnList.get(j).setText((Integer.parseInt(btnList.get(j).getText()) + 1) + "");
//					System.out.println("LOOMMM " + btnList.get(j).getText());
					if (playable)
					{// if it is our turn
						if (i == 1)// and if we are on the last iteration (last button)
							// and if the final button is on the other side
							if (sideCheck(btnList.get(j)))

								scoreCounting(btnList.get(j));// count the score
					} else// if not our turn
					{// just do the score (no side check)
						if (i == 1)
							scoreCounting(btnList.get(j));// count the score
					}

				}
			}
		}

	}

	static boolean sideCheck(JButton btnToCheck)
	{
		if (btnToCheck.getName() == "btn7" || btnToCheck.getName() == "btn8" || btnToCheck.getName() == "btn9"
				|| btnToCheck.getName() == "btn10" || btnToCheck.getName() == "btn11"
				|| btnToCheck.getName() == "btn12")
			return true;
		else
			return false;
	}

//we get the last button, if it contains 2 or 3 we check the one behind it and so on...
	static void scoreCounting(JButton lastBtn)
	{// if it is our turn and the button we currently checking is on our side
		if (playable && !sideCheck(lastBtn))
			return;
		// if it is NOT our turn and the button we currently checking in on the other
		// side
		if (!playable && sideCheck(lastBtn))
			return;
		if (lastBtn.getText().equals("2") || lastBtn.getText().equals("3"))
		{
			int newScore = (Integer.parseInt(localScore.getText())) + (Integer.parseInt(lastBtn.getText()));
			if (playable)// we don't want to calculate the score when it's not our turn
				localScore.setText(newScore + "");
			lastBtn.setText("0");
			int val = Integer.parseInt(lastBtn.getName().substring(3, lastBtn.getName().length()));
			val--;
			for (int ii = 0; ii < btnList.size(); ii++)
			{
				if (btnList.get(ii).getName().equals("btn" + val))
				{
					scoreCounting(btnList.get(ii)); // recursion yey!!
				}
			}
		} else
			return;
	}

	void distribution(MouseEvent e)
	{
		JButton jc = (JButton) e.getSource();
		if (jc.getName() == "btn7" || jc.getName() == "btn8" || jc.getName() == "btn9" || jc.getName() == "btn10"
				|| jc.getName() == "btn11" || jc.getName() == "btn12")
			return;// nothing happen if clicked on opponent's buttons
		System.out.println("*****CURRENT PLAYER PLAYED HIS TURN*******  " + localPlayerName);

		innerDistribution(jc.getName());

		// at this point, the turn ended and the score got calculated
		sending("003" + "btn" + (Integer.parseInt(jc.getName().substring(3, jc.getName().length())) + 6) + "#"
				+ localScore.getText());
		playable = false;
		midLab.setText(otherPlayerName + "'s turn");
		timer.cancel();
		timer = new Timer();
		timer.schedule(new BlinkThread(2), 0, 500);

		wait(500);
		receiving();
	}

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
			if (!playable)// this so the 2nd player won't touch anything when they start
				return;
			if (!bothePlayersInside)// this so the 1st player won't start playing till the 2nd player connect
				return;
			JButton btn = (JButton) e.getSource();
			if (btn.getText() == "0")// nothing happens if clicked on an empty button
				return;
			distribution(e);
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
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel panel_9_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_9_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_9_1.setBackground(new Color(255, 228, 225));
		panel.add(panel_9_1);

		otherNameScore = new JLabel("XYZ score: ");
		panel_9_1.add(otherNameScore);

		otherScore = new JLabel("0");
		panel_9_1.add(otherScore);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(new Color(255, 228, 225));
		panel.add(panel_8);

		turnInd1 = new JLabel(">");
		turnInd1.setForeground(Color.WHITE);
		turnInd1.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_8.add(turnInd1);

		turnInd2 = new JLabel(">");
		turnInd2.setForeground(Color.WHITE);
		turnInd2.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_8.add(turnInd2);

		otherNameLab = new JLabel("New label");
		otherNameLab.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_8.add(otherNameLab);

		turnInd3 = new JLabel("< ");
		turnInd3.setForeground(Color.WHITE);
		turnInd3.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_8.add(turnInd3);

		turnInd4 = new JLabel("< ");
		turnInd4.setForeground(Color.WHITE);
		turnInd4.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_8.add(turnInd4);

		JPanel panel_10_1 = new JPanel();
		panel_10_1.setBackground(new Color(255, 228, 225));
		panel.add(panel_10_1);
//		txtPlayerTwo.addMouseListener(ml);
//		txtPlayerTwo.setTransferHandler(new TransferHandler("text"));

		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.setBorder(UIManager.getBorder("CheckBox.border"));
		panel_1.setBackground(new Color(128, 0, 0));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.ipadx = 1;
		gbc_panel_1.weighty = 5.0;
		gbc_panel_1.insets = new Insets(0, 2, 5, 2);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(0, 6, 2, 0));

		JButton btn12 = new JButton("4");
		btn12.setBorderPainted(false);
		btn12.setBackground(new Color(245, 245, 245));
		panel_1.add(btn12);
		btn12.setName("btn12");
		btnList.add(btn12);
//		btn12.addMouseMotionListener(mml);
		btn12.addMouseListener(ml);
//		btn12.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn11 = new JButton("4");
		btn11.setBorderPainted(false);
		btn11.setBackground(new Color(245, 245, 245));
		panel_1.add(btn11);
		btn11.setName("btn11");
		btnList.add(btn11);
//		btn11.addMouseMotionListener(mml);
		btn11.addMouseListener(ml);
//		btn11.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn10 = new JButton("4");
		btn10.setBorderPainted(false);
		btn10.setBackground(new Color(245, 245, 245));
		panel_1.add(btn10);
		btn10.setName("btn10");
		btnList.add(btn10);
//		btn10.addMouseMotionListener(mml);
		btn10.addMouseListener(ml);
//		btn10.setTransferHandler(new ValueExportTransferHandler(""));
		// btn8.setTransferHandler(new ValueImportTransferHandler());

		JButton btn9 = new JButton("4");
		btn9.setBorderPainted(false);
		btn9.setBackground(new Color(245, 245, 245));
		panel_1.add(btn9);
		btn9.setName("btn9");
		btnList.add(btn9);
//		btn9.addMouseMotionListener(mml);
		btn9.addMouseListener(ml);
//		btn9.setTransferHandler(new ValueExportTransferHandler(""));
		// btn7.setTransferHandler(new ValueImportTransferHandler());

		JButton btn8 = new JButton("4");
		btn8.setBorderPainted(false);
		btn8.setBackground(new Color(245, 245, 245));
		panel_1.add(btn8);
		btn8.setName("btn8");
		btnList.add(btn8);
//		btn8.addMouseMotionListener(mml);
		btn8.addMouseListener(ml);
//		btn8.setTransferHandler(new ValueExportTransferHandler(""));
		JButton btn7 = new JButton("4");
		btn7.setBorderPainted(false);
		btn7.setBackground(new Color(245, 245, 245));
		panel_1.add(btn7);
		btn7.setName("btn7");
		btnList.add(btn7);
//		btn7.addMouseMotionListener(mml);
		btn7.addMouseListener(ml);
//		btn7.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel panel_11 = new JPanel();
		panel_11.setOpaque(false);
		FlowLayout flowLayout_2 = (FlowLayout) panel_11.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panel_2.add(panel_11);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(UIManager.getBorder("CheckBox.border"));
		panel_11.add(panel_6);

		arrowDown = new JLabel("\u02C5");
		arrowDown.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_6.add(arrowDown);
		ButtonGroup bg = new ButtonGroup();

		JPanel panel_13 = new JPanel();
		panel_13.setOpaque(false);
		FlowLayout flowLayout_4 = (FlowLayout) panel_13.getLayout();
		flowLayout_4.setHgap(3);
		flowLayout_4.setVgap(12);
		panel_2.add(panel_13);

		midLab = new JLabel("Who go first?");
		panel_13.add(midLab);

		JPanel panel_12 = new JPanel();
		panel_12.setOpaque(false);
		FlowLayout flowLayout_3 = (FlowLayout) panel_12.getLayout();
		flowLayout_3.setAlignment(FlowLayout.RIGHT);
		panel_2.add(panel_12);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(UIManager.getBorder("CheckBox.border"));
		panel_12.add(panel_5);

		arrowUp = new JLabel("\u02C4");
		arrowUp.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_5.add(arrowUp);
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
		panel_3.setBackground(new Color(128, 0, 0));
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

		JButton btn1 = new JButton("4");
		btn1.setBorderPainted(false);
		btn1.setBackground(new Color(245, 245, 245));
		panel_3.add(btn1);
		btn1.setName("btn1");
		btnList.add(btn1);

//		btn1.addMouseMotionListener(mml);
		btn1.addMouseListener(ml);
//		btn1.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn2 = new JButton("4");
		btn2.setBorderPainted(false);
		btn2.setBackground(new Color(245, 245, 245));
		panel_3.add(btn2);
		btn2.setName("btn2");
		btnList.add(btn2);
//		btn2.addMouseMotionListener(mml);
		btn2.addMouseListener(ml);
//		btn2.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn3 = new JButton("4");
		btn3.setBorderPainted(false);
		btn3.setBackground(new Color(245, 245, 245));
		panel_3.add(btn3);
		btn3.setName("btn3");
		btnList.add(btn3);
//		btn3.addMouseMotionListener(mml);
		btn3.addMouseListener(ml);
//		btn3.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn4 = new JButton("4");
		btn4.setBorderPainted(false);
		btn4.setBackground(new Color(245, 245, 245));
		panel_3.add(btn4);
		btn4.setName("btn4");
		btnList.add(btn4);
//		btn4.addMouseMotionListener(mml);
		btn4.addMouseListener(ml);
//		btn4.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn5 = new JButton("4");
		btn5.setBorderPainted(false);
		btn5.setBackground(new Color(245, 245, 245));
		panel_3.add(btn5);
		btn5.setName("btn5");
		btnList.add(btn5);
//		btn5.addMouseMotionListener(mml);
		btn5.addMouseListener(ml);
//		btn5.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn6 = new JButton("4");
		btn6.setBorderPainted(false);
		btn6.setBackground(new Color(245, 245, 245));
		panel_3.add(btn6);
		btn6.setName("btn6");
		btnList.add(btn6);
//		btn6.addMouseMotionListener(mml);
		btn6.addMouseListener(ml);
//		btn6.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 4;
		frame.getContentPane().add(panel_4, gbc_panel_4);
		panel_4.setLayout(new GridLayout(0, 3, 0, 0));

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(new Color(224, 255, 255));
		FlowLayout flowLayout_1 = (FlowLayout) panel_9.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_9);

		JLabel localNameScore = new JLabel("Your score: ");
		panel_9.add(localNameScore);

		localScore = new JLabel("0");
		panel_9.add(localScore);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(new Color(224, 255, 255));
		panel_4.add(panel_7);

		turnInd11 = new JLabel(">");
		turnInd11.setForeground(Color.WHITE);
		turnInd11.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_7.add(turnInd11);

		turnInd12 = new JLabel(">");
		turnInd12.setForeground(Color.WHITE);
		turnInd12.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_7.add(turnInd12);

		playerNameLab = new JLabel("New label");
		playerNameLab.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_7.add(playerNameLab);

		turnInd13 = new JLabel("<");
		turnInd13.setForeground(Color.WHITE);
		turnInd13.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_7.add(turnInd13);

		turnInd14 = new JLabel("<");
		turnInd14.setForeground(Color.WHITE);
		turnInd14.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_7.add(turnInd14);

		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(224, 255, 255));
		panel_4.add(panel_10);
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
