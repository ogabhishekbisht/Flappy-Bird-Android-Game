package com.ogabhishekbisht.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture gameOver;
	//ShapeRenderer shapeRenderer ;
	int Score;
	BitmapFont font;
	int  scoringTube = 0;
	Circle birdCircle ;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
	Texture tube1;
	float tubeVelocity = 6;
	Texture tube2;
	float gap = 400;
	float maxTubeOfset;
	Random randomgenerator;


	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOfset = new float[numberOfTubes];
	float distanceBetweenTubes;

    public void startGame() {

        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight();

        for(int i = 0; i < numberOfTubes; i++){
            tubeOfset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap/2 - 400);
            tubeX[i] = Gdx.graphics.getWidth()/2-tube1.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfset[i], tube1.getWidth(),tube1.getHeight());
            bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tube2.getHeight() + tubeOfset[i], tube2.getWidth(),tube2.getHeight());

        }

    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		gameOver = new Texture("flappygo.png");
		font.getData().setScale(10);
		//shapeRenderer = new ShapeRenderer();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		background = new Texture("bg.png");
		birds = new Texture[2];
		birdCircle = new Circle();
		Score = 0;
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		tube1 = new Texture("toptube.png");
		tube2 = new Texture("bottomtube.png");

		maxTubeOfset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomgenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 /4;
        startGame();

		//tubeX = Gdx.graphics.getWidth()/2-tube1.getWidth()/2;
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if(gameState == 1) {


			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2 ) {
				Score++;
				if(scoringTube < numberOfTubes - 1){
					scoringTube++;
				}
				else{
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()){
				gameState = 1;
				velocity = -30;

			}
			for(int i = 0; i < numberOfTubes; i++) {

				if(tubeX[i] < -tube1.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOfset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap/2 - 400);
					topTubeRectangles[i] = new Rectangle();
					bottomTubeRectangles[i] = new Rectangle();
				}
				else
				{
					tubeX[i] -= tubeVelocity;

				}

				batch.draw(tube1, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfset[i]);
				batch.draw(tube2, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - tube2.getHeight() + tubeOfset[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfset[i], tube1.getWidth(),tube1.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tube2.getHeight() + tubeOfset[i], tube2.getWidth(),tube2.getHeight());


			}
			if(birdY > 0) {
				velocity = velocity + gravity;
				birdY = birdY - velocity;
			}
			else{
				gameState = 2;
			}

		}
		else if(gameState == 0) {

			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if(gameState == 2){

			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getWidth()/2);
			if(Gdx.input.justTouched()) {
				gameState = 1;
				Score = 0;
				scoringTube = 0;
				velocity = 0;
				startGame();
			}
		}
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}

		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth(), birdY);
		//	batch.draw(bird2,Gdx.graphics.getWidth()/2 - bird.getWidth(),Gdx.graphics.getHeight()/2 - bird.getHeight());

		font.draw(batch, String.valueOf(Score),Gdx.graphics.getWidth() /2  - birds[flapstate].getWidth()/2,Gdx.graphics.getHeight()*8/9);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2, birdY + birds[flapstate].getHeight()/2, birds[flapstate].getWidth()/2 );

	//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

	//	shapeRenderer.setColor(Color.RED);
	//	shapeRenderer.circle(birdCircle.x, birdCircle.y,birdCircle.radius);
		for(int i = 0; i < numberOfTubes; i++) {
		// shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOfset[i], tube1.getWidth(),tube1.getHeight());
		// shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - tube2.getHeight() + tubeOfset[i], tube2.getWidth(),tube2.getHeight());
		if(Intersector.overlaps(birdCircle,topTubeRectangles[i])) {
			gameState = 2;
		}
		if(Intersector.overlaps(birdCircle,bottomTubeRectangles[i])) {
			gameState = 2;
		}

		}

	//	shapeRenderer.end();


	}
	


}
