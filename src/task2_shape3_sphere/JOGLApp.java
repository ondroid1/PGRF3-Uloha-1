package task2_shape3_sphere;


import common.BasicRenderer;
import common.FrameFactory;

import javax.swing.*;

public class JOGLApp {


	private void start() {
		try {
			BasicRenderer renderer = new BasicRenderer("/task2_shape3_sphere/start.vert",
					"/task2_shape3_sphere/start.frag");
			FrameFactory.GetApplicationFrame(renderer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}