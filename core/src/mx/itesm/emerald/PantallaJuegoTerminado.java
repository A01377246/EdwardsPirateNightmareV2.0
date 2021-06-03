package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaJuegoTerminado extends Pantalla{

    private Stage escenaJuegoTerminado;
    private Texture fondojuegoTerminado;
    private Texture texturaTumba;
    private Tumba tumbaFinalJuego;
    private EdwardsPirateNightmare juego;

    public PantallaJuegoTerminado(final EdwardsPirateNightmare juego){
        this.juego = juego;
        texturaTumba = new Texture("sprites/Tumbstone.png");
        tumbaFinalJuego = new Tumba(texturaTumba, ANCHO/2, ALTO/4); //Dibujar tumba en el centro de la pantalla
        tumbaFinalJuego.sprite.setScale(5); //Dibujar la tumba 5 veces más grande
        fondojuegoTerminado = new Texture("pantallas/pantallaMuerte.png");
        escenaJuegoTerminado = new Stage(vista); // parámetro vista para escalar correctamente
        Button botonVolverAMenu = crearBoton("botones/botonVolverMenu.png"); // cargar imágen del botón
        botonVolverAMenu.setPosition(0, 25);
        botonVolverAMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a pantalla de juego terminado
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaJuegoTerminado.addActor(botonVolverAMenu); // añadir boton a escena
        Gdx.input.setInputProcessor(escenaJuegoTerminado); // la escena juego se encarga de registrar el input

    }





    @Override
    public void show() {
        juego.reproducirMusica(EdwardsPirateNightmare.TipoMusica.FINJUEGO); // poner la música de muerte, f


    }

    @Override
    public void render(float delta) {

        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        batch.draw(fondojuegoTerminado,0,0);
        tumbaFinalJuego.render(batch); // Dibujar Tumba
        batch.end();

        escenaJuegoTerminado.draw(); // dibujar la escena de juego terminado



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
