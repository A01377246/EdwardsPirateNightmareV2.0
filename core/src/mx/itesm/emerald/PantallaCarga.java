package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
        spriteCargando.setPosition(ANCHO/2 - spriteCargando.getWidth()/2, ALTO/2-spriteCargando.getHeight()/2);

        cargarSiguientePantalla(); // cargar todos los recursos del nivel 1
    }

    private void cargarRecursosNivel1(){
        //cargar sprites
        assetManager.load("sprites/pauseButton.png", Texture.class);
        assetManager.load("sprites/edwardRun.png", Texture.class);
        assetManager.load("sprites/coin.png", Texture.class);
        assetManager.load("sprites/bala.png", Texture.class);
        assetManager.load("sprites/heart.png", Texture.class);
        assetManager.load("sprites/fantasma1/fantasmaAnimacion.png", Texture.class);
        assetManager.load("sprites/soloHeart.png", Texture.class);



        //cargar pantallas

        assetManager.load("pantallas/pantallaFinNivelOp.png", Texture.class);
        assetManager.load("pantallas/p1GameOverAlt.png", Texture.class);
        assetManager.load("pantallas/P1.png", Texture.class);


        //cargar botones
        assetManager.load("pausa/botonContinuar.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("pausa/fondoPPausa.png", Texture.class);
        assetManager.load("pausa/botonMenu.png", Texture.class);
        assetManager.load("botones/botonPausa.png", Texture.class);





        //cargar Audio

    }
    private void cargarSiguientePantalla(){
        switch(siguientePantalla){
            case NIVEL_1:
                cargarRecursosNivel1();
            case NIVEL_2: 
                cargarRecursosNivel2();
        }
    }

    private void cargarRecursosNivel2() {
    }

    @Override
    public void render(float delta) {

        // Actualizar carga

        actualizar();

        //Dibujar
        borrarPantalla();
        spriteCargando.setRotation(spriteCargando.getRotation() - 5) ; //Rotar el timón, capitán.
        batch.setProjectionMatrix(camara.combined);


        batch.begin();
        spriteCargando.draw(batch);
        batch.end();

    }

    private void actualizar() {
        if(assetManager.update()) {// si el asset manager ya terminó
        switch(siguientePantalla){
            case NIVEL_1:
                juego.setScreen(new PantallaPlaya(juego)); // ir al nivel!
            case NIVEL_2:

        }
        }else{
            float avance = assetManager.getProgress()*100; //Mostrar avance del asset manager
            Gdx.app.log("Cargando", avance + "%");
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

