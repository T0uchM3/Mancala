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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

public class TesT2
{

	private JFrame frame;
	private JTextField txtPlayerOne;
	private JTextField txtPlayerTwo;
	JButton btn11;
	JButton btn12;
	ArrayList<JButton> btnList = new ArrayList<JButton>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
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
					TesT2 window = new TesT2();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TesT2()
	{
		initialize();
	}

	MouseMotionListener ml = new MouseMotionListener()
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
			if (jc.getName() == "btn11")
			{
				System.out.println("testDebugg " + btnList.get(0).getName());
				btnList.get(0).setTransferHandler(new ValueExportTransferHandler("text"));
				btnList.get(1).setTransferHandler(new ValueImportTransferHandler());
//				btn12.setTransferHandler(new ValueImportTransferHandler());
			}
			if (jc.getName() == "btn12")
			{
				btnList.get(1).setTransferHandler(new ValueExportTransferHandler("text"));
				btnList.get(0).setTransferHandler(new ValueImportTransferHandler());
			}
//			System.out.println("testDebugg " + jc.get);
		}
	};

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize()
	{

		frame = new JFrame();
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
		btn11.addMouseMotionListener(ml);
		btn11.setTransferHandler(new ValueExportTransferHandler("Test"));
//		btn11.setTransferHandler(new ValueImportTransferHandler());

		JButton btn12 = new JButton("5");
		panel_1.add(btn12);
		btn12.setName("btn12");
		btnList.add(btn12);
		btn12.addMouseMotionListener(ml);
		btn12.setTransferHandler(new ValueExportTransferHandler("Test"));
//		btn12.setTransferHandler(new ValueImportTransferHandler());

		JButton btn13 = new JButton("5");
		panel_1.add(btn13);

		JButton btn14 = new JButton("5");
		panel_1.add(btn14);

		JButton btn15 = new JButton("5");
		panel_1.add(btn15);

		JButton btn16 = new JButton("5");
		panel_1.add(btn16);

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 2;
		frame.getContentPane().add(panel_2, gbc_panel_2);

		JLabel lblNewLabel = new JLabel("New label");
		panel_2.add(lblNewLabel);
		lblNewLabel.setTransferHandler(new ValueImportTransferHandler());
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

		JButton btn02 = new JButton("5");
		panel_3.add(btn02);

		JButton btn03 = new JButton("5");
		panel_3.add(btn03);

		JButton btn04 = new JButton("5");
		panel_3.add(btn04);

		JButton btn05 = new JButton("5");
		panel_3.add(btn05);

		JButton btn06 = new JButton("5");
		panel_3.add(btn06);

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 4;
		frame.getContentPane().add(panel_4, gbc_panel_4);

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
			// Decide what to do after the drop has been accepted
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
							((JButton) component).setText(value.toString());
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
