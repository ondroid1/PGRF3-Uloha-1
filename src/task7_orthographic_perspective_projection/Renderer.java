package task7_orthographic_perspective_projection;

import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import common.GridFactory;
import oglutils.OGLBuffers;
import oglutils.OGLTextRenderer;
import oglutils.OGLUtils;
import oglutils.ShaderUtils;
import transforms.*;

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

    int shaderProgram, locView, locProj, locTime;

    private float time = 0;
    private ProjectionType projectionType = ProjectionType.PERSPECTIVE;

    private Mat4 proj;
    private Camera camera;

    @Override
    public void init(GLAutoDrawable glDrawable) {
        GL2GL3 gl = glDrawable.getGL().getGL2GL3();
        OGLUtils.shaderCheck(gl);
        OGLUtils.printOGLparameters(gl);

        textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());

        shaderProgram = ShaderUtils.loadProgram(gl, "/task7_orthographic_perspective_projection/start.vert",
                "/task7_orthographic_perspective_projection/start.frag",
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
        locTime = gl.glGetUniformLocation(shaderProgram, "time");
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        GL2GL3 gl = glDrawable.getGL().getGL2GL3();

        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);

        // set the current shader to be used, could have been done only once (in
        // init) in this sample (only one shader used)
        gl.glUseProgram(shaderProgram);

        time += 0.05;
        gl.glUniform1f(locTime, time);

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

        if (projectionType == ProjectionType.PERSPECTIVE) {
            proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
        } else {
            proj = new Mat4OrthoRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
        }

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
        if (projectionType == ProjectionType.PERSPECTIVE) {
            projectionType = ProjectionType.ORTHOGRAPHIC;
        } else {
            projectionType = ProjectionType.PERSPECTIVE;
        }
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

enum ProjectionType {
    ORTHOGRAPHIC, PERSPECTIVE
}