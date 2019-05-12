package common;

import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import oglutils.OGLBuffers;
import oglutils.OGLTextRenderer;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import java.awt.event.*;

public class BasicRenderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	private String vertexShaderFileName, fragmentShaderFileName;

	int width, height;

	OGLBuffers buffers;
	OGLTextRenderer textRenderer;

	int shaderProgram, locView, locProj;

	private Mat4 proj;
	private Camera camera;

	public BasicRenderer(String vertexShaderFileName, String fragmentShaderFileName) {
		this.vertexShaderFileName = vertexShaderFileName;
		this.fragmentShaderFileName = fragmentShaderFileName;
	}

	@Override
	public void init(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		OGLUtils.shaderCheck(gl);
		OGLUtils.printOGLparameters(gl);
		
		textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());
		
		shaderProgram = ShaderUtils.loadProgram(gl, vertexShaderFileName, fragmentShaderFileName,
				null,null,null,null); 

		buffers = GridFactory.generateGrid(gl, 100, 100);

		camera = new Camera()
				.withPosition(new Vec3D(10,10, 10))
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
	}

	@Override
	public void dispose(GLAutoDrawable glDrawable) {
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		gl.glDeleteProgram(shaderProgram);
	}

}