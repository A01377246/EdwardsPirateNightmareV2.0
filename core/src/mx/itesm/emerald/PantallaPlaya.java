package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.itesm.emerald.edwardspiratenightmare.utilities.Texto;

public class PantallaPlaya extends Pantalla {
    private EdwardsPirateNightmare juego;

    // Asset Manager
    private AssetManager assetManager;

    //Fondo infinito
    private Texture texturaFPlaya;
    private float xFondo = 0;

    //Personaje
    private Edward edward;
    private Texture texturaEdward;
    private Sound edwardLastimado;
    private Sound edwardSalto;

    // bandera Muerte
    private boolean banderaMuerte = false;

    //Objetos
    private Tumba f;
    private Texture texturaTumba;

    //Fantasma 1 (Enemigos)
    private Array<Fantasma1> arrFantasma1;
    private Texture texturaFantasma1;
    private float timerCrearFantasma1;
    private final float TIEMPO_CREAR_FANTASMA1 = 3;
    private Sound sonidoFantasma;


    //Crear Marcador
    private int puntos = 0;
    private Texto texto;

    //Monedas
    private Array<Moneda> arrMonedas; // arreglo de monedas
    private Texture texturaMoneda; // textura de las monedas
    private float timerCrearMoneda; // Contador que determina si es momento de que una moneda aparezca
    private final float TIEMPO_CREAR_MONEDA = 10; // las monedas se crearán cada 10 segundos
    private int contadorMonedas = 0; //cuenta cuántas monedas recoge el usuario
    private Moneda iconoContadorMonedas;
    private Sound sonidoMonedaRecogida;

    //Timer salida de edward del nivel
    private float tiempoSalida = 10;

    // Objeto Escudo
    private Texture texturaEscudo;
    private Array<Corazon> arrItemEscudo;
    private float timerCrearEscudo;
    private final float TIEMPO_CREAR_ITEM_ESCUDO = 45; // crear un corazón cuando hayan pasado 40 segundos

    //TIEMPO NIVEL
    private final float tiempoNivel = 120; //El nivel dura dos minutos
    private float timerNivel = 0; // Timer que acumula el tiempo para determinar cuando termina el nivel

    //Disparo del personaje
    private Array<Bala> arrBalas;
    private Texture texturaBala;
    private Sound sonidoBala;
    //Vidas del personaje
    private Array<Corazon> arrCorazones;

    //Pausa

    private EscenaPausa escenaPausa; // escena
    private BotonPausa botonPausa;
    private Texture texturaBotonPausa;
    private ProcesarEntrada procesadorEntrada; // Objeto que registra

    // bandera de fin de nivel
    private boolean banderaFinNivel;

    //Rectangulo de colision
    Rectangle rectColisionE;

    //Estados del Juego
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO; //Estado Inicial, jugando

    public PantallaPlaya(EdwardsPirateNightmare juego) {
        this.juego = juego;
        assetManager = juego.getAssetManager(); //Obtener asset Manager
    }

    @Override
    public void show() {
        crearFondo();
        crearTexto();
        crearEdward();
        crearBalas();
        crearCorazon();
        crearFantasma1();
        crearMoneda();
        crearTumba();
        crearIconoContadorMonedas();
        crearItemCorazon();
        crearBotonPausa();
        crearRectanguloColisionEdward();
        crearSonidos();
        procesadorEntrada = new ProcesarEntrada();
        //Bloquear tecla back
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        //Reproducir Musica nivel 1
        juego.reproducirMusica(EdwardsPirateNightmare.TipoMusica.NIVEL_1);

        //Pone el input Processor
        Gdx.input.setInputProcessor(procesadorEntrada);
    }

    private void crearRectanguloColisionEdward() {
        rectColisionE = new Rectangle();
        rectColisionE.setX(edward.sprite.getX()); //El rectangulo inicia exactamente dónde se encuentra Edward
        rectColisionE.setY(edward.sprite.getY()); //Iniciar la coordenada y del rectángulo en los pies de edward
        rectColisionE.setWidth(edward.getX());//
    }



