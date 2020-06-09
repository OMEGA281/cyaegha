package global;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import pluginHelper.SetSOP;

import javax.swing.JTextPane;
import javax.swing.DropMode;

public class ReadDisclaimer extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	private JButton okButton;

	/**
	 * Launch the application.
	 */
	public static void start()
	{
		try
		{
			ReadDisclaimer dialog = new ReadDisclaimer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ReadDisclaimer()
	{
		setTitle("初始化程序");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			textArea = new JTextArea();
			textArea.setEditable(false);
		}
		contentPanel.setLayout(new BorderLayout(0, 0));
		contentPanel.add(textArea);
		{
			final JCheckBox chckbxNewCheckBox = new JCheckBox("我已认真阅读并同意上述条款");
			chckbxNewCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okButton.setEnabled(chckbxNewCheckBox.isSelected());
				}
			});
			contentPanel.add(chckbxNewCheckBox, BorderLayout.SOUTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("下一步");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SetSOP.start();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("退出");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
