package task4_pixel_debug_xyz;

import common.BasicRenderer;
import common.FrameFactory;

import javax.swing.*;

public class JOGLApp {


	private void start() {
		try {
			BasicRenderer renderer = new BasicRenderer("/task4_pixel_debug_xyz/start.vert",
					"/task4_pixel_debug_xyz/start.frag");
			FrameFactory.GetApplicationFrame(renderer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}