    //Este método inicializa los objetos que serán usados para efectos de sonido
    private void crearSonidos() {
        //Sonidos Objetos
        sonidoMonedaRecogida = assetManager.get("sonidos/moneda.wav");
        //SonidosEnemigos
        sonidoFantasma = assetManager.get("sonidos/ghost.wav");
        //SonidosEdward
        edwardLastimado = assetManager.get("sonidos/hurt.wav");
        edwardSalto = assetManager.get("sonidos/jump.wav");

        //SonidosBala
        sonidoBala = assetManager.get("sonidos/laserpew.wav");
    }

    private void crearBotonPausa() {
        texturaBotonPausa = assetManager.get("botones/botonPausa.png");
        botonPausa = new BotonPausa(texturaBotonPausa, 0, ALTO - texturaBotonPausa.getHeight()); //Dibujar el botón de pausa en la esquina superior izquierda
        botonPausa.sprite.setScale(1.5f); //hacer el botón pausa 1.5 veces mas grande.
    }

    private void crearItemCorazon() {
        texturaEscudo = assetManager.get("sprites/Escudo.png");
        arrItemEscudo = new Array<>(2); // Solo apareceran dos corazones que ayudarán al jugador a bloquear un golpe
    }

    private void crearIconoContadorMonedas() {
        iconoContadorMonedas = new Moneda(texturaMoneda, ANCHO / 2 - texturaMoneda.getWidth(), ALTO * 0.95f - texturaMoneda.getHeight());
        iconoContadorMonedas.sprite.setScale(2);
    }

    private void crearMoneda() {
        texturaMoneda = assetManager.get("sprites/coin.png");
        arrMonedas = new Array<>(5); // Máximo 5 monedas aparecerán por nivel
    }

    private void crearCorazon() {
        Texture texturaCorazon = assetManager.get("sprites/heart.png");
        arrCorazones = new Array<>(5);
        for (int renglon = 0; renglon < 1; renglon++) {
            for (int colunma = 0; colunma < 5; colunma++) {
                Corazon corazon = new Corazon(texturaCorazon,
                        50 + colunma * 60, 0.85f * ALTO + renglon * 60);
                arrCorazones.add(corazon);
            }
        }
    }

    private void crearBalas() {
        arrBalas = new Array<>();
        texturaBala = assetManager.get("sprites/Bala_Plasma.png");
    }

    private void crearFondo() {
        texturaFPlaya = assetManager.get("pantallas/P1.png");
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }

    private void crearFantasma1() {
        arrFantasma1 = new Array<>();
        texturaFantasma1 = assetManager.get("sprites/fantasma1/fantasmaAnimacion.png");
    }

    private void crearEdward() {
        texturaEdward = assetManager.get("sprites/edwardRun.png");
        edward = new Edward(texturaEdward, 20, 50);
    }

    //Método para dibujar botones que se utilizarán en los diversos estados del juego
    private Button crearBoton(String imagen) {
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture
        return new Button(trdBtnPausa); // regesar imagen del boton Pausa
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.setProjectionMatrix(camara.combined);
        //Actualizar
        actualizar(delta);
        //Dibuja Fondo
        batch.draw(texturaFPlaya, xFondo, 0);
        batch.draw(texturaFPlaya, xFondo + texturaFPlaya.getWidth(), 0);
        //Dibuja personaje
        edward.render(batch);
        //Dibujar marcador
        texto.mostrarMensaje(batch, Integer.toString(puntos), 0.95f * ANCHO, 0.95f * ALTO);
        //Dibujar botón Pausa
        botonPausa.render(batch);
        //Dibujar corazon
        for (Corazon corazon : arrCorazones) {
            corazon.render(batch);
        }
        //Dibujar fantasma1
        for (Fantasma1 fantasma1 : arrFantasma1) {
            fantasma1.render(batch);
        }
        //Dibujar balas
        for (Bala bala : arrBalas) {
            bala.render(batch);
        }
        //Dibujar monedas
        for (Moneda moneda : arrMonedas) {
            moneda.render(batch);
        }
        // Dibujar contador monedas
        iconoContadorMonedas.render(batch);
        texto.mostrarMensaje(batch, Integer.toString(contadorMonedas), 0.55f * ANCHO, 0.95f * ALTO);
        //Dibujar item corazon
        for (Corazon itemCorazon : arrItemEscudo) {
            itemCorazon.render(batch);
        }
        if (banderaMuerte) {
            juego.setScreen(new PantallaJuegoTerminado(juego));
        }
        if (banderaFinNivel) {
            tiempoSalida -= delta; // Comenzar a restar del tiempo de salida
            Gdx.input.setInputProcessor(null); // dejar de procesar lo que haga el usuario
            edward.moverDerecha(delta);
            if (tiempoSalida <= 0) {
                juego.setScreen(new PantallaFinNivel(juego));
                // Guardar puntos y monedas al terminar el nivel
                Preferences preferencias = Gdx.app.getPreferences("Puntaje");
                preferencias.putInteger("puntos", puntos);
                preferencias = Gdx.app.getPreferences("Monedas");
                preferencias.putInteger("contadorMonedas", contadorMonedas);
                preferencias.flush(); //Se guardan en memoria no volátil
            }
        }
        batch.end();
        //Dibujar la pausa
        if (estadoJuego == EstadoJuego.PAUSADO && escenaPausa != null) { // Si el usuario ya tocó el botón de pausa
            escenaPausa.draw(); // dibujar pausa
        }
    }

