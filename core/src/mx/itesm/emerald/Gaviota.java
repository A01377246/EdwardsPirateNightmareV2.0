package mx.itesm.emerald;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gaviota extends Objeto {

    //Esta clase representa a las gaviotas que pasarán volando ocasionalmente a lo largo del nivel 1

    private Animation<TextureRegion> animacionVolar;
    private float timerAnimacion;


    //Movimiento a la derecha
    private final float vx = 180;
    private boolean animarGaviota = true; // La gaviota siempre vuela a menos que el juego esté en pausa

    public Gaviota (Texture textura, float x, float y) {

        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturas = region.split(50, 33);

        //Cuadros para volar
        TextureRegion[] arrFramesVolar = {texturas[0][0], texturas[0][1], texturas[0][2], texturas[0][3]};
        animacionVolar = new Animation<TextureRegion>(0.1f, arrFramesVolar);
        animacionVolar.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;


        sprite = new Sprite(texturas[0][0]);
        sprite.setPosition(x,y);

        }


        //Reescribimos el metodo render para mostrar la animacion.
        public void render(SpriteBatch batch) {
            float delta = Gdx.graphics.getDeltaTime();

                    if(!animarGaviota){ // Si el juego está en pausa, no actualizar animación.
                        timerAnimacion = 0;
                    }
                    timerAnimacion += delta;
                    TextureRegion frame = animacionVolar.getKeyFrame(timerAnimacion);
                    batch.draw(frame, sprite.getX(), sprite.getY());

            }

            //Si está pausado, dejar de moverse


    public void moverDerecha(float delta){ // este método se llama para mover a la gaviota a la derecha
        float dx = vx * delta;
        sprite.setX(dx + sprite.getX()); // actualizar posición

    }


}
