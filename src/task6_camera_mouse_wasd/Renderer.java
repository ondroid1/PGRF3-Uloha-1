package task6_camera_mouse_wasd;

import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import common.GridFactory;
import oglutils.OGLBuffers;
import oglutils.OGLTextRenderer;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import java.awt.event.*;

/**
 * GLSL sample:<br/>
 * Read and compile shader from files "/shader/glsl01/start.*" using ShaderUtils
 * class in oglutils package (older GLSL syntax can be seen in
 * "/shader/glsl01/startForOlderGLSL")<br/>
 * Manage (create, bind, draw) vertex and index buffers using OGLBuffers class
 * in oglutils package<br/>
 * Requires JOGL 2.3.0 or newer
 * 
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-05
 */
public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	int width, height;

	OGLBuffers buffers;
	OGLTextRenderer textRenderer;

	int shaderProgram, locView, locProj;

	private Mat4 proj;
	private Camera camera;
	private int mx, my;

	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		OGLUtils.shaderCheck(gl);
		OGLUtils.printOGLparameters(gl);
		
		textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());
		
		shaderProgram = ShaderUtils.loadProgram(gl, "/task6_camera_mouse_wasd/start.vert",
				"/task6_camera_mouse_wasd/start.frag",
				null,null,null,null); 

		buffers = GridFactory.generateGrid(gl, 100, 100);

		camera = new Camera()
				.withPosition(new Vec3D(0,0, 0))
				.addAzimuth(5 / 4. * Math.PI)
				.addZenith(-1 / 5. * Math.PI)
				.withFirstPerson(false)
				.withRadius(5);

		locProj = gl.glGetUniformLocation(shaderProgram, "proj");
		locView = gl.glGetUniformLocation(shaderProgram, "view");
	}

	@Override
	public void display(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);
		
		// set the current shader to be used, could have been done only once (in
		// init) in this sample (only one shader used)
		gl.glUseProgram(shaderProgram); 

		gl.glUniformMatrix4fv(locView, 1, false, camera.getViewMatrix().floatArray(), 0);
		gl.glUniformMatrix4fv(locProj, 1, false, proj.floatArray(), 0);
		
		// bind and draw
		buffers.draw(GL2GL3.GL_TRIANGLES, shaderProgram);
		
		String text = this.getClass().getName();
		textRenderer.drawStr2D(3, height - 20, text);
		textRenderer.drawStr2D(width - 90, 3, " (c) PGRF UHK");

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
		proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);

		textRenderer.updateSize(width, height);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		camera = camera.addAzimuth(Math.PI * (mx - e.getX()) / width);
		camera = camera.addZenith(Math.PI * (e.getY() - my) / width);
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

		switch(e.getKeyChar()) {
			case 'w': // nahoru
				camera = camera.up(0.1);
				break;
			case 'a': // doleva
				camera = camera.left(0.1);
				break;
			case 's': // dolů
				camera = camera.down(0.1);
				break;
			case 'd': // doprava
				camera = camera.right(0.1);
				break;
		}
	}

	@Override
	public void dispose(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		gl.glDeleteProgram(shaderProgram);
	}

}