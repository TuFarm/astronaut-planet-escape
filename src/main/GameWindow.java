package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {

	private JFrame jframe;

	public GameWindow(GamePanel gamePanel) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		JFrame jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false); // Có thể cho true nếu bạn muốn resize
		jframe.add(gamePanel);
		jframe.pack();

		jframe.setSize(screenWidth, screenHeight); // Đặt theo kích thước màn hình
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();

			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
}
