package mx.itesm.emerald;

import com.badlogic.gdx.graphics.Texture;

// Representa los corazones que aparecen en el juego como objetos y los que representan la vida del jugador
public class Corazon extends Objeto {

    float vX = - 700;

    public Corazon(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    public float getX() {
        return sprite.getX();
    }

    public void moverIzquierda(float delta){
        float dx = vX * delta;
        sprite.setX(sprite.getX() + dx);

    }
}
