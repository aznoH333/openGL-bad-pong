public class Ball {
    // variables
    private final float size = 0.1f;
    private final float speed = 0.01f;
    private float xM = speed;
    private float yM = speed;
    private SquareBody body;
    private Paddle paddle;

    public Ball(float x, float y, Paddle p){
        paddle = p;
        body = new SquareBody(x, y, size, size);
    }

    public void render(){
        body.render();
    }

    public void update(){
        //collide with environment
        environmentalCollide();
        paddleCollide();
        body.translate(xM,yM);

    }

    private void environmentalCollide(){
        // y axis
        if (body.getY() > 1 || body.getY()  - body.getHeight() < -1 ) yM *= -1;
        // x axis
        if (body.getX() > 1 - body.getWidth() || body.getX() < -1 ) xM *= -1;
    }

    private void paddleCollide(){
        if (body.collideWith(paddle.getBody())){
            xM = xM*-1;
        }
    }
}
