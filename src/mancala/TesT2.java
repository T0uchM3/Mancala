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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

public class TesT2
{

	private static JFrame frame;
	private JTextField txtPlayerOne;
	private JTextField txtPlayerTwo;
	static JLabel midLab;
	JButton btnDrag;
	JLabel firstMarker;
	JLabel secondMarker;
	JButton startBtn;
	static boolean localPlayerTurn = true;
	static boolean sender = false;
	static ConnectionWindow cw;
	static JButton btnDrop;
	static ArrayList<JButton> btnList = new ArrayList<JButton>();
	static TesT2 window;

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
					window = new TesT2();
					window.frame.setVisible(false);
					window.frame.setEnabled(false);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	static void openConnectionDialog()
	{
		cw = new ConnectionWindow();
//		ConnectionSetup cs = new ConnectionSetup();
//		cw.setModalityType(ModalityType.APPLICATION_MODAL);

		cw.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		cw.setVisible(true);
		cw.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				// TODO Auto-generated method stub
				System.out.println("CLOSE");
				System.exit(0);
//				cw.dispose();
//				frame.setVisible(true);
			}
		});
	}

	static void foundPlayer()
	{
		cw.setVisible(false);
		window.frame.setVisible(true);
		window.frame.setEnabled(true);
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public TesT2() throws Exception
	{
		initialize();
	}

	void sendPacket()
	{
		try
		{
			DatagramSocket ds = new DatagramSocket();
			String str = "hello world";
//			InetAddress ia = InetAddress.getByName("localhost");
			InetAddress ia = InetAddress.getByName(txtPlayerOne.getText());
			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ia, 3000);
			ds.send(dp);
			ds.close();
		} catch (Exception ex)
		{
			// TODO: handle exception
		}
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
		frame.getContentPane().setBackground(new Color(224, 255, 255));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]
		{ 0, 0 };
		gridBagLayout.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[]
		{ 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setHgap(0);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);

		secondMarker = new JLabel(">  ");
		panel.add(secondMarker);

		txtPlayerTwo = new JTextField();
		txtPlayerTwo.setText("Player Two");
		txtPlayerTwo.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtPlayerTwo);
		txtPlayerTwo.setColumns(10);
//		txtPlayerTwo.addMouseListener(ml);
//		txtPlayerTwo.setTransferHandler(new TransferHandler("text"));

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.weighty = 5.0;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(0, 6, 0, 0));
		JButton btn11 = new JButton("5");
		panel_1.add(btn11);
		btn11.setName("btn11");
		btnList.add(btn11);
		btn11.addMouseMotionListener(mml);
		btn11.addMouseListener(ml);
		btn11.setTransferHandler(new ValueExportTransferHandler(""));
//		btn11.setTransferHandler(new ValueImportTransferHandler());

		JButton btn12 = new JButton("5");
		panel_1.add(btn12);
		btn12.setName("btn12");
		btnList.add(btn12);
		btn12.addMouseMotionListener(mml);
		btn12.addMouseListener(ml);
		btn12.setTransferHandler(new ValueExportTransferHandler(""));
