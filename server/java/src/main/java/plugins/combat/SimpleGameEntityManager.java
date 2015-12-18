package plugins.combat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.gamemachine.core.CharacterService;
import io.gamemachine.core.GameEntityManager;
import io.gamemachine.core.PlayerService;
import io.gamemachine.messages.BuildObject;
import io.gamemachine.messages.Character;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.StatusEffect;
import io.gamemachine.messages.Vitals;
import plugins.core.combat.ClientDbLoader;
import plugins.landrush.BuildObjectHandler;

public class SimpleGameEntityManager implements GameEntityManager {

	private static final Logger logger = LoggerFactory.getLogger(SimpleGameEntityManager.class);
	private List<Vitals> baseVitals;
	public Map<Integer, BuildObject> buildObjects = new ConcurrentHashMap<Integer, BuildObject>();
	private CombatDamage combatDamage;

	public SimpleGameEntityManager() {
		baseVitals = ClientDbLoader.getVitalsContainer().vitals;
		for (BuildObject buildObject : ClientDbLoader.getBuildObjects().getBuildObjectList()) {
			buildObjects.put(buildObject.templateId, buildObject);
		}

		combatDamage = new CombatDamage();
	}

	private Vitals getVitalsTemplate(Vitals.Template template) {
		for (Vitals vitals : baseVitals) {
			if (vitals.template == template) {
				return vitals.clone();
			}
		}
		throw new RuntimeException("Unable to find vitals template " + template.toString());
	}

	@Override
	public void OnCharacterCreated(Character character, Object data) {
		//Character.db().save(character);
	}

	@Override
	public Vitals getBaseVitals(String characterId) {
		Character character = CharacterService.instance().find(characterId);
		return getVitalsTemplate(character.vitalsTemplate);
	}

	@Override
	public Vitals getBaseVitals(String entityId, Vitals.VitalsType vitalsType) {
		if (vitalsType == Vitals.VitalsType.BuildObject) {
			Vitals vitals = getVitalsTemplate(Vitals.Template.BuildObjectTemplate);

			BuildObject buildObject = BuildObjectHandler.find(entityId);
			if (buildObject == null) {
				logger.warn("Null build object "+entityId);
				return vitals;
			}
			
			// ground blocks don't have template ids.  Should implement
			if (buildObjects.containsKey(buildObject.templateId)) {
				vitals.health = buildObjects.get(buildObject.templateId).health;
			}
			
			return vitals;
		} else {
			throw new RuntimeException("Invalid vitals type " + vitalsType.toString());
		}

	}

	@Override
	public int getEffectValue(StatusEffect statusEffect, String playerSkillId, String characterId) {
		return combatDamage.getEffectValue(statusEffect, playerSkillId, characterId);
	}

	@Override
	public void skillUsed(String playerSkillId, String characterId) {
		combatDamage.skillUsed(playerSkillId, characterId);
	}

	@Override
	public void OnPlayerConnected(String playerId) {
		logger.warn("OnPlayerConnect " + playerId);
		Player player = PlayerService.getInstance().find(playerId);
	}

	@Override
	public void OnPlayerDisConnected(String playerId) {
		// TODO Auto-generated method stub

	}

}