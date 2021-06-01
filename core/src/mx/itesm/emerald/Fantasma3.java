package mx.itesm.emerald;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Fantasma3 extends Objeto
{
    //Desplazamiento en cada frame
    private float ALTO = 720;
    private float velocidadX = -375;
    private float velocidadY = 335;

    public Fantasma3 (Texture textura,float x, float y)
    {
        super(textura, x, y);
    }


    public void moverIzquierda(float delta)
    {
        float dx = velocidadX*delta;
        sprite.setX(sprite.getX()+dx);
    }

    public void rebote(float delta) //Movimiento de rebote del fantasma 3
    {
        float dy = velocidadY*delta;
        sprite.setY(sprite.getY()+dy);

        if (sprite.getY()>=ALTO*0.8f-sprite.getHeight() || sprite.getY()<=30)
        {
            velocidadY = -velocidadY;
        }
    }
    public float getX()
    {
        return sprite.getX();
    }

    public float getY()
    {
        return sprite.getY();
    }
}
