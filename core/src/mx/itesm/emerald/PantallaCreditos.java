package mx.itesm.emerald;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

// Muestra la pantalla de los créditos
public class PantallaCreditos extends Pantalla {

    private Texture texturaCreditos;
    private Texture texturaVolver;
    private Stage escenaCreditos;

    public PantallaCreditos(final EdwardsPirateNightmare juego) {

        texturaCreditos = new Texture("pantallas/creditos.png");

        escenaCreditos = new Stage(vista); // vista como paramétro para que se escalen correctamente

    // crea el boton de volver y lo agrega a una escena

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
        escenaCreditos.addActor(botonVolver);
        Gdx.input.setInputProcessor(escenaCreditos);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(texturaCreditos,0,0);
        batch.end();
        escenaCreditos.draw();

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
