package mx.itesm.emerald;

import com.badlogic.gdx.graphics.Texture;

public class Escudo extends Objeto{
    // Representa los Escudos que aparecen en el juego como objetos y los que representan la vida del jugador


        float vX = - 700;

        public Escudo(Texture textura, float x, float y) {
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


