package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import mx.itesm.emerald.edwardspiratenightmare.utilities.Texto;

public class PantallaFinNivel extends Pantalla {
    private Stage escenaNivelTerminado;
    private Texture fondoNivelTerminado;
    private int puntos;
    private Texto texto;
    private int numeroMonedas;

    public PantallaFinNivel(final EdwardsPirateNightmare juego) {
        escenaNivelTerminado = new Stage(vista);
        fondoNivelTerminado = new Texture("pantallas/pantallaFinNivelOp.png");
        Button botonVolverAMenu = crearBoton("botones/botonVolverMenu.png"); // cargar imágen del botón
        botonVolverAMenu.setPosition(0, 25);
        botonVolverAMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a pantalla de juego terminado
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaNivelTerminado.addActor(botonVolverAMenu); // añadir boton a escena
        Gdx.input.setInputProcessor(escenaNivelTerminado); // la escena juego se encarga de registar el input

    }



    @Override
    public void show() {
        recuperarInfoNivel();
        crearTexto();

    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");

    }

    private void recuperarInfoNivel() {
        Preferences prefs = Gdx.app.getPreferences("Puntaje");
        puntos = prefs.getInteger("puntos",0);
        prefs = Gdx.app.getPreferences("Monedas");
        numeroMonedas = prefs.getInteger("contadorMonedas",0);

    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(fondoNivelTerminado,0,0);
        escenaNivelTerminado.draw(); // dibujar la escena de juego terminado

        //Decirle al usuario cuántos puntos obtuvo

        texto.mostrarMensaje(batch, "Obtuviste " + Integer.toString(puntos) + " puntos", ANCHO/2 , ALTO/2 );
        texto.mostrarMensaje(batch, "Obtuviste " + Integer.toString(numeroMonedas) + " Monedas", ANCHO/2, ALTO/2 -50);

        batch.end();




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
