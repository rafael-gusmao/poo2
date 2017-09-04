package iceadventure;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class GamePlayState extends BasicGameState {

    static boolean fullscreen = false;
    static boolean showFPS = true;
    private boolean[][] blocked;
    static String title = "Slick2D Camera Tutorial";
    static int fpslimit = 60;
    TiledMap map;
    Hero player;
    Camera camera;
    int mapHeight, mapWidth;
    int tileHeight, tileWidth;
    int stateid;
    AnimalFlyWeight animalFactory;
    int bearX = 500;
    int bearY = 500;
    
    Bear bearMovimento; 
    Bear bearParado; 

    public GamePlayState(int id) {
        stateid = id;
    }

    @Override
    public int getID() {
        return stateid;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        map = new TiledMap("tiledmaps/IceAdventureMap.tmx");
        mapWidth = map.getWidth() * map.getTileWidth();
        mapHeight = map.getHeight() * map.getTileHeight();
        tileHeight = map.getTileHeight();
        tileWidth = map.getTileWidth();
        player = new Hero(tileWidth, tileHeight);
        camera = new Camera(map, mapWidth, mapHeight);
        blocked = new boolean[map.getWidth()][map.getHeight()];
        animalFactory = new AnimalFlyWeight();
        initializeBlocked();        
        bearMovimento = (Bear) animalFactory.getTipoAnimal("bear");        
        bearParado = (Bear) animalFactory.getTipoAnimal("bear");
        
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        player.update(gc, sbg, delta, this);
        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(IceAdventure.MENUSTATE);
        }
        bearX++;
        bearMovimento.setPosX(bearX);
        bearParado.setPosX(bearX);
        
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        
        camera.translate(g, player);
        map.render(0, 0);
        player.render();                            
        bearMovimento.render();
        bearMovimento.setPosX(600);
        bearMovimento.setPosY(600);
        bearParado.render();
        bearParado.setPosX(500);
        bearParado.setPosY(500);
        
    }

    public boolean isBlocked(float x, float y) {
        int xBlock = (int) x / map.getTileWidth();
        int yBlock = (int) y / map.getTileHeight();
        return blocked[xBlock][yBlock];
    }
    
    public Vector2f getHeroPosition() {
        return player.getpos();
    }
    
    public void setHeroPosition(Vector2f pos) {
        player.setpos(pos);
    }

    private void initializeBlocked() {
        for (int l = 0; l < map.getLayerCount(); l++) {
            String layerValue = map.getLayerProperty(l, "blocked", "false");
            if (layerValue.equals("true")) {
                for (int c = 0; c < map.getWidth(); c++) {
                    for (int r = 0; r < map.getHeight(); r++) {
                        if (map.getTileId(c, r, l) != 0) {
                            blocked[c][r] = true;
                        }
                    }
                }
            }
        }
    }
}