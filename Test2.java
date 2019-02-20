/*
    TESTFIL: Laddar en gubbe (png-bild) på en karta (png-bild). Varje gång gubben rör sig så printas färgen där han befinner sig.
    Detta sker med hjälp av klassen PixelReader, som läser en pixel gubben befinner sig i!
 */

package project;


import javafx.animation.AnimationTimer;
        import javafx.application.Application;
        import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.*;
        import javafx.scene.image.*;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.paint.Color;
        import javafx.stage.Stage;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class Test2 extends Application {

    private static final double W = 1400, H = 800;

    private static final String HERO_IMAGE_LOC = "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";



    private Image heroImage;
    private Node  hero;

    PixelReader pixelReader;



    boolean running, goNorth, goSouth, goEast, goWest;

    @Override
    public void start(Stage stage) throws Exception {
        heroImage = new Image(HERO_IMAGE_LOC);
        hero = new ImageView(heroImage);
        Image background = new Image("https://i.imgur.com/ptr3zBi.png");
        ImageView bgView = new ImageView(background);

        pixelReader = background.getPixelReader();

        Group dungeon = new Group();

        moveHeroTo(W / 2, H / 2);

        Scene scene = new Scene(dungeon, W, H);


        dungeon.getChildren().addAll(bgView, hero);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                    case SHIFT: running = true; break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;
                    case SHIFT: running = false; break;
                }
            }
        });

        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int dx = 0, dy = 0;

                if (goNorth) dy -= 1;
                if (goSouth) dy += 1;
                if (goEast)  dx += 1;
                if (goWest)  dx -= 1;
                if (running) { dx *= 3; dy *= 3; }

                moveHeroBy(dx, dy);
            }
        };
        timer.start();
    }

    private void moveHeroBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        double x = cx + hero.getLayoutX() + dx;
        double y = cy + hero.getLayoutY() + dy;

        moveHeroTo(x, y);
    }

    private void moveHeroTo(double x, double y) {
        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        if (x - cx >= 0 &&
                x + cx <= W &&
                y - cy >= 0 &&
                y + cy <= H) {
            hero.relocate(x - cx, y - cy);
        }
        readPixel(x, y);
    }

    public static void main(String[] args) { launch(args); }

    public void readPixel(double x, double y){
        Color color = pixelReader.getColor((int)x, (int)y);

        //System.out.println(color.toString());
        if(color.toString().equals("0x22b14cff")){
            System.out.println("grön");
        }
        if(color.toString().equals("0xed1c24ff")){
            System.out.println("röd");
        }
        if(color.toString().equals("0x00a2e8ff")){
            System.out.println("blå");
        }
    }
}
