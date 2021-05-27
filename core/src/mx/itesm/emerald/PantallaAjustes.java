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

public class PantallaAjustes extends Pantalla {

    private final EdwardsPirateNightmare juego;
    private Texture texturaAjustes;
    private Texture texturaBack;
    private Stage escenaAjustes;
    private Texto texto;

    public PantallaAjustes( EdwardsPirateNightmare juego) {
        this.juego=juego;
    }

    @Override
    public void show() {
        escenaAjustes = new Stage(vista);
        crearFondo();
        crearBotonBack();
        crearBotones();
        crearTexto();
        Gdx.input.setInputProcessor(escenaAjustes);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }


    private void crearBotones() {

        Button btnSonido = crearBoton("botones/button_sound_on.png","botones/button_sound_hover.png");
        btnSonido.setPosition(ANCHO/2-80,2*ALTO/4, Align.center);
        escenaAjustes.addActor(btnSonido);
        btnSonido.addListener(new ClickListener(){ //Si el usuario da click al botón de encender música
            @Override
            //Leer estado actual de musica encendida, si es falso, comenzar a reproducir.
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Preferences prefs = Gdx.app.getPreferences("silenciar");
                Boolean musicaEncendida = prefs.getBoolean("musicaEncendida", true);
                if (!musicaEncendida) {
                    prefs.putBoolean("musicaEncendida", true);
                    prefs.flush(); // guardar preferencia de música silenciada
                    juego.reproducirMusica(EdwardsPirateNightmare.TipoMusica.MENU);

                }
            }
            });

        Button btnNoSonido = crearBoton("botones/button_sound_off.png","botones/button_sound_off.png");
        btnNoSonido.setPosition(ANCHO/2+80,2*ALTO/4,Align.center);
        escenaAjustes.addActor(btnNoSonido);
        btnNoSonido.addListener(new ClickListener(){ //Si el usuario da click al botón de apagar música
            @Override
            //Leer estado actual de musica encendida, si es true,  cambiar a false y detener
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Preferences prefs = Gdx.app.getPreferences("silenciar");
                Boolean musicaEncendida = prefs.getBoolean("musicaEncendida", true);
                if(musicaEncendida){
                    prefs.putBoolean("musicaEncendida", false);
                    prefs.flush(); // guardar preferencia de música silenciada
                    juego.detenerMusica();
                }

            }
        });

        Button btnAplicar = crearBoton("botones/button_yes.png","botones/button_yes_hover.png");
        btnAplicar.setPosition(1180,20);
        escenaAjustes.addActor(btnAplicar);
        btnAplicar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenu(juego));
            }
        });

        Button btnMas = crearBoton("botones/button_plus.png","botones/button_plus_hover.png");
        btnMas.setPosition(ANCHO/2+80,2*ALTO/7+10,Align.center);
        escenaAjustes.addActor(btnMas);

        Button btnMenos = crearBoton("botones/button_minus.png","botones/button_minus_hover.png");
        btnMenos.setPosition(ANCHO/2-80,2*ALTO/7+10,Align.center);
        escenaAjustes.addActor(btnMenos);
        btnNoSonido.addListener(new ClickListener(){ //Si el usuario da click al botón de apagar música
            @Override
            //Leer estado actual de musica encendida, si es true,  cambiar a false y detener
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                    juego.disminuirVolumen(); // disminuir volumen de la música
                }

        });

    }

    private Button crearBoton(String archvio, String archivoInverso)
    {
        Texture texturaBoton = new Texture(archvio);
        TextureRegionDrawable trdBtnS = new TextureRegionDrawable(texturaBoton);
        //Inverso
        Texture texturaInverso = new Texture(archivoInverso);
        TextureRegionDrawable trdBtnInverso = new TextureRegionDrawable(texturaInverso);
        return new Button(trdBtnS, trdBtnInverso);
    }

    private void crearBotonBack() {

        texturaBack = new Texture("botones/botonvolverS.png");
        TextureRegionDrawable trdBtnVolver = new TextureRegionDrawable(texturaBack);
        Button botonVolver = new Button(trdBtnVolver);
        botonVolver.setPosition(0, 0);
        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar pantalla
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaAjustes.addActor(botonVolver);
        //Gdx.input.setInputProcessor(escenaAjustes);
    }

    private void crearFondo() {
        texturaAjustes = new Texture("pantallas/PantallaPrincipal.png");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(texturaAjustes,0,0);

        texto.mostrarMensaje(batch,"Sonido",ANCHO/2-20,ALTO/2+100);
        texto.mostrarMensaje(batch,"Musica",ANCHO/2-20,ALTO/2-35);
        batch.end();
        escenaAjustes.draw();
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
