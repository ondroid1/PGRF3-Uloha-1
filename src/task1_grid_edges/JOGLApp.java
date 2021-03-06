package task1_grid_edges;


import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import common.Constants;
import common.FrameFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second

	private void start() {
		try {
			Frame testFrame = new Frame(Constants.FRAME_TITLE);
			testFrame.setSize(512, 384);

			// setup OpenGL version
			GLProfile profile = GLProfile.getMaximum(true);
			GLCapabilities capabilities = new GLCapabilities(profile);

			// The canvas is the widget that's drawn in the JFrame
			GLCanvas canvas = new GLCanvas(capabilities);
			Renderer ren = new Renderer();
			canvas.addGLEventListener(ren);
			canvas.addMouseListener(ren);
			canvas.addMouseMotionListener(ren);
			canvas.addKeyListener(ren);
			canvas.setSize( 512, 384 );

			testFrame.add(canvas);

			//final Animator animator = new Animator(canvas);
			final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

			testFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					new Thread(() -> {
						if (animator.isStarted()) animator.stop();
						System.exit(0);
					}).start();
				}
			});
			testFrame.setTitle(ren.getClass().getName());
			testFrame.pack();
			testFrame.setVisible(true);
			animator.start(); // start the animation loop


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}