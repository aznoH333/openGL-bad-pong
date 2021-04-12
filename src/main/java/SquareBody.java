import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SquareBody {

    private float[] vertices;

    private final int[] indices = {
            0, 1, 3, // First triangle
            0, 2, 3 // Second triangle
    };

    private int squareVaoId;
    private int squareVboId;
    private int squareEboId;
    private int uniformColorLocation;
    private int uniformTransformLocation;

    private float x;
    private float y;
    private float width;
    private float height;

    private  Matrix4f matrix = new Matrix4f()
            .identity()
            .translate(0f, 0f, 0f);
    // 4x4 -> FloatBuffer of size 16
    private FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

    private float[] transform = {
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
    };

    public SquareBody(float x,float y,float width,float height){
        vertices = new float[12];
        //set verticies
        for(int it = 0;it < 4;it++){
            vertices[it*3] = x+width*(it%2);
            vertices[it*3+1] = y+height*(Math.round(it/2));
            vertices[it*3+2] = 0.0f;
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Setup shaders
        Shaders.initShaders();

        // Get uniform location
        uniformColorLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "outColor");
        uniformTransformLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "transform");

        // Generate all the ids
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();

        // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindVertexArray(squareVaoId);

        // Tell OpenGL we are currently writing to this buffer (eboId)
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        // Change to VBOs...
        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(fb);

        // Change to Color...
        // Tell OpenGL we are currently writing to this buffer (colorsId)
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glUniform3f(uniformColorLocation, 1.0f, 1.0f, 1.0f);

        // Sending Mat4 to GPU
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformTransformLocation, false, matrixFloatBuffer);
    }

    public void render(){
        GL33.glUseProgram(Shaders.shaderProgramId);

        // Draw using the glDrawElements function
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public void update(){

    }

    public void translate(float addX, float addY){
        GL33.glBindVertexArray(squareVaoId);
        // Sending Mat4 to GPU
        x += addX;
        y -= addY;
        matrix.translate(addX, addY, 0);
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformTransformLocation, false, matrixFloatBuffer);
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }

    //lmfao Xtrem jank
    public boolean collideWith(SquareBody other){
        return x+width > other.getX() && x < other.getX()+ other.getWidth() && y+height*3 > other.getY() && y < other.getY()+ other.getHeight()/3;
    }

}
