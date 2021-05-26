package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
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

    //Fondo infinito
    private Texture texturaFPlaya;
    private float xFondo = 0;

    //Personaje
    private Edward edward;
    private Texture texturaEdward;

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

    //Timer salida de edward del nivel

    private float tiempoSalida = 10;

    // Objeto Corazon
    private Texture texturaCorazon;
    private Array<Corazon> arrItemCorazon;
    private float timerCrearCorazon;
    private final float TIEMPO_CREAR_ITEM_CORAZON = 45; // crear un corazón cuando hayan pasado 40 segundos

    private final float tiempoNivel = 120; //El nivel dura dos minutos
    private float timerNivel = 0; // Timer que acumula el tiempo para determinar cuando termina el nivel

    //Disparo del personaje
    private Array<Bala> arrBalas;
    private Texture texturaBala;

    //Vidas del personaje
    private Array<Corazon> arrCorazones;

    // bandera de fin de nivel
    private boolean banderaFinNivel;

    //Estados del juego
    private EstadoNivel estadoNivel = EstadoNivel.Jugando; //Inicia jugando

    //Cambio de Nivel
    private EscenaCambio escenaCambio;


    public PantallaPlaya(EdwardsPirateNightmare juego) {
        this.juego = juego;
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
        //Pone el input Processor
        Gdx.input.setInputProcessor(new ProcesarEntrada());


    }

    private void crearItemCorazon() {
        texturaCorazon = new Texture("sprites/heart.png");
        arrItemCorazon = new Array<>(2); // Solo apareceran dos corazones que ayudarán al jugador a recuperar salud.
    }

    private void crearIconoContadorMonedas() {
        iconoContadorMonedas = new Moneda(texturaMoneda, ANCHO/2 - texturaMoneda.getWidth(), ALTO * 0.95f - texturaMoneda.getHeight());
        iconoContadorMonedas.sprite.setScale(2);
    }

    private void
    crearMoneda() {
        texturaMoneda = new Texture("sprites/coin.png");
        arrMonedas = new Array<>(5); // Máximo 5 monedas aparecerán por nivel

    }

    private void crearCorazon() {
        Texture texturaCorazon = new Texture("sprites/heart.png");
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
        texturaBala = new Texture("sprites/bala.png");
    }

    private void crearFondo() {
        texturaFPlaya = new Texture("pantallas/P1.png");
    }

    private void crearTexto() {
        texto = new Texto("fonts/pirate.fnt");
    }


    private void crearFantasma1() {
        arrFantasma1 = new Array<>();
        texturaFantasma1 = new Texture("sprites/fantasma1/fantasmaAnimacion.png");
    }

    private void crearEdward() {
        texturaEdward = new Texture("sprites/edwardRun.png");
        edward = new Edward(texturaEdward, 20, 50);
    }

    //Método para dibujar botones que se utilizarán en los diversos estados del juego
    private Button crearBoton(String imagen){
        Texture texturaBoton = new Texture(imagen); // cargar imagén del botón.
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable(texturaBoton); // tipo correcto del boton, drawable. No acepta texture

        return new Button(trdBtnPausa); // regesar imagen del boton Pausa
    }




    @Override
    public void render(float delta) {

        batch.begin();
        batch.setProjectionMatrix(camara.combined);

        actualizar(delta);  //Actualizar
        //Dibuja Fondo

        batch.draw(texturaFPlaya, xFondo, 0);

        batch.draw(texturaFPlaya, xFondo + texturaFPlaya.getWidth(), 0);

        //Dibuja personaje
        edward.render(batch);
        //Dibujar marcador
        texto.mostrarMensaje(batch, Integer.toString(puntos), 0.95f * ANCHO, 0.95f * ALTO);

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
        for(Moneda moneda: arrMonedas){
            moneda.render(batch);
        }
        // Dibujar contador monedas
            iconoContadorMonedas.render(batch);
            texto.mostrarMensaje(batch, Integer.toString(contadorMonedas), 0.50f * ANCHO, 0.95f * ALTO);

        //Dibujar item corazon

        for(Corazon itemCorazon: arrItemCorazon){
            itemCorazon.render(batch);
        }

        batch.end();

        if(banderaMuerte){
            juego.setScreen(new PantallaJuegoTerminado(juego));
        }
        if(banderaFinNivel){
            estadoNivel = EstadoNivel.cambioNivel;
            tiempoSalida -= delta; // Comenzar a restar del tiempo de salida
            Gdx.input.setInputProcessor(null); // dejar de procesar lo que haga el usuario
            edward.moverDerecha(delta*2);
            if(escenaCambio == null)
            {
                escenaCambio = new EscenaCambio(vista);
            }
            for (Fantasma1 fantasma1: arrFantasma1) {
                fantasma1.moverDerecha(-delta);
            }
            if(tiempoSalida <= 0) {
                //juego.setScreen(new PantallaFinNivel(juego));
                // Guardar puntos y monedas al terminar el nivel
                Preferences preferencias = Gdx.app.getPreferences("Puntaje");
                preferencias.putInteger("puntos", puntos);
                preferencias = Gdx.app.getPreferences("Monedas");
                preferencias.putInteger("contadorMonedas", contadorMonedas);
                preferencias.flush(); //Se guardan en memoria no volátil

            }
        }

        //Dibujar escena de cambio de nivel
        if (estadoNivel == EstadoNivel.cambioNivel && escenaCambio != null) {
            escenaCambio.draw();
        }

    }




    private void actualizar(float delta) {
        if (estadoNivel == EstadoNivel.Jugando) {
            actualizarFondo();
            actualizarFantasma1(delta);
            actualizarBalas(delta);
            verificarColisionFantasma();
            actualizarTiempo(delta);
            verificarSalud();
            actualizarMonedas(delta);
            verificarColisionMoneda();
            actualizarItemCorazon(delta);
            verificarColisionItemCorazon();
        }
        if(estadoNivel == EstadoNivel.cambioNivel)
        {
            actualizarFondo();
           // actualizarFantasma1(delta);
            actualizarBalas(delta);
            //verificarColisionFantasma();
            actualizarTiempo(delta);
            verificarSalud();
            actualizarMonedas(delta);
            //verificarColisionMoneda();
            actualizarItemCorazon(delta);
            verificarColisionItemCorazon();
        }
    }

    private void verificarColisionItemCorazon() {

        for(int i = arrItemCorazon.size-1; i >= 0; i--){
            Corazon corazon = arrItemCorazon.get(i);
            // si la moneda y Edward chocan
            if(corazon.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())){
                arrCorazones.add(corazon); // añadir un corazon para recuperar salud
                arrItemCorazon.removeIndex(i); //Remover el corazon


            }

        }

    }

    private void actualizarItemCorazon(float delta) {
        timerCrearCorazon += delta;
        if(timerCrearCorazon > TIEMPO_CREAR_ITEM_CORAZON) {
            timerCrearCorazon = 0;
            float xCorazon = MathUtils.random(ANCHO, ANCHO + 1.5f);
            float yCorazon = MathUtils.random(80, 210);
            Corazon itemCorazon = new Corazon(texturaCorazon, xCorazon, yCorazon);
            arrItemCorazon.add(itemCorazon);
        }
        // mover corazones

        for(Corazon corazon : arrItemCorazon){
            corazon.moverIzquierda(delta);
        }
    }



    private void verificarColisionMoneda() {
        for(int i = arrMonedas.size-1; i >= 0; i--){
            Moneda moneda = arrMonedas.get(i);
            // si la moneda y Edward chocan
            if(moneda.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())){
                contadorMonedas += 1; // Sumar uno al contador de monedas
                arrMonedas.removeIndex(i); //Remover las monedas


            }

        }

    }



    private void actualizarMonedas(float delta) {
        timerCrearMoneda += delta;

        if(timerCrearMoneda > TIEMPO_CREAR_MONEDA) {  //Si es momento de crear una moneda
            timerCrearMoneda = 0;
            float xMoneda = MathUtils.random(ANCHO, ANCHO+1.5f);
            float yMoneda = MathUtils.random(30,210); // La y de las monedas será un número aleatorio entre 20 y 250
            Moneda moneda = new Moneda(texturaMoneda, xMoneda, yMoneda);
            moneda.sprite.setScale(1.5f); // Hacer la moneda 1.5 más grande
            arrMonedas.add(moneda);
        }
        //Mover monedas
        for(Moneda moneda : arrMonedas) {
            moneda.moverIzquierda(delta); // mover monedas

        }

    }


    private void verificarSalud() {
        if (arrCorazones.size == 0){
                banderaMuerte = true;
            //juego.setScreen(new PantallaJuegoTerminado(juego));
        }
    }


    private void crearTumba() {
        texturaTumba = new Texture("sprites/soloHeart.png");
        f = new Tumba(texturaTumba,edward.getX(), edward.getY());
    }


    private void actualizarTiempo(float delta) {
        timerNivel += delta; //acumular tiempo en el timer
        if(timerNivel >= tiempoNivel) {
            banderaFinNivel = true;

        }
    }

    private void verificarColisionFantasma() {
            for(int i = arrCorazones.size-1; i >= 0; i--){
                for(int j = arrFantasma1.size-1; j >= 0; j-- ){
                    Fantasma1 fantasma = arrFantasma1.get(j);
                    //si edward choca con un fantasma
                    //if(fantasma.getX() == edward.getX() + edward.sprite.getWidth()/2 && fantasma.getY() <= edward.getY() + edward.sprite.getHeight()/2){
                        if(fantasma.sprite.getBoundingRectangle().overlaps(edward.sprite.getBoundingRectangle())){
                        arrCorazones.removeIndex(i);
                        arrFantasma1.removeIndex(j);

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
                    if (bala.sprite.getBoundingRectangle().overlaps(fantasma1.sprite.getBoundingRectangle())) {
                        //Contar Puntos
                        puntos += 150;
                        //Borrar bala
                        arrBalas.removeIndex(i);
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
        if(timerNivel < 50){  // Los fantasmas van a una velocidad normal
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta);
            }
        }else if (timerNivel < 90){
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 2); // los fantasmas son 2 veces más rápidos!
            }}else if (timerNivel < 120){
            for (Fantasma1 fantasma1 : arrFantasma1) {
                fantasma1.moverIzquierda(delta * 3); // los fantasmas son 5 veces más rápidos!
            }}else{
                //No mover fantasmas en los ultimos 10 segundos de salida del nivel
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
    public void dispose() {
        arrCorazones.clear();
        arrFantasma1.clear();
        arrBalas.clear();
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
                Bala bala = new Bala(texturaBala, edward.getSprite().getX(), edward.getSprite().getY() + edward.sprite.getHeight()/2);
                arrBalas.add(bala);
            } else {
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

    //Escena cuando cambia de nivel
    private class EscenaCambio extends Stage
    {
        private Texture texturaFondo;

        //Imagen ventana de cambio
        public EscenaCambio(Viewport vista)
        {
            super(vista);
            //texto.mostrarMensaje(batch,"Pulsa para continuar tu camino...",ANCHO/2,ALTO/2);

            //Boton para continuar
            Texture texturaBtn = new Texture("botones/button_nivel2.png");
            TextureRegionDrawable trd = new TextureRegionDrawable(texturaBtn);
            Button btn = new Button(trd);
            addActor(btn);
            btn.setPosition(ANCHO/2,0.4f*ALTO,Align.center);
            //Agrega listener del boton
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Cambiar la pantalla
                    //juego.setScreen(new Nivel2(juego));
                }
            } );
        }

    }
}



