package mx.itesm.emerald;

//Representa el personaje que estara en el escenario

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Edward extends Objeto {

    private Animation<TextureRegion> animacionCorrer;
    private float timerAnimacion;

    //Salto
    private final float yBase = 54;     //Suelo, piso
    private float tAire;                //tiempo que lleva en el aire
    private float tVuelo;               //tiempo de vuelo tolta
    private final float v0y = 100;      //Componente en y de la velocidad
    private final float g = 50f;      //pixeles/s^2

    //Movimiento a la derecha
    private final float vx = 150;

    private EstadoEdward estado;

    public Edward(Texture textura, float x, float y) {
        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturas = region.split(160, 160);

        //Cuadros para caminar
        TextureRegion[] arrFramesCaminar = {texturas[0][1], texturas[0][2], texturas[0][3], texturas[1][0],
                texturas[1][1], texturas[1][2], texturas[1][3]};
        animacionCorrer = new Animation<TextureRegion>(0.1f, arrFramesCaminar);
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        //IDLE
        sprite = new Sprite(texturas[1][2]);
        sprite.setPosition(x, y);

        estado = EstadoEdward.CAMINANDO;
    }

    //Reescribimos el metodo render para mostrar la animacion.
    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        switch (estado) {
            case CAMINANDO:
                timerAnimacion += delta;
                TextureRegion frame = animacionCorrer.getKeyFrame(timerAnimacion);
                batch.draw(frame, sprite.getX(), sprite.getY());
                break;
            case SALTANDO:
                actualizar();
                super.render(batch);
                break;
        }
    }

    private void actualizar() {
        float delta = Gdx.graphics.getDeltaTime();
        tAire += 5 * delta;
        float y = yBase + v0y * tAire - 0.5f * g * tAire * tAire;
        sprite.setY(y);
        //como saber que ya termino la simulacion?
        if (tAire >= tVuelo || y <= yBase) {
            estado = EstadoEdward.CAMINANDO;
            sprite.setY(yBase);
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void saltar() {
        if (estado != EstadoEdward.SALTANDO) {
            tAire = 0;
            tVuelo = 2 * v0y / g;
            estado = EstadoEdward.SALTANDO;
        }
    }

    public void moverDerecha(float delta){ // este método solo se llama cuando termina el nivel para que Edward salga de la pantalla
        float dx = vx * delta;
        sprite.setX(dx + sprite.getX()); // actualizar posición

    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
