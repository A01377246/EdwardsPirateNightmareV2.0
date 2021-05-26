package mx.itesm.emerald;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EdwardsPirateNightmare extends Game {

	//Asset Manager para cargar recursos

	private final AssetManager assetManager = new AssetManager(); // se encarga de cargar recursos

	private Music fondo;

		public void create(){
		// Mostrar primer pantalla
		setScreen(new PantallaMenu(this)); //Exclusivo de clase game

	}


	//Método accesor de Asset Manager
	public AssetManager getAssetManager(){
			return assetManager;
	}

	public void reproducirMusica(TipoMusica tipo){
			switch(tipo){

				case MENU:
					assetManager.load("musica/pirateCrew.wav", Music.class); // Programar la carga de la canción
					assetManager.finishLoading(); // cargar
					fondo = assetManager.get("musica/pirateCrew.wav");
					break;

				case NIVEL_1:
					assetManager.load("musica/pirateAttack.mp3", Music.class);
					assetManager.finishLoading();
					fondo = assetManager.get("musica/pirateAttack.mp3");
					break;

			}
			/*if(fondo.isPlaying()){
				fondo.stop();
			}*/

			fondo.play();

	}
	public void detenerMusica(){ // detiene la música
			fondo.stop();
	}

	public void dispose(){
			super.dispose();
			assetManager.clear();

	}

	public enum TipoMusica {
			NIVEL_1,
			NIVEL_2,
			MENU,
			TIENDA
	}
}
