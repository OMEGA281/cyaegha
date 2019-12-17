package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class FloatWindow extends JFrame {

	private JPanel contentPane;
	static int pointX,pointY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FloatWindow frame = new FloatWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FloatWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setBackground(new Color(0,0,0,0));
		addMouseListener(new MouseAdapter() 
		{
			public void mousePressed(MouseEvent e) 
            {                 
               pointX = e.getPoint().x;
               pointY= e.getPoint().y;
            }
         });
		addMouseMotionListener(new MouseMotionAdapter()
          {
              public void mouseDragged(MouseEvent e) 
              {
                  setLocation((e.getXOnScreen()-pointX),(e.getYOnScreen()-pointY));//设置拖拽后，窗口的位置
              }
		});
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0,0,0,0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