//		btn12.setTransferHandler(new ValueImportTransferHandler());

		JButton btn13 = new JButton("5");
		panel_1.add(btn13);
		btn13.setName("btn13");
		btnList.add(btn13);
		btn13.addMouseMotionListener(mml);
		btn13.addMouseListener(ml);
		btn13.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn14 = new JButton("5");
		panel_1.add(btn14);
		btn14.setName("btn14");
		btnList.add(btn14);
		btn14.addMouseMotionListener(mml);
		btn14.addMouseListener(ml);
		btn14.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn15 = new JButton("5");
		panel_1.add(btn15);
		btn15.setName("btn15");
		btnList.add(btn15);
		btn15.addMouseMotionListener(mml);
		btn15.addMouseListener(ml);
		btn15.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn16 = new JButton("5");
		panel_1.add(btn16);
		btn16.setName("btn16");
		btnList.add(btn16);
		btn16.addMouseMotionListener(mml);
		btn16.addMouseListener(ml);
		btn16.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frame.getContentPane().add(panel_2, gbc_panel_2);

		midLab = new JLabel("First or Second?");
		panel_2.add(midLab);
		midLab.setTransferHandler(new ValueImportTransferHandler());

		JRadioButton firstRB = new JRadioButton("First");
		firstRB.setActionCommand("firstRB");
		panel_2.add(firstRB);
		firstRB.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				startBtn.setEnabled(true);
			}
		});

		JRadioButton secondRB = new JRadioButton("Second");
		secondRB.setActionCommand("econdRB");
		panel_2.add(secondRB);
		secondRB.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				startBtn.setEnabled(true);
			}
		});
		ButtonGroup bg = new ButtonGroup();
		bg.add(firstRB);
		bg.add(secondRB);

		startBtn = new JButton("Start");
		startBtn.setEnabled(false);
		panel_2.add(startBtn);
		startBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				if (bg.getSelection().getActionCommand() == "firstRB")// This go first, cuz it wait for incs
				{
					Thread nThread = new Thread(new NetThread());
					nThread.start();
					sender = true;
					System.out.println("sender = " + sender);
				} else// and this go second
				{
					sendPacket();
					sender = false;
					System.out.println("sender = " + sender);
				}

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
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.weighty = 5.0;
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 3;
		frame.getContentPane().add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(0, 6, 0, 0));

		JButton btn01 = new JButton("5");
		panel_3.add(btn01);
		btn01.setName("btn01");
		btnList.add(btn01);

		btn01.addMouseMotionListener(mml);
		btn01.addMouseListener(ml);
		btn01.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn02 = new JButton("5");
		panel_3.add(btn02);
		btn02.setName("btn02");
		btnList.add(btn02);
		btn02.addMouseMotionListener(mml);
		btn02.addMouseListener(ml);
		btn02.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn03 = new JButton("5");
		panel_3.add(btn03);
		btn03.setName("btn03");
		btnList.add(btn03);
		btn03.addMouseMotionListener(mml);
		btn03.addMouseListener(ml);
		btn03.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn04 = new JButton("5");
		panel_3.add(btn04);
		btn04.setName("btn04");
		btnList.add(btn04);
		btn04.addMouseMotionListener(mml);
		btn04.addMouseListener(ml);
		btn04.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn05 = new JButton("5");
		panel_3.add(btn05);
		btn05.setName("btn05");
		btnList.add(btn05);
		btn05.addMouseMotionListener(mml);
		btn05.addMouseListener(ml);
		btn05.setTransferHandler(new ValueExportTransferHandler(""));

		JButton btn06 = new JButton("5");
		panel_3.add(btn06);
		btn06.setName("btn06");
		btnList.add(btn06);
		btn06.addMouseMotionListener(mml);
		btn06.addMouseListener(ml);
		btn06.setTransferHandler(new ValueExportTransferHandler(""));

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 4;
		frame.getContentPane().add(panel_4, gbc_panel_4);

		firstMarker = new JLabel("> ");
		panel_4.add(firstMarker);

		txtPlayerOne = new JTextField();
		txtPlayerOne.setHorizontalAlignment(SwingConstants.CENTER);
		txtPlayerOne.setText("Player One");
		panel_4.add(txtPlayerOne);
		txtPlayerOne.setColumns(10);
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
			int dropVal = Integer.parseInt(TesT2.btnDrop.getText());
			// we don't go under 0
			if (dragVal != 0)
				TesT2.updateValue((dragVal - 1) + "", source.getName(), TesT2.btnDrop.getName(), (dropVal + 1) + "");
			System.out.println(source.getName() + "  >>>  " + TesT2.btnDrop.getName());
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
							TesT2.btnDrop = (JButton) component;
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
