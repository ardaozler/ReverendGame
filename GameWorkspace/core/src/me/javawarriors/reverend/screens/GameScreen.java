package me.javawarriors.reverend.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import me.javawarriors.reverend.ReverendGame;
import me.javawarriors.reverend.entities.Blood;
import me.javawarriors.reverend.entities.Bullet;
import me.javawarriors.reverend.entities.Healing;
import me.javawarriors.reverend.entities.Mob1;
import me.javawarriors.reverend.entities.Mob2;
import me.javawarriors.reverend.entities.Player;
import me.javawarriors.reverend.entities.Shield;
import me.javawarriors.reverend.entities.SpiderBoss;
import me.javawarriors.reverend.entities.Trap;
import me.javawarriors.reverend.entities.miniBoss;
import me.javawarriors.reverend.entities.sBullet;

public class GameScreen implements Screen {

	// Shader
	private ShaderProgram shader;
	private boolean shaderON = false;
	// map
	private TiledMap map;
	// cam
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	// game
	private Mob1 mob1a;
	private Mob2 mob2a;
	private miniBoss miniBoss;
	private SpiderBoss spiderBoss;

	private Player player;
	private Trap trap;
	private Healing heal;
	private ArrayList<Blood> bloods;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> pbullets;
	private ArrayList<Bullet> mbullets;
	private ArrayList<sBullet> sbullets;
	private ArrayList<Shield> shields;
	private ArrayList<Mob1> mob1s;
	private ArrayList<Mob2> mob2s;
	private ArrayList<miniBoss> miniBosses;
	private ArrayList<SpiderBoss> spiderBosses;
	private ArrayList<Trap> traps;
	private ArrayList<Healing> heals;
	ReverendGame game;

	// music
	public Music music;
	boolean bgmusic = false;

