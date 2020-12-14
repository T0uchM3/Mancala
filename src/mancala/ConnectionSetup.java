package mancala;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ConnectionSetup extends JDialog
{

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			ConnectionSetup dialog = new ConnectionSetup();
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setVisible(true);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConnectionSetup()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 208);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblNewLabel = new JLabel("Search for a player");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblNewLabel.setMaximumSize(new Dimension(92, 25));
			lblNewLabel.setMinimumSize(new Dimension(92, 25));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel, BorderLayout.NORTH);
		}
		{
			JButton btnNewButton_1 = new JButton("New button");
			contentPanel.add(btnNewButton_1, BorderLayout.WEST);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JButton btnNewButton = new JButton("Search");
				btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				panel.add(btnNewButton);
			}
		}
		{
			JLabel lblNewLabel_1 = new JLabel("New label");
			lblNewLabel_1.setPreferredSize(new Dimension(200, 14));
			contentPanel.add(lblNewLabel_1, BorderLayout.EAST);
		}
	}

}
