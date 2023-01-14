package net.better.snowball.fights;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterSnowballFights implements ModInitializer {
	public static GameRules.Key<GameRules.IntRule> SNOWBALL_DAMAGE;
	public static GameRules.Key<GameRules.BooleanRule> SNOWBALLS_ONLY_DAMAGE_PLAYERS;

	public static final Logger LOGGER = LoggerFactory.getLogger("bettersnowballfights");

	@Override
	public void onInitialize() {
		SNOWBALL_DAMAGE = GameRuleRegistry.register("snowballDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));
		SNOWBALLS_ONLY_DAMAGE_PLAYERS = GameRuleRegistry.register("snowballsOnlyDamagePlayers", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));


		LOGGER.info("\"Hey, catch!\" Minecraft now loaded with 'Better Snowball Fights'");
	}
}
