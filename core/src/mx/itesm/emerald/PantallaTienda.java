package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import mx.itesm.emerald.edwardspiratenightmare.utilities.Texto;

public class PantallaTienda extends Pantalla {
    private Texture tienda;
    private Texture texturaVolver;
    private Stage escenaTienda;
    private Texto texto;
    private int cantidadMonedas;
    private Texture texturaMoneda;
    private Moneda iconoMoneda;

    //Objetos que se venden en la tienda

    private Corazon objetoCorazon;
    private Texture texturaCorazon;
    private Bandana objetoBandana;
    private Texture texturaBandana;

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
        crearBandana();
        crearCorazon();

    }

    private void crearCorazon() {
        texturaCorazon= new Texture ("sprites/heart.png");

        objetoCorazon= new Corazon(texturaCorazon, ANCHO/2, ALTO - 450);
        objetoCorazon.sprite.setScale(2.5f);

    }

    private void crearBandana() {
        texturaBandana = new Texture ("sprites/bandana.png");

        objetoBandana = new Bandana(texturaBandana, ANCHO/2, ALTO - 200);
        objetoBandana.sprite.setScale(4);
    }

    private void crearIconoMoneda() {
        texturaMoneda = new Texture ("sprites/coin.png");

        iconoMoneda = new Moneda(texturaMoneda, ANCHO -200, ALTO - 100);
        iconoMoneda.sprite.setScale(2);
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
        objetoBandana.render(batch);
        texto.mostrarMensaje(batch, Integer.toString(cantidadMonedas), ANCHO -100, ALTO - 50);
        texto.mostrarMensaje(batch, "Multiplica tus puntos por dos", ANCHO/2, ALTO - 225);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO - 180);

        // Mostrar corazon, precio y descripción

        objetoCorazon.render(batch);
        texto.mostrarMensaje(batch, "Inicias con un punto de vida extra", ANCHO/2, ALTO - 525);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO - 480);

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


}




