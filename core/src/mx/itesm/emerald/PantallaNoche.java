package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

public class PantallaNoche extends Pantalla {
    private EdwardsPirateNightmare juego;

    // Asset Manager
    private AssetManager assetManager;

    //Fondo infinito
    private Texture texturaNoche;
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
    private boolean reproducirSonidoFantasma;

    //Fantasma 2
    private Array<Fantasma2> arrFantasma2;
    private Texture texturaFantasma2;
    private float timerCrearFantasma2;
    private final float TIEMPO_CREAR_FANTASMA2 = 6;

    //Fantasma 3 y rebote
    private Array<Fantasma3> arrFantasma3;
    private Texture texturaFantasma3;
    private float timerCrearFantasma3;
    private final float TIEMPO_CREAR_FANTASMA3 = 30; //se crea cada 30 segundos

    //Crear Marcador
    private int puntos = 0;
    private Texto texto;

    //Monedas
    private Array<Moneda> arrMonedas; // arreglo de monedas
    private Texture texturaMoneda; // textura de las monedas
    private float timerCrearMoneda; // Contador que determina si es momento de que una moneda aparezca
    private final float TIEMPO_CREAR_MONEDA = 10; // las monedas se crear??n cada 10 segundos
    private int contadorMonedas = 0; //cuenta cu??ntas monedas recoge el usuario
    private Moneda iconoContadorMonedas;
    private Sound sonidoMonedaRecogida;

    //Timer salida de edward del nivel
    private float tiempoSalida = 10;

    // Objeto Escudo
    private Texture texturaEscudo;
    private Array<Corazon> arrItemEscudo;
    private float timerCrearEscudo;
    private final float TIEMPO_CREAR_ITEM_ESCUDO = 45; // crear un coraz??n cuando hayan pasado 40 segundos

    //Tiempo Nivel
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

    public PantallaNoche(EdwardsPirateNightmare juego) {
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
        crearFantasma2();
        crearFantasma3();
        crearMoneda();
        crearTumba();
        crearIconoContadorMonedas();
        crearItemCorazon();
        crearBotonPausa();
        crearRectanguloColisionEdward();
        crearSonidos();
        //recuperarInfoNivel();
        procesadorEntrada = new ProcesarEntrada();

        //Bloquear tecla back
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        //Reproducir Musica nivel 1
        juego.reproducirMusica(EdwardsPirateNightmare.TipoMusica.NIVEL_2); //reproducir m??sica del nivel 2

        //Pone el input Processor
        Gdx.input.setInputProcessor(procesadorEntrada);
    }

    private void recuperarInfoNivel() {
        Preferences prefs = Gdx.app.getPreferences("Puntaje2");
        puntos = prefs.getInteger("puntos2", 0);
        contadorMonedas = prefs.getInteger("contadorMonedas2", 0);
    }

