package common;

import com.jogamp.opengl.GL2GL3;
import oglutils.OGLBuffers;

/**
 * Třída pro vytváření gridů složených z trojůhelníků
 */
public class GridFactory {


    /**
     * @param gl
     * @param m počet vrcholů v řádku
     * @param n počet vrcholů ve sloupci
     * @return
     */
    public static OGLBuffers generateGrid (GL2GL3 gl, int m, int n) {
        float[] vb = new float[m * n * 2];
        int index = 0;

        for (int j = 0; j < n; j++) {
            float y = j / (float) (n - 1);
            for (int i = 0; i < m; i++) {
                float x = i / (float)(m - 1);
                vb[index++] = x;
                vb[index++] = y;
            }
        }

        int[] ib = new int[(m - 1) * (n - 1) * 2 * 3];
        int index2 = 0;

        for (int r = 0; r < n - 1; r++) {
            for (int c = 0; c < m - 1; c++) {
                ib[index2++] = r * m + c;
                ib[index2++] = r * m + c + 1;
                ib[index2++] = r * m + c + m;

                ib[index2++] = r * m + c + m;
                ib[index2++] = r * m + c + 1;
                ib[index2++] = r * m + c + m + 1;
            }
        }

        OGLBuffers.Attrib[] attributes = {
            new OGLBuffers.Attrib("inPosition", 2)
        };

        return new OGLBuffers(gl, vb, attributes, ib);
    }
}
