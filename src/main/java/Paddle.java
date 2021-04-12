import org.lwjgl.glfw.GLFW;

public class Paddle {
    // variables
    private final float width = 0.1f;
    private final float height = 0.3f;
    private final float speed = 0.01f;
    private float xM = 0;
    private float yM = 0;
    private SquareBody body;

    public Paddle(float x, float y){
        body = new SquareBody(x, y, width, height);
    }

    public void render(){
        body.render();
    }

    public void update(long window){
        updateInput(window);

        body.translate(xM,yM);

    }

    private void updateInput(long window){
        if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            yM = speed;
        } else if(GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            yM = -speed;
        } else {
            yM = 0;
        }
    }

    public SquareBody getBody(){
        return body;
    }
}
