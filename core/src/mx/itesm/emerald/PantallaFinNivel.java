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

public class PantallaFinNivel extends Pantalla {
    private EdwardsPirateNightmare juego;
    private Texture fondoNivelTerminado;
    private Stage escenaNivelTerminado;
    private Texto texto;
    private int puntos;
    private int numeroMonedas;
    private int pantalla = 1;

    public PantallaFinNivel(EdwardsPirateNightmare juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearMenu();
        recuperarInfoNivel();
        crearTexto();
        Gdx.input.setCatchKey(Input.Keys.BACK, false);
    }

    public void crearMenu() {
        escenaNivelTerminado = new Stage(vista);
        fondoNivelTerminado = new Texture("pantallas/pantallaFinNivelOp.png");

        Button botonLevel2 = crearBoton("botones/button_nivel2.png"); // cargar imágen del botón
        botonLevel2.setPosition(ANCHO / 2, ALTO / 4, Align.center);
        botonLevel2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL_2));
                pantalla += 1;
            }
        });

        Button botonVolverAMenu = crearBoton("botones/botonVolverMenu.png"); // cargar imágen del botón
        botonVolverAMenu.setPosition(0.50f, 0.20f);
        botonVolverAMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        if (pantalla == 1) {
            escenaNivelTerminado.addActor(botonLevel2);
        }
        escenaNivelTerminado.addActor(botonVolverAMenu);
        Gdx.input.setInputProcessor(escenaNivelTerminado);
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }

    private void recuperarInfoNivel() {
        Preferences prefs = Gdx.app.getPreferences("Puntaje");
        puntos = prefs.getInteger("puntos", 0);
        prefs = Gdx.app.getPreferences("Monedas");
        numeroMonedas = prefs.getInteger("contadorMonedas", 0);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0, 1, 0); // borrar pantalla
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(fondoNivelTerminado, 0, 0);
        //escenaNivelTerminado.draw(); // dibujar la escena de juego terminado
        //Decirle al usuario cuántos puntos obtuvo
        texto.mostrarMensaje(batch, "Obtuviste " + Integer.toString(puntos) + " puntos", ANCHO / 2, ALTO / 2);
        texto.mostrarMensaje(batch, "Obtuviste " + Integer.toString(numeroMonedas) + " Monedas", ANCHO / 2, ALTO / 2 - 50);
        batch.end();
        escenaNivelTerminado.draw();
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

    // añadir segundo parámetro para hacer un cambio cuando el usuario da click
    private Button crearBoton(String imagen) {
        // crear otra textura y otro drawable
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture
        return new Button(trdBtnJugar); // regesar imagen normal e imagen de retroalimentacion
    }
}
