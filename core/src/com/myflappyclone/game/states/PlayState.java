package com.myflappyclone.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.myflappyclone.game.MyFlappyClone;
import com.myflappyclone.game.sprites.Bird;
import com.myflappyclone.game.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -60;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Texture gameoverImg;
    private Vector2 groundPos1, groundPos2;
    private boolean gameOver;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(60, 300);
        cam.setToOrtho(false, MyFlappyClone.WIDTH / 2, MyFlappyClone.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        gameoverImg = new Texture("gameover.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);
        tubes = new Array<Tube>();

        for(int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
        gameOver = false;
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            if(gameOver){
                gsm.set(new PlayState(gsm));
            } else {
                bird.jump();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;
        for(Tube tube : tubes) {
            if(cam.position.x - (cam.viewportWidth /2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if(tube.collides(bird.getBounds())) {
//                gsm.set(new PlayState(gsm));
                bird.colliding = true;
                gameOver = true;
                break;
            }
        }
        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET){
//            gsm.set(new PlayState(gsm));
            bird.colliding = true;
            gameOver = true;
        }
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y);
        for(Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        if(gameOver)
            sb.draw(gameoverImg, cam.position.x - gameoverImg.getWidth() / 2, cam.position.y);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for(Tube tube : tubes)
            tube.dispose();
        System.out.println("Play State Disposed");
    }

    private void updateGround() {
        if((cam.position.x - cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if((cam.position.x - cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }
}