	public GameScreen(ReverendGame game) {

		this.game = game;
		bloods = new ArrayList<Blood>();
		shields = new ArrayList<Shield>();
		bullets = new ArrayList<Bullet>();
		pbullets = new ArrayList<Bullet>();
		mbullets = new ArrayList<Bullet>();
		sbullets = new ArrayList<sBullet>();
		mob1s = new ArrayList<Mob1>();
		miniBosses = new ArrayList<miniBoss>();
		spiderBosses = new ArrayList<SpiderBoss>();
		mob2s = new ArrayList<Mob2>();
		traps = new ArrayList<Trap>();
		heals = new ArrayList<Healing>();
		music = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));
		music.setVolume(0.2f);
		music.setLooping(true);
	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(0, 0, 0, 1);

		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("map1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 4f);
		player = new Player((TiledMapTileLayer) map.getLayers().get(3), this);
		camera = new OrthographicCamera();

		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("shaders/vignette.vsh"),
				Gdx.files.internal("shaders/vignette.fsh"));
		System.out.println(shader.isCompiled() ? "shader compiled." : shader.getLog());

		// Tutorial room
		trap = new Trap(player, this, 126, 3136);
		heal = new Healing(player, this, 1150, 3712);

		// first mob room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 3596, 1851, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 4351, 2336, 50);
		trap = new Trap(player, this, 4160, 2048);
		trap = new Trap(player, this, 4160, 2432);

		// second room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 7567, 2889, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 9021, 2616, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 8836, 1761, 50);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 9021, 1761, 200, 20, 40, "bebe1.png", 11, 12);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 7567, 2616, 200, 20, 40, "bebe1.png", 11, 12);
		trap = new Trap(player, this, 7423, 2368);
		trap = new Trap(player, this, 8448, 2944);
		trap = new Trap(player, this, 8768, 2432);
		trap = new Trap(player, this, 8448, 1792);
		heal = new Healing(player, this, 8196, 3200);

		// 2 kapili room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 4219, 5315, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 5673, 4750, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 5129, 5385, 50);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 5129, 5385, 500, 10, 40, "yesilbebe.png", 12, 12);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 5673, 4750, 500, 10, 40, "yesilbebe.png", 12, 12);
		trap = new Trap(player, this, 5184, 4608);
		trap = new Trap(player, this, 3776, 4480);
		trap = new Trap(player, this, 5696, 4864);

		// first to the left

		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 457, 4401, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 1341, 4475, 50);
		trap = new Trap(player, this, 3904, 6143);
		trap = new Trap(player, this, 1216, 4544);
		trap = new Trap(player, this, 320, 4672);
		trap = new Trap(player, this, 960, 5120);
		heal = new Healing(player, this, 2752, 6275);

		// fake boss room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 1173, 8250, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 2120, 8950, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 2113, 7551, 50);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 1173, 8250, 150, 40, 60, "kirmizibebe.png", 12, 14);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 2120, 8950, 150, 40, 60, "kirmizibebe.png", 12, 14);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 2113, 7551, 150, 40, 60, "kirmizibebe.png", 12, 14);
		miniBoss = new miniBoss((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0),
				this, "miniBoss", 2113, 7551, 50, 500);
		trap = new Trap(player, this, 704, 7616);
		trap = new Trap(player, this, 960, 8448);
		trap = new Trap(player, this, 1856, 8384);
		trap = new Trap(player, this, 1856, 7552);
		trap = new Trap(player, this, 384, 8448);
		trap = new Trap(player, this, 1088, 9088);
		trap = new Trap(player, this, 2176, 9152);
		heal = new Healing(player, this, 1280, 9344);

		// sagdan ilk room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 7269, 4986, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 8118, 4278, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 8134, 5484, 50);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 8134, 4278, 200, 20, 40, "bebe1.png", 11, 12);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 7269, 4986, 200, 20, 40, "bebe1.png", 11, 12);
		trap = new Trap(player, this, 6656, 5440);
		trap = new Trap(player, this, 7680, 4928);
		trap = new Trap(player, this, 8192, 4288);
		trap = new Trap(player, this, 8320, 5120);

		// sagdan ikinci room
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 8778, 8919, 50);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 7764, 8271, 50);
		trap = new Trap(player, this, 8896, 8128);
		trap = new Trap(player, this, 7808, 8256);
		trap = new Trap(player, this, 8512, 8768);

		// Boss room
		heal = new Healing(player, this, 4672, 9408);
		spiderBoss = new SpiderBoss((TiledMapTileLayer) map.getLayers().get(3),
				(TiledMapTileLayer) map.getLayers().get(0), this, "spiderBoss", 4672, 7776, 550, 500);

		renderer.getBatch().setShader(shader);
	}

	@Override
	public void render(float delta) {
		if (!bgmusic) {
			music.play();
			bgmusic = true;
		}
		// camera.viewportHeight = 1920;
		// camera.viewportWidth = 1080;
		// camera.update();

		shader.setUniformf("innerRadius", (float) player.getHP() / 100);
		if (player.getHP() < 60) {
			shader.setUniformf("intensity", 0.9f);
		} else {
			shader.setUniformf("intensity", 0f);
		}
		shader.setUniformf("u_resolution", 1920, 1080);

		bullets.addAll(pbullets);
		bullets.addAll(mbullets);
		// Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.Update(Gdx.graphics.getDeltaTime());

		// CAmera Smooth Follow lerp değerini değiştirerek kamera follow hızı
		// değiştri=========================

		float lerp = 10f;
		Vector3 position = camera.position;
		// position.x += (player.getX() - position.x + player.getWidth() / 2) * lerp *
		// Gdx.graphics.getDeltaTime();
		// position.y += (player.getY() - position.y + player.getHeight() / 2) * lerp *
		// Gdx.graphics.getDeltaTime();

		// sabit
		// cam============================================================================================
		camera.setToOrtho(false);
		camera.position.set(player.getX(), player.getY(), 0);

		camera.update();
		renderer.setView(camera);
		renderer.render();
		renderer.getBatch().begin();

		for (Trap trap : traps) {
			trap.update(Gdx.graphics.getDeltaTime());
			renderer.getBatch().draw(trap.GetFrame(), trap.getX(), trap.getY(), 16 * 4, 16 * 4);
		}

		for (Healing heal : heals) {
			heal.update(Gdx.graphics.getDeltaTime());
			renderer.getBatch().draw(heal.GetFrame(), heal.getX(), heal.getY(), 16 * 4, 33 * 4);
			if (heal.inHealRange()) {
				heal.render((SpriteBatch) renderer.getBatch());

			}
		}

		for (Blood blood : bloods) {
			blood.render((SpriteBatch) renderer.getBatch());
		}

		for (Mob1 mob : mob1s) {
			mob.Update(Gdx.graphics.getDeltaTime());
			if (mob.isDead()) {
				mob.setInactive();
				;
			}
		}
		for (miniBoss miniBoss : miniBosses) {
			miniBoss.Update(Gdx.graphics.getDeltaTime());
			if (miniBoss.isDead()) {
				miniBoss.setInactive();
			}
		}

		for (Mob2 mob : mob2s) {
			mob.Update(Gdx.graphics.getDeltaTime());
			if (mob.isDead()) {
				mob.setInactive();
			}
		}

		for (SpiderBoss spiderBoss : spiderBosses) {
			spiderBoss.Update(Gdx.graphics.getDeltaTime());
			if (spiderBoss.isDead()) {
				spiderBoss.setInactive();
			}
		}

		for (miniBoss miniBoss : miniBosses) {
			miniBoss.render((SpriteBatch) renderer.getBatch());
		}

		for (Mob1 mob1 : mob1s) {
			mob1.render((SpriteBatch) renderer.getBatch());
		}

		for (Mob2 mob2 : mob2s) {
			mob2.render((SpriteBatch) renderer.getBatch());
		}

		for (Bullet bullet : bullets) {
			bullet.render((SpriteBatch) renderer.getBatch());
		}

		for (sBullet bullet : sbullets) {
			bullet.render((SpriteBatch) renderer.getBatch());
		}

		for (SpiderBoss spiderBoss : spiderBosses) {
			spiderBoss.render((SpriteBatch) renderer.getBatch());
		}

		// HUD
		renderer.getBatch().draw(player.GetDashFrame(), camera.position.x - 658,
				camera.position.y - this.camera.viewportWidth / 4 + 39, 17 * 3f, 18 * 3f);
		renderer.getBatch().draw(player.GetAttackFrame(), camera.position.x - 595,
				camera.position.y - this.camera.viewportWidth / 4 + 39, 17 * 3f, 18 * 3f);
		renderer.getBatch().draw(player.GetShieldFrame(), camera.position.x - 531,
				camera.position.y - this.camera.viewportWidth / 4 + 39, 17 * 3f, 18 * 3f);
		renderer.getBatch().draw(player.GetHealthFrame(), camera.position.x - 800,
				camera.position.y - this.camera.viewportWidth / 4, 72 * 5, 25 * 5);

		// miniBossHealth
		if (miniBoss.isShowHealthBar()) {
			renderer.getBatch().draw(miniBoss.GetBossHealthFrame(), camera.position.x, camera.position.y + 390, 49 * 3f,
					10 * 3f);
		}
		// SpiderBossHealth
		if (spiderBoss.isShowHealthBar()) {
			renderer.getBatch().draw(spiderBoss.GetBossHealthFrame(), camera.position.x, camera.position.y + 390,
					49 * 3f, 10 * 3f);
		}
		// player
		renderer.getBatch().draw(player.GetFrame(), player.getX(), player.getY(), player.getWidth(),
				player.getHeight());

		for (Shield shield : shields) {
			shield.render((SpriteBatch) renderer.getBatch());
		}

		// renderer.getBatch().draw(mob1.GetFrame(), mob1.getX(), mob1.getY(),
		// mob1.getWidth(),
		// player.getHeight());

		renderer.getBatch().end();

		if (player.getHP() <= 0) {
			this.dispose();
			game.setScreen(new DeathScreen(game));
		}

		if (player.getHP() >= 1000) {
			this.dispose();
			game.setScreen(new GameEndScreen(game));
		}

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		// camera.update();
		shader.setUniformf("u_resolution", width, height);
	}

	public ArrayList<Shield> getShields() {
		return shields;
	}

	public void setShields(ArrayList<Shield> shields) {
		this.shields = shields;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public ArrayList<Bullet> getPbullets() {
		return pbullets;
	}

	public void setPbullets(ArrayList<Bullet> pbullets) {
		this.pbullets = pbullets;
	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public ArrayList<Bullet> getMbullets() {
		return mbullets;
	}

	public void setMbullets(ArrayList<Bullet> mbullets) {
		this.mbullets = mbullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	public ArrayList<miniBoss> getMiniBosses() {
		return miniBosses;
	}

	public void setMiniBosses(ArrayList<miniBoss> miniBosses) {
		this.miniBosses = miniBosses;
	}

	public ArrayList<sBullet> getSbullets() {
		return sbullets;
	}

	public void setSbullets(ArrayList<sBullet> sbullets) {
		this.sbullets = sbullets;
	}

	public ArrayList<Mob2> getMob2s() {
		return mob2s;
	}

	public void setMob2s(ArrayList<Mob2> mob2s) {
		this.mob2s = mob2s;
	}

	public ArrayList<Mob1> getMob1s() {
		return mob1s;
	}

	public void setMob1s(ArrayList<Mob1> mob1s) {
		this.mob1s = mob1s;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Trap> getTraps() {
		return traps;
	}

	public void setTraps(ArrayList<Trap> traps) {
		this.traps = traps;
	}

	public ArrayList<Healing> getHeals() {
		return heals;
	}

	public void setHeals(ArrayList<Healing> heals) {
		this.heals = heals;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	public boolean isBgmusic() {
		return bgmusic;
	}

	public void setBgmusic(boolean bgmusic) {
		this.bgmusic = bgmusic;
	}

	public ArrayList<Blood> getBloods() {
		return bloods;
	}

	public void setBloods(ArrayList<Blood> bloods) {
		this.bloods = bloods;
	}

	public ArrayList<SpiderBoss> getSpiderBosses() {
		return spiderBosses;
	}

	public void setSpiderBosses(ArrayList<SpiderBoss> spiderBosses) {
		this.spiderBosses = spiderBosses;
	}

	@Override
	public void dispose() {
		player.getDashSfx().dispose();
		player.getWalking().dispose();
		music.dispose();
		spiderBoss.getBossmusic().dispose();
		map.dispose();
		renderer.dispose();

	}

}
