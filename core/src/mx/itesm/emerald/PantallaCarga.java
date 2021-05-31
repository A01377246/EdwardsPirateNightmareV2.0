package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

//Esta clase representa la pantalla de carga para el nivel 1

public class PantallaCarga extends Pantalla {

    private final EdwardsPirateNightmare juego;
    private AssetManager assetManager;

    //Imagen Cargando
    private Texture texturaCargando;
    private Sprite spriteCargando;
    private Pantallas siguientePantalla;


    public PantallaCarga(EdwardsPirateNightmare juego, Pantallas siguientePantalla) {
        assetManager = juego.getAssetManager(); // obtener asset Manager
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;

    }

    @Override
    public void show() {
        //Cargar recurso para el ícono de la pantalla de carga
        assetManager.load("pantallaCarga/iconoCargaTimon.png", Texture.class);
        assetManager.finishLoading();
        texturaCargando = assetManager.get("pantallaCarga/iconoCargaTimon.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO / 2 - spriteCargando.getWidth() / 2, ALTO / 2 - spriteCargando.getHeight() / 2);
        cargarSiguientePantalla(); // cargar recursos
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    private void cargarRecursosNivel1() {
        //cargar sprites
        assetManager.load("sprites/pauseButton.png", Texture.class);
        assetManager.load("sprites/edwardRun.png", Texture.class);
        assetManager.load("sprites/coin.png", Texture.class);
        assetManager.load("sprites/bala.png", Texture.class);
        assetManager.load("sprites/heart.png", Texture.class);
        assetManager.load("sprites/fantasma1/fantasmaAnimacion.png", Texture.class);
        assetManager.load("sprites/soloHeart.png", Texture.class);
        assetManager.load("sprites/Escudo.png", Texture.class);
        //Cargar pantallas
        assetManager.load("pantallas/pantallaFinNivelOp.png", Texture.class);
        assetManager.load("pantallas/p1GameOverAlt.png", Texture.class);
        assetManager.load("pantallas/P1.png", Texture.class);
        //cargar botones
        assetManager.load("pausa/botonContinuar.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("pausa/fondoPPausa.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("botones/botonPausa.png", Texture.class);
        //Cargar efectos de Sonido
        assetManager.load("sonidos/moneda.wav", Sound.class);
        assetManager.load("sonidos/ghost.wav", Sound.class);
        assetManager.load("sonidos/hurt.wav", Sound.class);
        assetManager.load("sonidos/jump.wav", Sound.class);
        assetManager.load("sonidos/laserpew.wav", Sound.class);
        //cargar Audio

    }

    private void cargarSiguientePantalla() {
        switch (siguientePantalla) {
            case NIVEL_1:
                cargarRecursosNivel1();
            case NIVEL_2:
                cargarRecursosNivel2();
        }
    }


    private void cargarRecursosNivel2() {
        assetManager.load("sprites/pauseButton.png", Texture.class);
        assetManager.load("sprites/edwardRun.png", Texture.class);
        assetManager.load("sprites/coin.png", Texture.class);
        assetManager.load("sprites/Bala_Plasma.png", Texture.class);
        assetManager.load("sprites/heart.png", Texture.class);
        assetManager.load("sprites/fantasma1/fantasmaAnimacion.png", Texture.class);
        assetManager.load("sprites/fantasma2/fantasma2Animacion.png", Texture.class);
        assetManager.load("sprites/soloHeart.png", Texture.class);
        //cargar pantallas
        assetManager.load("pantallas/pantallaFinNivelOp.png", Texture.class);
        assetManager.load("pantallas/p1GameOverAlt.png", Texture.class);
        assetManager.load("pantallas/Fondo2.png", Texture.class);
        //cargar botones
        assetManager.load("pausa/botonContinuar.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("pausa/fondoPPausa.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("botones/botonPausa.png", Texture.class);
        //Cargar efectos de Sonido
        assetManager.load("sonidos/moneda.wav", Sound.class);
        assetManager.load("sonidos/ghost.wav", Sound.class);
        assetManager.load("sonidos/hurt.wav", Sound.class);
        assetManager.load("sonidos/jump.wav", Sound.class);
        assetManager.load("sonidos/laserpew.wav", Sound.class);
    }

    @Override
    public void render(float delta) {
        // Actualizar carga
        actualizar();
        //Dibujar
        borrarPantalla();
        spriteCargando.setRotation(spriteCargando.getRotation() - 5); //Rotar el timón, capitán.
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        batch.end();
    }

    private void actualizar() {
        if (assetManager.update()) {// si el asset manager ya terminó
            switch (siguientePantalla) {
                case NIVEL_1:
                    juego.setScreen(new PantallaPlaya(juego)); // ir al nivel!
                    break;
                case NIVEL_2:
                    //juego.setScreen(new PantallaNoche(juego)); // ir al nivel 2
                    break;
            }
        } else {
            float avance = assetManager.getProgress() * 100;
        }
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

