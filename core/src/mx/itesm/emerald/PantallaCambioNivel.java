package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import mx.itesm.emerald.edwardspiratenightmare.utilities.Texto;

public class PantallaCambioNivel extends Pantalla
{

    private EdwardsPirateNightmare juego;
    private Texture fondoCambio;
    private Texto texto;
    private Stage escenaCambio;

    public PantallaCambioNivel (EdwardsPirateNightmare juego)
    {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearBotonContinuar();
        crearTexto();
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }

    private void crearBotonContinuar() {
        escenaCambio = new Stage(vista);
        fondoCambio = new Texture("Cambio/fondoCambio.png");

        Button botonCambio = crearBoton("pausa/botonContinuar.png"); // cargar imágen del botón
        botonCambio.setPosition(ANCHO / 2, ALTO*0.3f, Align.center);
        botonCambio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL_2));
            }
        });
        escenaCambio.addActor(botonCambio);

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(fondoCambio,0,0);
        //texto.mostrarMensaje(batch,"Sigue tu camino...", ANCHO/2, ALTO/2);
        batch.end();
        escenaCambio.draw();
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

    private Button crearBoton(String imagen) { // añadir segundo parámetro para hacer un cambio cuando el usuario da click
        // crear otra textura y otro drawable
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture
        return new Button(trdBtnJugar); // regesar imagen normal e imagen de retroalimentacion
    }
}
