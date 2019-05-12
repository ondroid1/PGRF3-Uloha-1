import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import common.BasicRenderer;
import common.Demo;
import common.RendererType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


import java.util.List;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second
	private GLCanvas canvas = null;
	private Frame applicationFrame;
	private int demoId = 1;
	static int[] countMenuItems = {15,20};
	private KeyAdapter keyAdapter;
	private static Demo[] demos = new Demo[] {
		// 1. bod
		new Demo ("Bod 1 - Pouze hrany", "task1_grid_edges", RendererType.OWN, null, null),
		new Demo ("Bod 1 - Povrch vyplněný barvou", "task1_grid_filled", RendererType.BASIC, "/task1_grid_filled/start.vert",
				"/task1_grid_filled/start.frag"),
		// 2. bod
		new Demo ("Bod 2 - První těleso - kartézské souřadnice", "task2_shape1_cartesian", RendererType.BASIC, "/task2_shape1_cartesian/start.vert",
				"/task2_shape1_cartesian/start.frag"),
		new Demo ("Bod 2 - Druhé těleso - kartézské souřadnice", "task2_shape2_cartesian", RendererType.BASIC, "/task2_shape2_cartesian/start.vert",
				"/task2_shape2_cartesian/start.frag"),
		new Demo ("Bod 2 - První těleso - sférické souřadnice", "task2_shape3_sphere", RendererType.BASIC, "/task2_shape3_sphere/start.vert",
				"/task2_shape3_sphere/start.frag"),
		new Demo ("Bod 2 - Druhé těleso - sférické souřadnice", "task2_shape4_sphere", RendererType.BASIC, "/task2_shape4_sphere/start.vert",
				"/task2_shape4_sphere/start.frag"),
		new Demo ("Bod 2 - První těleso - cylindrické souřadnice", "task2_shape5_cylindrical", RendererType.BASIC, "/task2_shape5_cylindrical/start.vert",
				"/task2_shape5_cylindrical/start.frag"),
		new Demo ("Bod 2 - Druhé těleso - cylindrické souřadnice", "task2_shape6_cylindrical", RendererType.BASIC, "/task2_shape6_cylindrical/start.vert",
				"/task2_shape6_cylindrical/start.frag"),
		// 3. bod
		new Demo ("Bod 3 - Modifikace funkce v čase", "task3_uniform_time", RendererType.BASIC, "/task3_uniform_time/start.vert",
				"/task3_uniform_time/start.frag"),
		// 4. bod
		new Demo ("Bod 4 - Pixelové zobrazení povrchu - XYZ", "task4_pixel_debug_xyz", RendererType.BASIC, "/task4_pixel_debug_xyz/start.vert",
				"/task4_pixel_debug_xyz/start.frag"),
		new Demo ("Bod 4 - Pixelové zobrazení povrchu - normála", "task4_pixel_debug_normal", RendererType.BASIC, "/task4_pixel_debug_normal/start.vert",
				"/task4_pixel_debug_xyz/start.frag"),
		new Demo ("Bod 4 - Pixelové zobrazení povrchu - textura", "task4_pixel_debug_texture", RendererType.BASIC, "/task4_pixel_debug_texture/start.vert",
				"/task4_pixel_debug_xyz/start.frag"),
	};

	public void start() {
		try {
			applicationFrame = new Frame("PGRF3 - Úloha 1 - Ondřej Stieber");
			applicationFrame.setSize(512, 384);
			
			makeGUI(applicationFrame, getDemoNames());

			setApp(applicationFrame, demos[0]);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String[] getDemoNames() {

		return Stream.of(demos).map(a -> a.getDemoName()).toArray(String[]::new);
	}
	
	private void makeGUI(Frame testFrame, String[] demoNames) {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
//				demoId = Integer
//						.valueOf(ae.getActionCommand().substring(0, ae.getActionCommand().indexOf('-') - 1).trim());
				demoId = Arrays.asList(demoNames).indexOf(ae.getActionCommand().substring(ae.getActionCommand().indexOf('-') + 2));
				setApp(testFrame, demos[demoId]);
			}
		};

		MenuBar menuBar = new MenuBar();
		int menuIndex = 0;
		for(int itemMenu = 0 ; itemMenu < 1; itemMenu++){
			Menu menu1 = new Menu("Ukázka");
			MenuItem m;
			for (int i = 0; i < demoNames.length && i < countMenuItems[itemMenu]; i++) {
				m = new MenuItem(new Integer(menuIndex + 1).toString() + " - "
						+ demoNames[menuIndex]);
				m.addActionListener(actionListener);
				menu1.add(m);
				menuIndex++;
			}
			menuBar.add(menu1);
		}
		
		keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_HOME:
					demoId = 1;
					setApp(testFrame, demos[demoId - 1]);

					break;
				case KeyEvent.VK_END:
					demoId = demoNames.length;
					setApp(testFrame, demos[demoId - 1]);
					break;
				case KeyEvent.VK_LEFT:
					if (demoId > 1)
						demoId--;
					setApp(testFrame, demos[demoId - 1]);
					break;
				case KeyEvent.VK_RIGHT:
					if (demoId < demoNames.length)
						demoId++;
					setApp(testFrame, demos[demoId - 1]);
					break;
				}
			}

		};

		testFrame.setMenuBar(menuBar);
	}

	private void setApp(Frame testFrame, Demo demo) {
		Dimension dim;
		if (canvas != null){
			testFrame.remove(canvas);
			dim = canvas.getSize();
		} else {
			dim = new Dimension(600, 400);
		}
			
		// setup OpenGL version
		GLProfile profile = GLProfile.getMaximum(true);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setRedBits(8);
		capabilities.setBlueBits(8);
		capabilities.setGreenBits(8);
		capabilities.setAlphaBits(8);
		capabilities.setDepthBits(24);

		canvas = new GLCanvas(capabilities);
		canvas.setSize(dim);
		testFrame.add(canvas);

		Object rendererInstance = null;
		Class<?> rendererClass;
		try {
			if (demo.getRendererType() == RendererType.BASIC) {
				rendererInstance = new BasicRenderer(demo.getVertexShaderFileName(), demo.getFragmentShaderFileName());
			} else {
				rendererClass = Class.forName(demo.getDemoPath() + ".Renderer");
				rendererInstance = rendererClass.newInstance();
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		canvas.addGLEventListener((GLEventListener) rendererInstance);
		canvas.addKeyListener((KeyListener) rendererInstance);
		canvas.addKeyListener(keyAdapter);
		canvas.addMouseListener((MouseListener) rendererInstance);
		canvas.addMouseMotionListener((MouseMotionListener) rendererInstance);
		canvas.requestFocus();

		final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
		testFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop();
						System.exit(0);
					}
				}.start();
			}
		});
		
//		testFrame.setTitle(rendererInstance.getClass().getName());

		testFrame.pack();
		testFrame.setVisible(true);
		animator.start(); // start the animation loop
	}

	private void setDemos () {

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}