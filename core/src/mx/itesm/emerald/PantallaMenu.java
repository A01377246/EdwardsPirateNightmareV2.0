package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;


public class PantallaMenu extends Pantalla {

    //Referencia al juego principal
    private EdwardsPirateNightmare juego;
    private Texture texturaFondo;
    private Stage escenaMenu; //contenedor de actores en la pantalla

    public PantallaMenu(EdwardsPirateNightmare juego) {
        this.juego = juego;
    }

    @Override
    public void show() { // Se ejecuta al inicio, antes de mostrar la pantalla
        crearMenu(); // usar otro método para no saturar con código
        juego.reproducirMusica(EdwardsPirateNightmare.TipoMusica.MENU); //reproducir música del menú
    }

    private void crearMenu() {
        texturaFondo = new Texture("pantallas/PantallaPrincipal.png");
        // crear escena
        escenaMenu = new Stage(vista); // vista como paramétro para que se escalen correctamente

        // crear botón

        Button btnjugar = crearBoton("botones/botonJugar.png", "botones/botonJugarS.png");
        btnjugar.setPosition(ANCHO / 3, 2 * ALTO / 4, Align.center); // centrar el botòn en las coordenadas selecconadas
        // Hacer algo cuando se seleccione el boton
        btnjugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar pantalla
                juego.setScreen(new PantallaCarga(juego, Pantallas.NIVEL_1)); //Cargar primer nivel
            }
        });

        Button btncreditos = crearBoton("botones/botonCreditosf.png", "botones/botonCreditosfS.png");
        btncreditos.setPosition(2 * ANCHO / 3, ALTO / 4, Align.center);
        btncreditos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaCreditos(juego));
            }
        });

        Button btnAjustes = crearBoton("botones/boton ajustes.png", "botones/boton ajustes.png");
        btnAjustes.setPosition(0, 0); // habra que editar el boton, le sobra mucho espacio
        btnAjustes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaAjustes(juego));
            }
        });

        Button btnHistoria = crearBoton("botones/botonHistoria.png", "botones/botonHistoriaS.png");
        btnHistoria.setPosition(2 * ANCHO / 3, 2 * ALTO / 4, Align.center);
        btnHistoria.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaHistoria(juego));
            }
        });

        Button btnTienda = crearBoton("botones/botonTienda.png", "botones/botonTiendaS.png");
        btnTienda.setPosition(ANCHO / 3, ALTO / 4, Align.center);
        btnTienda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaTienda(juego));
            }
        });


        // Agregar botón a escena

        escenaMenu.addActor(btnjugar);
        escenaMenu.addActor(btncreditos);
        escenaMenu.addActor(btnTienda);
        escenaMenu.addActor(btnHistoria);
        escenaMenu.addActor(btnAjustes);

        //atender botones

        Gdx.input.setInputProcessor(escenaMenu);

    }

    //método para simplificiar la creación de botones
    private Button crearBoton(String imagen, String seleccionado) { // añadir segundo parámetro para hacer un cambio cuando el usuario da click
        // crear otra textura y otro drawable
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture

        Texture texturaCambio = new Texture(seleccionado); // cargar imagén del botón.
        TextureRegionDrawable trdBtnCambio = new TextureRegionDrawable(texturaCambio);

        return new Button(trdBtnJugar, trdBtnCambio); // regesar imagen normal e imagen de retroalimentacion
    }

    @Override
    //Se ejecuta automáticamente (60 veces por segundo)
    public void render(float delta) { // tiempo transcurrido entre frames, delta
        borrarPantalla(0, 1, 0); // borrar pantalla

        batch.begin(); // comenzar batch
        batch.setProjectionMatrix(camara.combined); // instrucciones de opengl para que to se vea correcto
        batch.draw(texturaFondo, 0, 0);
        batch.end();

        escenaMenu.draw(); // La escena Menu se dibuja por si sola, no necesita batch
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