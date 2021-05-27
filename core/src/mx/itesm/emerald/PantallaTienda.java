package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.itesm.emerald.edwardspiratenightmare.utilities.Texto;

public class PantallaTienda extends Pantalla {
    private Texture tienda;
    private Texture texturaVolver;
    private Stage escenaTienda;
    private Texto texto;
    private int cantidadMonedas;
    private Texture texturaMoneda;
    private Moneda iconoMoneda;

    private EscenaCorazon escenaCorazon;


    public PantallaTienda(final EdwardsPirateNightmare juego) {
        tienda = new Texture("pantallas/empty store.png");
        escenaTienda = new Stage(vista); // vista como paramétro para que se escalen correctamente

        texturaVolver = new Texture("botones/botonvolverS.png");
        TextureRegionDrawable trdBtnVolver = new TextureRegionDrawable(texturaVolver);
        Button botonVolver = new Button(trdBtnVolver);
        botonVolver.setPosition(0, 0);
        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar pantalla
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaTienda.addActor(botonVolver);
        Gdx.input.setInputProcessor(escenaTienda);
    }

    @Override
    public void show() {
        recuperarMonedas();
        crearTexto();
        crearIconoMoneda();
        crearBotones();
        Gdx.input.setCatchKey(Input.Keys.BACK, false);

    }



    private void crearIconoMoneda() {
        texturaMoneda = new Texture ("sprites/coin.png");

        iconoMoneda = new Moneda(texturaMoneda, ANCHO -200, ALTO - 100);
        iconoMoneda.sprite.setScale(2);
    }


    private void crearBotones(){
        Button btnBandana = crearBoton("sprites/bandana.png");
        btnBandana.setTransform(true);
        btnBandana.scaleBy(2);

        btnBandana.setPosition(ANCHO / 2,  ALTO * 3/4, Align.center); // centrar el botòn en las coordenadas selecconadas

        Button btnCorazon = crearBoton("sprites/heart.png");
        btnCorazon.setTransform(true);
        btnCorazon.scaleBy(2);
        btnCorazon.setPosition(ANCHO / 2,  ALTO * 1/4, Align.center); // centrar el botòn en las coordenadas selecconadas
        btnCorazon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               escenaCorazon = new EscenaCorazon(vista);
            }
        });
        escenaTienda.addActor(btnBandana);
        escenaTienda.addActor(btnCorazon);

    }
    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }

    private void recuperarMonedas() {
        Preferences prefs = Gdx.app.getPreferences("Monedas"); // recuperar cantidad monedas
        cantidadMonedas = prefs.getInteger("contadorMonedas",0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(tienda, 0, 0);
        iconoMoneda.render(batch);
        // Mostrar bandana, precio y descripción
        texto.mostrarMensaje(batch, Integer.toString(cantidadMonedas), ANCHO -100, ALTO - 50);
        texto.mostrarMensaje(batch, "Multiplica tus puntos por dos", ANCHO/2, ALTO - 225);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO - 180);

        // Mostrar corazon, precio y descripción

        texto.mostrarMensaje(batch, "Inicias con un punto de vida extra", ANCHO/2, ALTO - 525);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO*1/8);

        batch.end();
        escenaTienda.draw();
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

    //método para simplificiar la creación de botones
    private Button crearBoton(String imagen) { // añadir segundo parámetro para hacer un cambio cuando el usuario da click
        // crear otra textura y otro drawable
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture



        return new Button(trdBtnJugar); // regesar imagen normal e imagen de retroalimentacion
    }

    private class EscenaCorazon extends Stage {
        public EscenaCorazon(final Viewport vista){


        }

    }
}