    private void actualizar(float delta) {
        //Actualizar solo cuando se está jugando
        if (estadoJuego == EstadoJuego.JUGANDO) {
            actualizarFondo();
            actualizarFantasma1(delta);
            actualizarBalas(delta);
            verificarColisionFantasma();
            actualizarTiempo(delta);
            verificarSalud();
            actualizarMonedas(delta);
            verificarColisionMoneda();
            actualizarItemCorazon(delta);
            verificarColisionItemEscudo();
            actualizarRectanguloDeColision();
        }
    }

    private void verificarColisionItemEscudo() {
        for (int i = arrItemEscudo.size - 1; i >= 0; i--) {
            Corazon escudo = arrItemEscudo.get(i);
            // si la moneda y Edward chocan
            if (escudo.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                arrCorazones.add(escudo); // añadir un corazon para recuperar salud
                arrItemEscudo.removeIndex(i); //Remover el corazon
            }
        }
    }

    private void actualizarItemCorazon(float delta) {
        timerCrearEscudo += delta;
        if (timerCrearEscudo > TIEMPO_CREAR_ITEM_ESCUDO) {
            timerCrearEscudo = 0;
            float xCorazon = MathUtils.random(ANCHO, ANCHO + 1.5f);
            float yCorazon = MathUtils.random(80, 210);
            Corazon itemEscudo = new Corazon(texturaEscudo, xCorazon, yCorazon);
            arrItemEscudo.add(itemEscudo);
        }
        // mover corazones
        for (Corazon corazon : arrItemEscudo) {
            corazon.moverIzquierda(delta);
        }
    }

    private void verificarColisionMoneda() {
        if (timerNivel < 118) {
            for (int i = arrMonedas.size - 1; i >= 0; i--) {
                Moneda moneda = arrMonedas.get(i);
                // si la moneda y Edward chocan
                if (moneda.sprite.getBoundingRectangle().overlaps(rectColisionE)) {
                    contadorMonedas += 1; // Sumar uno al contador de monedas
                    sonidoMonedaRecogida.play(); //Reproducir sonido de moneda recogida
                    arrMonedas.removeIndex(i); //Remover las monedas
                }
            }
        }
    }

    private void actualizarMonedas(float delta) {
        timerCrearMoneda += delta;
        if (timerCrearMoneda > TIEMPO_CREAR_MONEDA) {  //Si es momento de crear una moneda
            timerCrearMoneda = 0;
            float xMoneda = MathUtils.random(ANCHO, ANCHO + 1.5f);
            float yMoneda = MathUtils.random(30, 210); // La y de las monedas será un número aleatorio entre 20 y 250
            Moneda moneda = new Moneda(texturaMoneda, xMoneda, yMoneda);
            moneda.sprite.setScale(1.5f); // Hacer la moneda 1.5 más grande
            arrMonedas.add(moneda);
        }
        //Mover monedas
        for (Moneda moneda : arrMonedas) {
            moneda.moverIzquierda(delta); // mover monedas
        }
    }

