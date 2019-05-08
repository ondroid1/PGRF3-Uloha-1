import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.SwingUtilities;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second
	private GLCanvas canvas = null;
	private Frame applicationFrame;
	private int demoId = 1;
	static int[] countMenuItems = {15,20};

	private KeyAdapter keyAdapter;

	public void start() {
		try {
			applicationFrame = new Frame("PGRF3 - Úloha 1 - Ondřej Stieber");
			applicationFrame.setSize(512, 384);
			
			System.out.println("searching all samples");

			String[] names = getDemoNames("", "\\Renderer.class"); // comment this to have only the samples listed in names
			makeGUI(applicationFrame, names);

			setApp(applicationFrame, names[0]);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String[] getDemoNames(String beginingPath, String nameApp) {
		Object[] paths;
		try {
			paths = Files.find(
					Paths.get(""),
					Integer.MAX_VALUE,
					(filePath, fileAttr) -> (filePath.toString()
							.lastIndexOf(nameApp)) > 0).toArray();
			List<String> pathList = new ArrayList<>();
			for (int i = 0; i < paths.length; i++) {
				Path path = (Path) paths[i];
				String s = path.toString();
				
				int indexS =  s.indexOf(beginingPath);
				if (indexS < 0) continue;
				int indexE =  s.lastIndexOf("\\");
				if (indexE < 1) continue;
				
				s = s.substring(indexS, indexE).trim().replace("\\", ".");
				pathList.add(s);
			}
			Collections.sort(pathList);

			//to find groups of samples
			HashMap<String, Integer> mapGroups = new HashMap<>();
			for (int i = 0; i < pathList.size(); i++) {
				String s = pathList.get(i);
				System.out.println(s);
				int index =  s.indexOf(".");
				if (index < 1) continue;
				String group = s.substring(0,index).trim();
				if (mapGroups.containsKey(group))
					mapGroups.put(group, mapGroups.get(group) + 1);
				else
					mapGroups.put(group, 1);
			}

			String[] names = new String[pathList.size()];
			int iName = 0;
			for (Object name : pathList.toArray())
				names[iName++] = (String) name;
			
			countMenuItems = new int[mapGroups.size()];

			SortedSet<String> keys = new TreeSet<String>(mapGroups.keySet());
			int iKey = 0;
			for (String key : keys) {
				countMenuItems[iKey] = mapGroups.get(key);
				// System.out.println(nameMenuItem[iKey]+countMenuItems[iKey]);
				iKey++;
			}

			return names;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	private void makeGUI(Frame testFrame, String[] rendererClassNames) {
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				demoId = Integer
						.valueOf(ae.getActionCommand().substring(0, ae.getActionCommand().lastIndexOf('-') - 1).trim());
				setApp(testFrame, rendererClassNames[demoId - 1]);
			}
		};

		MenuBar menuBar = new MenuBar();
		int menuIndex = 0;
		for(int itemMenu = 0 ; itemMenu < 1; itemMenu++){
			Menu menu1 = new Menu("Ukázka");
			MenuItem m;
			for (int i = 0; i < rendererClassNames.length && i < countMenuItems[itemMenu]; i++) {
				m = new MenuItem(new Integer(menuIndex + 1).toString() + " - "
						+ rendererClassNames[menuIndex]);
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
					setApp(testFrame, rendererClassNames[demoId - 1]);

					break;
				case KeyEvent.VK_END:
					demoId = rendererClassNames.length;
					setApp(testFrame, rendererClassNames[demoId - 1]);
					break;
				case KeyEvent.VK_LEFT:
					if (demoId > 1)
						demoId--;
					setApp(testFrame, rendererClassNames[demoId - 1]);
					break;
				case KeyEvent.VK_RIGHT:
					if (demoId < rendererClassNames.length)
						demoId++;
					setApp(testFrame, rendererClassNames[demoId - 1]);
					break;
				}
			}

		};

		testFrame.setMenuBar(menuBar);
	}

	private void setApp(Frame testFrame, String name) {
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
			rendererClass = Class.forName(name + ".Renderer");
			rendererInstance = rendererClass.newInstance();
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
		
		testFrame.setTitle(rendererInstance.getClass().getName());

		testFrame.pack();
		testFrame.setVisible(true);
		animator.start(); // start the animation loop
}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JOGLApp().start());
	}

}