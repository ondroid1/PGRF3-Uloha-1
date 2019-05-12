package task8_vertex_vs_pixel_lighting;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.File;

import com.jogamp.opengl.util.texture.TextureIO;
import common.GridFactory;
import oglutils.OGLBuffers;
import oglutils.OGLTextRenderer;
import oglutils.OGLTexture2D;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import oglutils.ToFloatArray;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

/**
 * GLSL sample:<br/>
 * Draw 3D geometry, use camera and projection transformations<br/>
 * Requires JOGL 2.3.0 or newer
 * 
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2015-09-05
 * 
 * Modified by Vojtech Kalivoda.
 */

public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {

	int width, height, ox, oy;

	OGLBuffers buffers;
	
	OGLTextRenderer textRenderer; // = new OGLTextRenderer();

	
	OGLTexture2D texture;

	int shader, locMat, locTime, locObjectMode, locVisualisationMode, locMoveMode,
	locSpecularMode, locModelViewMat, locReflectorMode, locShaderMode;
	
	float time = 0;
	
	int objectMode = 0, visualisationMode = 0, moveMode = 0, specularMode = 0,
			reflectorMode = 0, shaderMode = 0;

	Camera cam = new Camera();
	Mat4 proj; // created in reshape()
	Mat4 modeView;

	public void init(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		
		OGLUtils.printOGLparameters(gl);
		OGLUtils.shaderCheck(gl);
		
        textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());

		shader = ShaderUtils.loadProgram(gl, "/task8_vertex_vs_pixel_lighting/blinn-phong");

//		buffers = MeshGenerator.createGrid(gl, 100, 100, "inParamPos");
        buffers = GridFactory.generateGrid(gl, 100, 100);

		locMat = gl.glGetUniformLocation(shader, "mat");
		locTime = gl.glGetUniformLocation(shader, "time");
		locObjectMode = gl.glGetUniformLocation(shader, "objectMode");
		locVisualisationMode = gl.glGetUniformLocation(shader, "visualisationMode");
		locMoveMode = gl.glGetUniformLocation(shader, "moveMode");
		locSpecularMode = gl.glGetUniformLocation(shader, "specularMode");
		locModelViewMat = gl.glGetUniformLocation(shader, "modelViewMat");
		locReflectorMode = gl.glGetUniformLocation(shader, "reflectorMode");
		locShaderMode = gl.glGetUniformLocation(shader, "shaderMode");
		
//		texture = TextureIO.newTexture(new File("/textures/bricks.jpg"));
//		texture.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
//		texture.getTexture().setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

		texture = new OGLTexture2D(gl, "/textures/mosaic.jpg");


		cam = cam.withPosition(new Vec3D(5, 5, 2.5))
				.withAzimuth(Math.PI * 1.25)
				.withZenith(Math.PI * -0.125);
		
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}
	
	public void display(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		time += 0.01;
		
		gl.glUseProgram(shader);
		
		gl.glUniformMatrix4fv(locMat, 1, false,
				ToFloatArray.convert(cam.getViewMatrix().mul(proj)), 0);
		
		gl.glUniformMatrix4fv(locModelViewMat, 1, false, ToFloatArray.convert(cam.getViewMatrix()), 0);
		gl.glUniform1f(locTime, time);
		gl.glUniform1i(locObjectMode, objectMode);
		gl.glUniform1i(locVisualisationMode, visualisationMode);
		gl.glUniform1i(locSpecularMode, specularMode);
		gl.glUniform1i(locMoveMode, moveMode);
		gl.glUniform1i(locReflectorMode, reflectorMode);
		gl.glUniform1i(locShaderMode, shaderMode);

		texture.bind(shader, "texture", 0);
		
		buffers.draw(GL2.GL_TRIANGLES, shader);
		
        textRenderer.drawStr2D(3, height - 20, this.getClass().getName());
        textRenderer.drawStr2D(width - 90, 3, " (c) PGRF UHK");
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
		proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		ox = e.getX();
		oy = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		cam = cam.addAzimuth((double) Math.PI * (ox - e.getX()) / width)
				.addZenith((double) Math.PI * (e.getY() - oy) / width);
		ox = e.getX();
		oy = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			cam = cam.forward(1);
			break;
		case KeyEvent.VK_D:
			cam = cam.right(1);
			break;
		case KeyEvent.VK_S:
			cam = cam.backward(1);
			break;
		case KeyEvent.VK_A:
			cam = cam.left(1);
			break;
		case KeyEvent.VK_CONTROL:
			cam = cam.down(1);
			break;
		case KeyEvent.VK_SHIFT:
			cam = cam.up(1);
			break;
		case KeyEvent.VK_SPACE:
			cam = cam.withFirstPerson(!cam.getFirstPerson());
			break;
		case KeyEvent.VK_R:
			cam = cam.mulRadius(0.9f);
			break;
		case KeyEvent.VK_F:
			cam = cam.mulRadius(1.1f);
			break;
		case KeyEvent.VK_1:
			if(objectMode == 2) objectMode = 0;
			else objectMode++;
			break;
		case KeyEvent.VK_2:
			if(visualisationMode == 2) visualisationMode = 0;
			else visualisationMode++;
			break;
		case KeyEvent.VK_3:
			if(moveMode == 1) moveMode = 0;
			else moveMode++;
			break;
		case KeyEvent.VK_4:
			if(specularMode == 3) specularMode = 0;
			else specularMode++;
			break;
		case KeyEvent.VK_5:
			if(reflectorMode == 1) reflectorMode = 0;
			else reflectorMode++;
			break;
		case KeyEvent.VK_6:
			if(shaderMode == 1) shaderMode = 0;
			else shaderMode++;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void dispose(GLAutoDrawable glDrawable) {
		GL2 gl = glDrawable.getGL().getGL2();
		gl.glDeleteProgram(shader);
	}

}