    private void verificarSalud() {
        if (arrCorazones.size == 0) {
            banderaMuerte = true;
        }
    }

    private void crearTumba() {
        texturaTumba = assetManager.get("sprites/soloHeart.png");
        f = new Tumba(texturaTumba, edward.getX(), edward.getY());
    }

    private void actualizarTiempo(float delta) {
        timerNivel += delta; //acumular tiempo en el timer
        if (timerNivel >= tiempoNivel) {
            banderaFinNivel = true;
        }
    }

    private void verificarColisionFantasma() {
        if (timerNivel < 118) {
            for (int i = arrCorazones.size - 1; i >= 0; i--) {
                for (int j = arrFantasma1.size - 1; j >= 0; j--) {
                    Fantasma1 fantasma = arrFantasma1.get(j);
                    //si edward choca con un fantasma
                    if (fantasma.sprite.getBoundingRectangle().overlaps(rectColisionE)) {
                        edwardLastimado.play(); //reproducir sonido cuando edward es lastimado
                        arrCorazones.removeIndex(i); //edward pierde un corazon
                        arrFantasma1.removeIndex(j); //el fantasma desaparece
                    }

                }
            }
        }
    }

    private void actualizarBalas(float delta) {
        for (int i = arrBalas.size - 1; i >= 0; i--) {
            Bala bala = arrBalas.get(i);
            bala.mover(delta);
            //prueba si la bola debe de desaparecer si se sale de la pantalla
            if (bala.getX() > ANCHO) {
                arrBalas.removeIndex(i);
            } else {
                for (int iA = arrFantasma1.size - 1; iA >= 0; iA--) {
                    Fantasma1 fantasma1 = arrFantasma1.get(iA);
                    if (bala.sprite.getX() >= fantasma1.sprite.getX() && bala.sprite.getY() >= fantasma1.getY() && bala.sprite.getY() <= fantasma1.getY() + fantasma1.sprite.getHeight()) {
                        //Contar Puntos
                        puntos += 150;
                        //Borrar bala
                        arrBalas.removeIndex(i);
                        sonidoFantasma.play(); //Reproducir sonido de muerte del fantasma
                        arrFantasma1.removeIndex(iA);
                        break;
                    }
                }
            }
        }
    }

