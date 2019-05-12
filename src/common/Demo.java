package common;

public class Demo {

    String demoName;
    String demoPath;
    RendererType rendererType;
    String vertexShaderFileName;
    String fragmentShaderFileName;


    public Demo (String demoName, String demoPath, RendererType rendererType, String vertexShaderFileName, String fragmentShaderFileName) {
        this.demoName = demoName;
        this.demoPath = demoPath;
        this.rendererType = rendererType;
        this.vertexShaderFileName = vertexShaderFileName;
        this.fragmentShaderFileName = fragmentShaderFileName;
    }


    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }

    public String getDemoName() {
        return demoName;
    }

    public void setDemoPath(String demoPath) {
        this.demoPath = demoPath;
    }

    public String getDemoPath() {
        return demoPath;
    }

    public void setRendererType(RendererType rendererType) {
        this.rendererType = rendererType;
    }

    public RendererType getRendererType() {
        return rendererType;
    }

    public void setVertexShaderFileName(String vertexShaderFileName) {
        this.vertexShaderFileName = vertexShaderFileName;
    }

    public String getVertexShaderFileName() {
        return vertexShaderFileName;
    }

    public void setFragmentShaderFileName(String fragmentShaderFileName) {
        this.fragmentShaderFileName = fragmentShaderFileName;
    }

    public String getFragmentShaderFileName() {
        return fragmentShaderFileName;
    }
}
