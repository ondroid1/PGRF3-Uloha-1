package task3_uniform_time;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import common.BasicRenderer;
import common.FrameFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JOGLApp {


	private void start() {
		try {
			BasicRenderer renderer = new BasicRenderer("/task3_uniform_time/start.vert",
					"/task3_uniform_time/start.frag");
			FrameFactory.GetApplicationFrame(renderer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}