    private void crearRectanguloColisionEdward() {
        rectColisionE = new Rectangle();
        rectColisionE.setX(edward.sprite.getX()); //El rectangulo inicia exactamente d??nde se encuentra Edward
        rectColisionE.setY(edward.sprite.getY()); //Iniciar la coordenada y del rect??ngulo en los pies de edward
        rectColisionE.setWidth(edward.getX());//
    }

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
        botonPausa = new BotonPausa(texturaBotonPausa, 0, ALTO - texturaBotonPausa.getHeight()); //Dibujar el bot??n de pausa en la esquina superior izquierda
        botonPausa.sprite.setScale(0.9f); //hacer el bot??n pausa 1.5 veces mas grande.
    }

    private void crearItemCorazon() {
        texturaEscudo = assetManager.get("sprites/Escudo.png");
        arrItemEscudo = new Array<>(2); // Solo apareceran dos corazones que ayudar??n al jugador a recuperar salud.
    }

    private void crearIconoContadorMonedas() {
        iconoContadorMonedas = new Moneda(texturaMoneda, ANCHO / 2 - texturaMoneda.getWidth(), ALTO * 0.95f - texturaMoneda.getHeight());
        iconoContadorMonedas.sprite.setScale(2);
    }

    private void crearMoneda() {
        texturaMoneda = assetManager.get("sprites/coin.png");
        arrMonedas = new Array<>(5); // M??ximo 5 monedas aparecer??n por nivel
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
        texturaNoche = assetManager.get("pantallas/Fondo2.png");
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }

    private void crearFantasma1() {
        arrFantasma1 = new Array<>();
        texturaFantasma1 = assetManager.get("sprites/fantasma1/fantasmaAnimacion.png");
    }

    private void crearFantasma2() {
        arrFantasma2 = new Array<>();
        texturaFantasma2 = assetManager.get("sprites/fantasma2/fantasma2Animacion.png");
    }

    private void crearFantasma3() {
        arrFantasma3 = new Array<>(); //
        texturaFantasma3 = assetManager.get("sprites/fantasma3/boss_edward.png");
    }

    private void crearEdward() {
        texturaEdward = assetManager.get("sprites/edwardRun.png");
        edward = new Edward(texturaEdward, 20, 50);
    }

    private Button crearBoton(String imagen) {
        Texture texturaBoton = new Texture(imagen); // cargar imag??n del bot??n.
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
        batch.draw(texturaNoche, xFondo, 0);
        batch.draw(texturaNoche, xFondo + texturaNoche.getWidth(), 0);
        //Dibuja personaje
        edward.render(batch);
        //Dibujar marcador
        texto.mostrarMensaje(batch, Integer.toString(puntos), 0.83f * ANCHO, 0.95f * ALTO);
        //Dibujar bot??n Pausa
        botonPausa.render(batch);
        //Dibujar corazon
        for (Corazon corazon : arrCorazones) {
            corazon.render(batch);
        }
        //Dibujar fantasma1
        for (Fantasma1 fantasma1 : arrFantasma1) {
            fantasma1.render(batch);
        }
        //Dibujar fantasma2
        for (Fantasma2 fantasma2 : arrFantasma2) {
            fantasma2.render(batch);
        }
        //Dibujar Jefe Final
        for (Fantasma3 fantasma3 : arrFantasma3) {
            fantasma3.render(batch);
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
            estadoJuego = EstadoJuego.CAMBION;
            tiempoSalida -= delta; // Comenzar a restar del tiempo de salida
            //Gdx.input.setInputProcessor(null); // dejar de procesar lo que haga el usuario
            edward.moverDerecha(delta * 2);
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverDerecha(-delta);
            }
            for (Fantasma2 fantasma2 : arrFantasma2) {
                fantasma2.moverDerecha(-delta);
            }
            for (Fantasma3 fantasma3 : arrFantasma3) {
                fantasma3.moverDerecha(-delta);
            }
            for (Moneda moneda : arrMonedas) {
                moneda.moverDerecha(-delta);
            }
            //Quitar balas de la pantalla
            for (int i = arrBalas.size - 1; i >= 0; i--) {
                Bala bala = arrBalas.get(i);
                bala.mover(-delta * 3);
            }
            Preferences preferencias = Gdx.app.getPreferences("Puntaje2");
            preferencias.putInteger("puntos2", puntos);
            preferencias.putInteger("contadorMonedas2", contadorMonedas);
            preferencias.flush(); //Se guardan en memoria no vol??til
        }
        if (tiempoSalida <= 3) {
            juego.setScreen(new PantallaFinal(juego));
        }
        batch.end();

        //Dibujar la pausa
        if (estadoJuego == EstadoJuego.PAUSADO && escenaPausa != null) { // Si el usuario ya toc?? el bot??n de pausa
            escenaPausa.draw(); // dibujar pausa
        }
    }

    private void actualizar(float delta) {
        //Actualizar solo cuando se est?? jugando
        if (estadoJuego == EstadoJuego.JUGANDO) {
            actualizarFondo();
            actualizarFantasma1(delta);
            actualizarFantasma2(delta);
            actualizarFantasma3(delta);
            actualizarBalas(delta); //Fantasma1
            actualizarBalas2(delta); //Fantasma2
            actualizarBalas3(delta);
            verificarColisionFantasma();
            verificarColisionFantasma2();
            verificarColisionFantasma3();
            actualizarTiempo(delta);
            verificarSalud();
            actualizarMonedas(delta);
            verificarColisionMoneda();
            actualizarItemCorazon(delta);
            verificarColisionItemCorazon();
            actualizarRectanguloDeColision();
        }
        if (estadoJuego == EstadoJuego.CAMBION) {
            actualizarBalas(delta);
            actualizarTiempo(delta);
            actualizarMonedas(delta);
            actualizarItemCorazon(delta);
        }
    }

    private void verificarSalud() {
        if (arrCorazones.size == 0) {
            banderaMuerte = true;
        }
    }

    private void verificarColisionItemCorazon() {
        for (int i = arrItemEscudo.size - 1; i >= 0; i--) {
            Corazon corazon = arrItemEscudo.get(i);
            // si la moneda y Edward chocan
            if (corazon.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                arrCorazones.add(corazon); // a??adir un escudo que bloquea un golpe
                arrItemEscudo.removeIndex(i); //Remover el corazon
            }
        }
    }

    private void verificarColisionMoneda() {
        if (timerNivel < 118) {
            for (int i = arrMonedas.size - 1; i >= 0; i--) {
                Moneda moneda = arrMonedas.get(i);
                // si la moneda y Edward chocan
                if (moneda.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                    contadorMonedas += 1; // Sumar uno al contador de monedas
                    sonidoMonedaRecogida.play(); //Reproducir sonido de moneda recogida
                    arrMonedas.removeIndex(i); //Remover las monedas
                }
            }
        }
    }

    private void verificarColisionFantasma() {
        if (timerNivel < 118) {
            for (int i = arrCorazones.size - 1; i >= 0; i--) {
                for (int j = arrFantasma1.size - 1; j >= 0; j--) {
                    Fantasma1 fantasma = arrFantasma1.get(j);
                    //si edward choca con un fantasma
                    if (fantasma.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                        edwardLastimado.play(); //reproducir sonido cuando edward es lastimado
                        arrCorazones.removeIndex(i); //edward pierde un corazon
                        arrFantasma1.removeIndex(j); //el fantasma desaparece
                    }
                }
            }
        }
    }

    private void verificarColisionFantasma2() {
        if (timerNivel < 118) {
            for (int i = arrCorazones.size - 1; i >= 0; i--) {
                for (int j = arrFantasma2.size - 1; j >= 0; j--) {
                    Fantasma2 fantasma2 = arrFantasma2.get(j);
                    //si edward choca con un fantasma
                    if (fantasma2.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                        edwardLastimado.play(); //reproducir sonido cuando edward es lastimado
                        arrCorazones.removeIndex(i); //edward pierde un corazon
                        arrFantasma2.removeIndex(j); //el fantasma desaparece

                    }

                }
            }
        }
    }

    private void verificarColisionFantasma3() {
        if (timerNivel < 118) {
            for (int i = arrCorazones.size - 1; i >= 0; i--) {
                for (int j = arrFantasma3.size - 1; j >= 0; j--) {
                    Fantasma3 fantasma3 = arrFantasma3.get(j);
                    //si edward choca con un fantasma
                    if (fantasma3.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())) {
                        edwardLastimado.play(); //reproducir sonido cuando edward es lastimado
                        arrCorazones.removeIndex(i); //edward pierde un corazon
                        arrFantasma3.removeIndex(j); //el fantasma desaparece
                    }
                }
            }
        }
    }

    private void actualizarItemCorazon(float delta) {
        timerCrearEscudo += delta;
        if (timerCrearEscudo > TIEMPO_CREAR_ITEM_ESCUDO) {
            timerCrearEscudo = 0;
            float xCorazon = MathUtils.random(ANCHO, ANCHO + 1.5f);
            float yCorazon = MathUtils.random(80, 210);
            Corazon itemCorazon = new Corazon(texturaEscudo, xCorazon, yCorazon);
            arrItemEscudo.add(itemCorazon);
        }
        // mover corazones
        for (Corazon corazon : arrItemEscudo) {
            corazon.moverIzquierda(delta);
        }
    }

    private void actualizarMonedas(float delta) {
        timerCrearMoneda += delta;
        if (timerCrearMoneda > TIEMPO_CREAR_MONEDA) {  //Si es momento de crear una moneda
            timerCrearMoneda = 0;
            float xMoneda = MathUtils.random(ANCHO, ANCHO + 1.5f);
            float yMoneda = MathUtils.random(30, 210); // La y de las monedas ser?? un n??mero aleatorio entre 20 y 250
            Moneda moneda = new Moneda(texturaMoneda, xMoneda, yMoneda);
            moneda.sprite.setScale(1.5f); // Hacer la moneda 1.5 m??s grande
            arrMonedas.add(moneda);
        }
        //Mover monedas
        for (Moneda moneda : arrMonedas) {
            moneda.moverIzquierda(delta); // mover monedas
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
                fantasma1.moverIzquierda(delta * 2);
            }
        } else if (timerNivel < 90) {
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 2.5f); // los fantasmas son 2 veces m??s r??pidos!
            }
        } else if (timerNivel < 120) {
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 3); // los fantasmas son 5 veces m??s r??pidos!
            }
        } else {
            //No mover fantasmas en los ultimos 10 segundos de salida del nivel
            arrFantasma1.clear();
        }
    }

    private void actualizarFantasma2(float delta) {
        timerCrearFantasma2 += delta;
        if (timerCrearFantasma2 > TIEMPO_CREAR_FANTASMA2) {
            timerCrearFantasma2 = 0;
            //crear fantasma1
            float xFantasma2 = MathUtils.random(ANCHO, ANCHO * 1.5f);
            float yFantasma2 = MathUtils.random(30, 210);
            Fantasma2 fantasma2 = new Fantasma2(texturaFantasma2, xFantasma2, yFantasma2);
            arrFantasma2.add(fantasma2);
        }

        //Mover al fantasma1
        if (timerNivel < 50) {  // Los fantasmas van a una velocidad normal
            for (Fantasma2 fantasma2 : arrFantasma2) {
                fantasma2.moverIzquierda(delta * 2);
            }
        } else if (timerNivel > 70 && timerNivel < 100) {
            for (Fantasma2 fantasma2 : arrFantasma2) {
                fantasma2.moverIzquierda(delta * 3); // los fantasmas son 2 veces m??s r??pidos!
            }
        } else if (timerNivel < 110) {
            for (Fantasma2 fantasma2 : arrFantasma2) {
                fantasma2.moverIzquierda(delta * 3.5f); // los fantasmas son 5 veces m??s r??pidos!
            }
        } else {
            //limpiar arreglo de fantasmas y
            arrFantasma2.clear();
        }
    }

    private void actualizarFantasma3(float delta) {
        timerCrearFantasma3 += delta;
        if (timerCrearFantasma3 > TIEMPO_CREAR_FANTASMA3) {
            timerCrearFantasma3 = 0;
            //Crear fantasma3
            float xFantasma3 = MathUtils.random(ANCHO, ANCHO * 1.5f);
            float yFantasma3 = MathUtils.random(30, 210);
            Fantasma3 fantasma3 = new Fantasma3(texturaFantasma3, xFantasma3, yFantasma3);
            arrFantasma3.add(fantasma3);
        }
        //Mover fantasma3
        for (Fantasma3 fantasma3 : arrFantasma3) {
            fantasma3.moverIzquierda(delta);
            fantasma3.rebote(delta);
        }
    }

    private void actualizarTiempo(float delta) {
        timerNivel += delta; //acumular tiempo en el timer
        if (timerNivel >= tiempoNivel) {
            banderaFinNivel = true;
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
                    if (bala.sprite.getBoundingRectangle().overlaps(fantasma1.sprite.getBoundingRectangle())) {
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

    private void actualizarBalas2(float delta) {
        for (int i = arrBalas.size - 1; i >= 0; i--) {
            Bala bala = arrBalas.get(i);
            bala.mover(delta);
            //prueba si la bola debe de desaparecer si se sale de la pantalla
            if (bala.getX() > ANCHO) {
                arrBalas.removeIndex(i);
            } else {
                for (int iA = arrFantasma2.size - 1; iA >= 0; iA--) {
                    Fantasma2 fantasma2 = arrFantasma2.get(iA);
                    if (bala.sprite.getBoundingRectangle().overlaps(fantasma2.sprite.getBoundingRectangle())) {
                        //Contar Puntos
                        puntos += 200;
                        //Borrar bala
                        arrBalas.removeIndex(i);
                        sonidoFantasma.play(); //Reproducir sonido de muerte del fantasma
                        arrFantasma2.removeIndex(iA);
                        break;
                    }
                }
            }
        }
    }

    private void actualizarBalas3(float delta) {
        for (int i = arrBalas.size - 1; i >= 0; i--) {
            Bala bala = arrBalas.get(i);
            bala.mover(delta);
            //prueba si la bola debe de desaparecer si se sale de la pantalla
            if (bala.getX() > ANCHO) {
                arrBalas.removeIndex(i);
            } else {
                for (int iA = arrFantasma3.size - 1; iA >= 0; iA--) {
                    Fantasma3 fantasma3 = arrFantasma3.get(iA);
                    if (bala.sprite.getBoundingRectangle().overlaps(fantasma3.sprite.getBoundingRectangle())) {
                        //Contar Puntos
                        puntos += 350;
                        //Borrar bala
                        arrBalas.removeIndex(i);
                        sonidoFantasma.play(); //Reproducir sonido de muerte del fantasma
                        arrFantasma3.removeIndex(iA);
                        break;
                    }
                }
            }
        }
    }

    private void actualizarFondo() {
        xFondo -= 4;
        if (xFondo <= -texturaNoche.getWidth()) {
            xFondo = 0;
        }
    }

    private void crearTumba() {
        texturaTumba = assetManager.get("sprites/soloHeart.png");
        f = new Tumba(texturaTumba, edward.getX(), edward.getY());
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
        assetManager.unload("sprites/Bala_Plasma.png");
        assetManager.unload("sprites/heart.png");
        assetManager.unload("sprites/Escudo.png");
        assetManager.unload("sprites/fantasma1/fantasmaAnimacion.png");
        assetManager.unload("sprites/fantasma3/boss_edward.png");
        assetManager.unload("sprites/soloHeart.png");

        //borrar pantallas
        assetManager.unload("pantallas/pantallaFinNivelOp.png");
        assetManager.unload("pantallas/P1GameOverAlt.png");
        assetManager.unload("pantallas/Fondo2.png");

        //cargar botones
        assetManager.unload("pausa/botonContinuar.png");
        assetManager.unload("pausa/botonMenu.png");
        assetManager.unload("pausa/FondoPPausa.png");
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
            if (estadoJuego == EstadoJuego.JUGANDO) {
                if (v.x >= ANCHO / 2) {
                    //Dispara
                    Bala bala = new Bala(texturaBala, edward.sprite.getX() + edward.sprite.getWidth() * 1 / 2, edward.getY() + edward.sprite.getHeight() * 1 / 2);
                    arrBalas.add(bala);
                    sonidoBala.play();
                    //Verificar boton pausa

                }
                if (v.x > botonPausa.sprite.getX() && v.x <= 0 + botonPausa.sprite.getWidth() &&
                        v.y < botonPausa.sprite.getY() + botonPausa.sprite.getHeight() && v.y > botonPausa.sprite.getY()
                        && escenaPausa == null) { //Solo crear la escena de pausa cu??ndo no existe un objeto previo
                    escenaPausa = new EscenaPausa(vista); // crear nueva escena Pausa
                    estadoJuego = EstadoJuego.PAUSADO;
                    edward.cambiaAnimaci??n(estadoJuego); // pausar a edward
                    Gdx.input.setInputProcessor(escenaPausa); // Cambiar el input Processor a escena pausa

                }
                if (v.x <= ANCHO / 2 && v.y < ALTO - botonPausa.sprite.getHeight()) { // Saltar al tocar el lado izquierdo de la pantalla y cuando no se toca el boton de pausa
                    if (edward.devolverEstado() == EstadoEdward.CAMINANDO) { //solo reproducir cuando esta en el suelo, evitar multiples reproducciones
                        edwardSalto.play(); //Reproducir efecto de sonido para el salto
                    }
                    edward.saltar();
                }
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
        if (edward.devolverEstado() == EstadoEdward.SALTANDO) {  // Si edward est?? saltando, actualizar constantemente su rect??ngulo de colisi??n
            rectColisionE.setHeight(edward.sprite.getBoundingRectangle().getHeight() + edward.sprite.getHeight());
        }
    }

    private class EscenaPausa extends Stage {
        private Texture texturaFondo;

        public EscenaPausa(final Viewport vista) {
            super(vista); // Usar constructor de super clase stage
            texturaFondo = assetManager.get("pausa/FondoPPausa.png");
            Image imgFondo = new Image(texturaFondo);
            //Mostrar imagen en el centro de la pantalla
            imgFondo.setPosition(ANCHO / 2, ALTO / 2 - 30, Align.center);

            // Crear botones para la pantalla pausa usando el m??todo crear bot??n
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
                    edward.cambiaAnimaci??n(estadoJuego);
                    Gdx.input.setInputProcessor(procesadorEntrada);
                    escenaPausa = null; // Hacer que sea posible seleccionar la pausa de nuevo.

                }
            });

            //Listener de boton men??
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



