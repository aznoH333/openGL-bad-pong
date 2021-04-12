public class Game {



    static Paddle p = new Paddle(-0.9f,0);
    static Ball b = new Ball(0,0,p);

    public static void init(long window) {

    }

    public static void render(long window) {
        b.update();
        b.render();

        p.update(window);
        p.render();
    }

    public static void update(long window) {



    }

}