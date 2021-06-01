package mx.itesm.emerald;

import com.badlogic.gdx.graphics.Texture;

public class Bala extends Objeto {
    private float vX = 300;

    public Bala(Texture textura, float x, float y) {
        super(textura, x, y);
        sprite.setScale(2.5f); // Aumentar el tamaño de la bala de plasma.
    }


    //Mover a la derecha la bola de fuego
    public void mover(float delta) {
        float dx = vX * delta;
        sprite.setX(sprite.getX() + dx);


    }

    public float getX() {
        return sprite.getX();
    }
}
