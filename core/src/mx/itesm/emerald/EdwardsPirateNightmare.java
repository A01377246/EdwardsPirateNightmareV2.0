package mx.itesm.emerald;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EdwardsPirateNightmare extends Game {

		public void create(){
		// Mostrar primer pantalla
		setScreen(new PantallaMenu(this)); //Exclusivo de clase game
	}
}
