package plugins.npcdemo;

import java.util.Random;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.config.AppConfig;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.grid.GridService;
import io.gamemachine.grid.Grid;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.TrackData;
import io.gamemachine.messages.TrackData.EntityType;
import io.gamemachine.messages.UserDefinedData;
import io.gamemachine.regions.ZoneService;
import io.gamemachine.util.Vector3;
import plugins.core.combat.VitalsHandler;
import plugins.core.combat.VitalsProxy;

public class NpcEntity extends GameMessageActor {

	private LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	private int worldOffset = 0;
	private String id;
	private Grid grid;
	private Vector3 position;
	private TrackData trackData;
	public long tickInterval = 40l;
	public float mapSize = 990f;
	public float startPoint = -1000f;
	public double speed = 5d;
	public Vector3 target;
	private Random rand;
	private long lastUpdate = 0;
	private VitalsProxy vitalsProxy;
	
	public NpcEntity(String playerId, String characterId, int mapSize, int startPoint) {
		id = playerId;
		this.mapSize = mapSize;
		this.startPoint = startPoint;
	}

	@Override
	public void awake() {
		worldOffset =  AppConfig.getWorldOffset();
		rand = new Random();
		grid = GridService.getInstance().getPlayerGrid(null, id);
		position = randVector();
		//logger.warning("npc "+position);
		target = randVector();
		initTrackData();
		lastUpdate = System.currentTimeMillis();
		vitalsProxy = VitalsHandler.get(id, ZoneService.defaultZone().name);
		scheduleOnce(tickInterval, "update");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
	}
	
	@Override
	public void onTick(String message) {
		if (vitalsProxy.vitals.dead == 1) {
			sendTrackData();
			lastUpdate = System.currentTimeMillis();
			scheduleOnce(tickInterval, "update");
			return;
		}
		
		move();
		double dist = position.distance2d(target);
		if (dist <= 2) {
			target = randVector();
		}
		lastUpdate = System.currentTimeMillis();
		sendTrackData();
		scheduleOnce(tickInterval, "update");
	}
	
	private void move() {
		double deltatime = deltaTime();
		Vector3 dir = Vector3.zero();
		dir.x = target.x - position.x;
		dir.z = target.z - position.z;
		dir.normalizeLocal();
		
		position.x += (dir.x * speed * deltatime);
		position.z += (dir.z * speed * deltatime);
	}

	private double deltaTime() {
		return (System.currentTimeMillis() - lastUpdate) / 1000d;
	}

	private void initTrackData() {
		trackData = new TrackData();
		trackData.id = id;
		trackData.entityType = EntityType.Npc;
		trackData.userDefinedData = new UserDefinedData();
	}

	private int toInt(double num) {
		return (int) Math.round(num * 100l);
	}
	
	private void sendTrackData() {
		trackData.setX(toInt(position.x + worldOffset));
		trackData.setY(toInt(position.y + worldOffset));
		trackData.setZ(toInt(position.z + worldOffset));

		grid.set(trackData);
	}

	private Vector3 randVector() {
		Vector3 np = new Vector3();
		np.x = randFloat(startPoint, startPoint+mapSize);
		np.z = randFloat(startPoint, startPoint+mapSize);
		np.y = 40f;
		return np;
	}

	private float randFloat(float min, float max) {
		return rand.nextInt((int) ((max - min) + 1)) + min;
	}
}