    private void actualizarFantasma1(float delta) {
        timerCrearFantasma1 += delta;
        if (timerCrearFantasma1 > TIEMPO_CREAR_FANTASMA1) {
            timerCrearFantasma1 = 0;
            //crear fantasma1
            float xFantasma1 = MathUtils.random(ANCHO, ANCHO * 1.5f);
            float yFantasma1 = MathUtils.random(30, 210);
            Fantasma1 fantasma1 = new Fantasma1(texturaFantasma1, xFantasma1, yFantasma1);
            arrFantasma1.add(fantasma1);
        }

        //Mover al fantasma1
        if (timerNivel < 50) {  // Los fantasmas van a una velocidad normal
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta);
            }
        } else if (timerNivel < 90) {
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 1.5f); // los fantasmas son 2 veces más rápidos!
            }
        } else if (timerNivel < 120) {
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 1.8f); // los fantasmas son 5 veces más rápidos!
            }
        } else {
            //No mover fantasmas en los ultimos 10 segundos de salida del nivel
            arrFantasma1.clear();
        }
    }

    private void actualizarFondo() {
        xFondo -= 4;
        if (xFondo <= -texturaFPlaya.getWidth()) {
            xFondo = 0;
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() { //Borrar objetos cargados en el assetManager
        arrCorazones.clear();
        arrFantasma1.clear();
        arrBalas.clear();
        //borrar sprites
        assetManager.unload("sprites/pauseButton.png");
        assetManager.unload("sprites/edwardRun.png");
        assetManager.unload("sprites/coin.png");
        assetManager.unload("sprites/bala.png");
        assetManager.unload("sprites/heart.png");
        assetManager.unload("sprites/fantasma1/fantasmaAnimacion.png");
        assetManager.unload("sprites/soloHeart.png");

        //borrar pantallas
        assetManager.unload("pantallas/pantallaFinNivelOp.png");
        assetManager.unload("pantallas/p1GameOverAlt.png");
        assetManager.unload("pantallas/P1.png");

        //cargar botones
        assetManager.unload("pausa/botonContinuar.png");
        assetManager.unload("pausa/botonMenu.png");
        assetManager.unload("pausa/fondoPPausa.png");
        assetManager.unload("pausa/botonMenu.png");
        assetManager.unload("botones/botonPausa.png");
    }

    private class ProcesarEntrada implements InputProcessor {
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);
            if (v.x >= ANCHO / 2) {
                //Dispara
                Bala bala = new Bala(texturaBala, edward.sprite.getX() + edward.sprite.getWidth() * 1 / 2, edward.getY() + edward.sprite.getHeight() * 1 / 2);
                arrBalas.add(bala);
                //reproducir sonido de bala

                sonidoBala.play();

                //Verificar boton pausa

            }
            if (v.x > botonPausa.sprite.getX() && v.x <= 0 + botonPausa.sprite.getWidth() &&
                    v.y < botonPausa.sprite.getY() + botonPausa.sprite.getHeight() && v.y > botonPausa.sprite.getY()
                    && escenaPausa == null) { //Solo crear la escena de pausa cuándo no existe un objeto previo

                escenaPausa = new EscenaPausa(vista); // crear nueva escena Pausa
                estadoJuego = EstadoJuego.PAUSADO;
                edward.cambiaAnimación(estadoJuego); // pausar a edward
                Gdx.input.setInputProcessor(escenaPausa); // Cambiar el input Processor a escena pausa

            }
            if (v.x <= ANCHO / 2 && v.y < ALTO - botonPausa.sprite.getHeight()) { // Saltar al tocar el lado izquierdo de la pantalla y cuando no se toca el boton de pausa
                if (edward.devolverEstado() == EstadoEdward.CAMINANDO) { //solo reproducir cuando esta en el suelo, evitar multiples reproducciones
                    edwardSalto.play(); //Reproducir efecto de sonido para el salto
                }
                edward.saltar();
            }
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }

    private void actualizarRectanguloDeColision() {
        if (edward.devolverEstado() == EstadoEdward.SALTANDO) {  // Si edward está saltando, actualizar constantemente su rectángulo de colisión
            rectColisionE.setHeight(edward.sprite.getBoundingRectangle().getHeight() + edward.sprite.getHeight());
        }
    }

    //Escena que se muestra cuando el usuario pausa el juego
    private class EscenaPausa extends Stage {
        private Texture texturaFondo;

        public EscenaPausa(final Viewport vista) {
            super(vista); // Usar constructor de super clase stage

            texturaFondo = assetManager.get("pausa/fondoPPausa.png");
            Image imgFondo = new Image(texturaFondo);
            //Mostrar imagen en el centro de la pantalla
            imgFondo.setPosition(ANCHO / 2, ALTO / 2 - 30, Align.center);

            // Crear botones para la pantalla pausa usando el método crear botón

            Button botonContinuar = crearBoton("pausa/botonContinuar.png");
            Button botonMenu = crearBoton("pausa/botonMenu.png");

            //Posiciones de los botones
            botonContinuar.setPosition(ANCHO / 2, 520 * .7f, Align.center);
            botonMenu.setPosition(ANCHO / 2, 320 * .5f, Align.center);

            addActor(imgFondo);
            addActor(botonContinuar);
            addActor(botonMenu);

            //Listener de boton continuar
            botonContinuar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Quitar pausa, animar a edward de nuevo y devolver el input processor al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    edward.cambiaAnimación(estadoJuego);
                    Gdx.input.setInputProcessor(procesadorEntrada);
                    escenaPausa = null; // Hacer que sea posible seleccionar la pausa de nuevo.

                }
            });

            //Listener de boton menú
            botonMenu.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Salir del nivel y volver a la pantalla principal
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
        }
    }
}



