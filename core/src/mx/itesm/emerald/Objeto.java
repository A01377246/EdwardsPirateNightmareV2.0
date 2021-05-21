package mx.itesm.emerald;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Objeto {
    //Protected para que las clases que heredan puedan utilizar el sprite
    protected Sprite sprite;

    public Objeto(Texture textura, float x, float y){
        sprite = new Sprite(textura);
        sprite.setPosition(x,y);

    }
    // constructor por default
    public Objeto(){

    }

    // Dibujar objeto

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
}
