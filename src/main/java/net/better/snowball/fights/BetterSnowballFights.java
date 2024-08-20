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
	public static GameRules.Key<GameRules.IntRule> SNOWBALL_KNOCKBACK;

	public static GameRules.Key<GameRules.IntRule> EGG_DAMAGE;
	public static GameRules.Key<GameRules.BooleanRule> EGGS_ONLY_DAMAGE_PLAYERS;
	public static GameRules.Key<GameRules.IntRule> EGG_KNOCKBACK;

	public static GameRules.Key<GameRules.IntRule> BOBBER_DAMAGE;
	public static GameRules.Key<GameRules.BooleanRule> BOBBERS_ONLY_DAMAGE_PLAYERS;
	public static GameRules.Key<GameRules.IntRule> BOBBER_KNOCKBACK;
	public static GameRules.Key<GameRules.IntRule> FISHING_PULL_MULTIPLIER;

	public static final Logger LOGGER = LoggerFactory.getLogger("bettersnowballfights");

	@Override
	public void onInitialize() {
		SNOWBALL_DAMAGE = GameRuleRegistry.register("snowballDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));
		SNOWBALLS_ONLY_DAMAGE_PLAYERS = GameRuleRegistry.register("snowballsOnlyDamagePlayers", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
		SNOWBALL_KNOCKBACK = GameRuleRegistry.register("snowballKnockback", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));

		EGG_DAMAGE = GameRuleRegistry.register("eggDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));
		EGGS_ONLY_DAMAGE_PLAYERS = GameRuleRegistry.register("eggsOnlyDamagePlayers", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
		EGG_KNOCKBACK = GameRuleRegistry.register("eggKnockback", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));


		BOBBER_DAMAGE = GameRuleRegistry.register("fishingBobberDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));
		BOBBERS_ONLY_DAMAGE_PLAYERS = GameRuleRegistry.register("fishingBobberOnlyDamagePlayers", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));
		BOBBER_KNOCKBACK = GameRuleRegistry.register("fishingBobberKnockback", GameRules.Category.MISC, GameRuleFactory.createIntRule(0,0));
		FISHING_PULL_MULTIPLIER = GameRuleRegistry.register("fishingPullMultiplier", GameRules.Category.MISC, GameRuleFactory.createIntRule(1,1));

		LOGGER.info("\"Hey, catch!\" Minecraft now loaded with 'Better Snowball Fights'");
	}
}
