package pluginHelper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.DropMode;
import javax.swing.JTextArea;
import java.awt.Font;

public class SetSOP extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void start()
	{
		try
		{
			SetSOP dialog = new SetSOP();
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
	private SetSOP()
	{
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 330, 187);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.NORTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblNewLabel_2 = new JLabel("输入您的QQ号（该号码将作为最高权限号码）");
				lblNewLabel_2.setFont(new Font("黑体", Font.PLAIN, 15));
				panel.add(lblNewLabel_2, BorderLayout.NORTH);
			}
			{
				textField = new JTextField();
				textField.setFont(new Font("黑体", Font.PLAIN, 15));
				panel.add(textField);
				textField.setColumns(10);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel lblNewLabel = new JLabel("输入管理员号码（分号隔开）");
				lblNewLabel.setFont(new Font("黑体", Font.PLAIN, 15));
				panel.add(lblNewLabel, BorderLayout.NORTH);
			}
			{
				textArea = new JTextArea();
				textArea.setFont(new Font("宋体", Font.PLAIN, 15));
				textArea.setLineWrap(true);
				panel.add(textArea, BorderLayout.CENTER);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BorderLayout(0, 0));

			final JLabel lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setForeground(Color.RED);
			buttonPane.add(lblNewLabel_1, BorderLayout.WEST);

			{
				JButton okButton = new JButton("确定");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						String s = textField.getText();
						long SOP;
						long[] OPs;
						try
						{
							SOP = Long.parseLong(s);
						} catch (NumberFormatException e)
						{
							lblNewLabel_1.setText("请输入正确的SOP号码");
							return;
						}
						try
						{
							if (!textArea.getText().isEmpty())
							{
								String[] ops = textArea.getText().split(";");
								OPs = new long[ops.length];
								for (int i = 0; i < ops.length; i++)
									OPs[i] = Long.parseLong(ops[i]);
							} else
								OPs = new long[0];
						} catch (NumberFormatException e)
						{
							lblNewLabel_1.setText("请输入正确的OP号码");
							return;
						}
						AuthirizerListBook.setSOP(SOP);
						for (long l : OPs)
							AuthirizerListBook.setOP(l);
						JOptionPane.showMessageDialog(contentPanel, "设置完成");
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton, BorderLayout.EAST);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
