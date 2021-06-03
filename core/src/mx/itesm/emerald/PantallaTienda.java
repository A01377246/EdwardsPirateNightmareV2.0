package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    //verificar si el objeto ha sido comprado

    private boolean corazonComprado;
    private boolean bandanaComprada;


    //escenas de la tienda


    private EstadoTienda estadoTienda;

    private EscenaCorazon escenaCorazon;

    private EscenaBandana escenaBandana;


    public PantallaTienda(final EdwardsPirateNightmare juego) {
        tienda = new Texture("pantallas/fondoT.png");
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

        crearTexto();
        crearIconoMoneda();
        crearBotones();
        verificarObjetos();
        Gdx.input.setCatchKey(Input.Keys.BACK, false);

    }

    private void verificarObjetos() {
        Preferences objetosPrefs = Gdx.app.getPreferences("Objetos"); // preferencias para guardar objetos
        bandanaComprada = objetosPrefs.getBoolean("bandanaActiva",false);
        corazonComprado = objetosPrefs.getBoolean("vidaExtra", false);
    }


    private void crearIconoMoneda() {
        texturaMoneda = new Texture ("sprites/coin.png");

        iconoMoneda = new Moneda(texturaMoneda, ANCHO -200, ALTO - 100);
        iconoMoneda.sprite.setScale(2);
    }


    private void crearBotones(){
        Button btnBandana = crearBoton("sprites/bandana.png");
        btnBandana.setTransform(true);
        btnBandana.scaleBy(3);

        btnBandana.setPosition(ANCHO / 2 -25,  ALTO * 2/4, Align.center); // centrar el botòn en las coordenadas selecconadas

        Button btnCorazon = crearBoton("sprites/heart.png");
        btnCorazon.setTransform(true);
        btnCorazon.scaleBy(2);
        btnCorazon.setPosition(ANCHO / 2,  ALTO * 1/4, Align.center); // centrar el botòn en las coordenadas selecconadas
        btnCorazon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               escenaCorazon = new EscenaCorazon(vista);
               Gdx.input.setInputProcessor(escenaCorazon); //pasar input processor a escena corazón
                estadoTienda = EstadoTienda.COMPRANDOCORAZON; //cambiar el estado a comprando corazón
            }
        });

        btnBandana.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenaBandana = new EscenaBandana(vista);
                Gdx.input.setInputProcessor(escenaBandana); //pasar input processor a escena corazón
                estadoTienda = EstadoTienda.COMPRANDOBANDANA; //cambiar el estado a comprando corazón
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
        recuperarMonedas(); // actualizar número de monedas constantemente
        // mostrar monedas
        texto.mostrarMensaje(batch, Integer.toString(cantidadMonedas), ANCHO -100, ALTO - 50);
        /* Mostrar bandana, precio y descripción

        texto.mostrarMensaje(batch, "Multiplica tus puntos por dos", ANCHO/2, ALTO - 225);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO - 180);

        // Mostrar corazon, precio y descripción

        texto.mostrarMensaje(batch, "Inicias con un punto de vida extra", ANCHO/2, ALTO - 525);
        texto.mostrarMensaje(batch, "10", ANCHO/2, ALTO*1/8);*/

        batch.end();
        escenaTienda.draw();
        if(estadoTienda == EstadoTienda.COMPRANDOCORAZON && escenaCorazon != null){
            escenaCorazon.draw();
        }
        if(estadoTienda == EstadoTienda.COMPRANDOBANDANA && escenaBandana != null){
            escenaBandana.draw();
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

    //método para simplificiar la creación de botones
    private Button crearBoton(String imagen) { // añadir segundo parámetro para hacer un cambio cuando el usuario da click
        // crear otra textura y otro drawable
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture



        return new Button(trdBtnJugar); // regesar imagen normal e imagen de retroalimentacion
    }

    private class EscenaCorazon extends Stage {
        private Texture texturaFondo;
        public EscenaCorazon(final Viewport vista){
            super(vista);
            texturaFondo = new Texture("pantallas/compraCorazon.png");
            Image imgFondo = new Image(texturaFondo);
            //Mostrar imagen en el centro de la pantalla
            imgFondo.setPosition(ANCHO / 2, ALTO / 2 - 30, Align.center);

            // Crear botones para la pantalla pausa usando el método crear botón
            Button botonDesactivado = crearBoton("botones/botonComprarD.png");
            Button botonComprar = crearBoton("botones/botonComprar.png");
            Button botonVolver = crearBoton("botones/botonvolverS.png");



            //Posiciones de los botones

            botonVolver.setPosition(460, 160, Align.center);
            botonComprar.setPosition(820, 160, Align.center);
            botonDesactivado.setPosition(820,160, Align.center);

            botonVolver.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Quitar ventana de compra
                    estadoTienda = EstadoTienda.EXPLORANDO;
                    Gdx.input.setInputProcessor(escenaTienda); // devolver input procesor a la escena tienda
                    escenaCorazon = null; // Hacer que sea posible seleccionar la pausa de nuevo y desaparecer la ventana.
                }
            });

            botonComprar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Quitar ventana de compra
                    cantidadMonedas -= 10; // restar el costo del corazon
                    escenaCorazon = null; // cerrar ventana de compra
                    corazonComprado = true; // comprar corazón
                    Preferences objetosPrefs = Gdx.app.getPreferences("Objetos"); // preferencias para guardar objetos
                    objetosPrefs.putBoolean("vidaExtra", true);
                    Preferences monedasPrefs = Gdx.app.getPreferences("Monedas");
                    monedasPrefs.putInteger("contadorMonedas", cantidadMonedas); // guardar cantidad monedas
                    monedasPrefs.flush(); // guardar el gasto de monedas
                    objetosPrefs.flush(); // hacer que la compra persista

                    Gdx.input.setInputProcessor(escenaTienda);
                }
            });



            addActor(imgFondo);
            addActor(botonVolver);

            if(cantidadMonedas < 10 || corazonComprado == true){ //Si el jugador no tiene suficientes monedas añadir el botón desactivado o ya compró un corazón
                addActor(botonDesactivado);
            }else{
                addActor(botonComprar);
            }




        }

    }

    private class EscenaBandana extends Stage {
        private Texture texturaFondo;
        public EscenaBandana(final Viewport vista){
            super(vista);
            texturaFondo = new Texture("pantallas/compraBandana.png");
            Image imgFondo = new Image(texturaFondo);
            //Mostrar imagen en el centro de la pantalla
            imgFondo.setPosition(ANCHO / 2, ALTO / 2 - 30, Align.center);

            // Crear botones para la pantalla pausa usando el método crear botón
            Button botonDesactivado = crearBoton("botones/botonComprarD.png");
            Button botonComprar = crearBoton("botones/botonComprar.png");
            Button botonVolver = crearBoton("botones/botonvolverS.png");



            //Posiciones de los botones

            botonVolver.setPosition(460, 160, Align.center);
            botonComprar.setPosition(820, 160, Align.center);
            botonDesactivado.setPosition(820,160, Align.center);

            botonVolver.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Quitar ventana de compra
                    estadoTienda = EstadoTienda.EXPLORANDO;
                    Gdx.input.setInputProcessor(escenaTienda); // devolver input procesor a la escena tienda
                    escenaBandana = null; // Hacer que sea posible seleccionar la pausa de nuevo y desaparecer la ventana.
                }
            });

            botonComprar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Quitar ventana de compra
                    cantidadMonedas -= 10; // restar el costo del corazon
                    escenaBandana = null; // cerrar ventana de compra
                    bandanaComprada = true; // comprar la bandana
                    Preferences objetosPrefs = Gdx.app.getPreferences("Objetos"); // preferencias para guardar objetos
                    objetosPrefs.putBoolean("bandanaActiva", true);
                    Preferences monedasPrefs = Gdx.app.getPreferences("Monedas");
                    monedasPrefs.putInteger("contadorMonedas", cantidadMonedas); // guardar cantidad monedas
                    monedasPrefs.flush(); // guardar el gasto de monedas
                    objetosPrefs.flush(); // hacer que la compra persista
                    Gdx.input.setInputProcessor(escenaTienda);
                }
            });


            addActor(imgFondo);
            addActor(botonVolver);

            if(cantidadMonedas < 10 || bandanaComprada == true){ //Si el jugador no tiene suficientes monedas añadir el botón desactivado o ya compró un corazón
                addActor(botonDesactivado);
            }else{
                addActor(botonComprar);
            }




        }

    }




}




