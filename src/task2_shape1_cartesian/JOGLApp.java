package task2_shape1_cartesian;

import common.BasicRenderer;
import common.FrameFactory;

import javax.swing.*;

public class JOGLApp {


	private void start() {
		try {
			BasicRenderer renderer = new BasicRenderer("/task2_shape1_cartesian/start.vert",
					"/task2_shape1_cartesian/start.frag");
			FrameFactory.GetApplicationFrame(renderer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new task2_shape1_cartesian.JOGLApp().start());
	}

}