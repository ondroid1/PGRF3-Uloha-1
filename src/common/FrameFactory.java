package common;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameFactory {

    private static final int FPS = 60; // animator's target frames per second

    public static Frame GetApplicationFrame(BasicRenderer ren) {
        Frame testFrame = new Frame("PGRF3");
        testFrame.setSize(512, 384);

        // setup OpenGL version
        GLProfile profile = GLProfile.getMaximum(true);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas is the widget that's drawn in the JFrame
        GLCanvas canvas = new GLCanvas(capabilities);

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

        animator.start(); // start the animation loop

        testFrame.setTitle(ren.getClass().getName());
        testFrame.pack();
        testFrame.setVisible(true);

        return testFrame;
    }
}
