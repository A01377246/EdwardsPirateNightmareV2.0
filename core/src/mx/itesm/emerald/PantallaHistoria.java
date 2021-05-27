package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;



public class PantallaHistoria extends Pantalla {
    private Texture historia;
    private Texture texturaVolver;
    private Stage escenaHistoria;

    public PantallaHistoria(final EdwardsPirateNightmare juego) {


        historia = new Texture("pantallas/historia.png");
        escenaHistoria = new Stage(vista); // vista como paramétro para que se escalen correctamente

        texturaVolver = new Texture("botones/botonvolverS.png");
        TextureRegionDrawable trdBtnVolver = new TextureRegionDrawable(texturaVolver);
        Button botonVolver = new Button(trdBtnVolver);
        botonVolver.setPosition(0,0);
        botonVolver.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar pantalla
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaHistoria.addActor(botonVolver);
        Gdx.input.setInputProcessor(escenaHistoria);

    }

    @Override
    public void show() {
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(historia,0,0);
        batch.end();

        escenaHistoria.draw();


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}


