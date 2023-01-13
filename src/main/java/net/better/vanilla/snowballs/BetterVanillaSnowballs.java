package net.better.vanilla.snowballs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterVanillaSnowballs implements ModInitializer {
	public static GameRules.Key<GameRules.IntRule> SNOWBALL_DAMAGE;

	public static final Logger LOGGER = LoggerFactory.getLogger("bettervanillasnowballs");

	@Override
	public void onInitialize() {
		SNOWBALL_DAMAGE = GameRuleRegistry.register("snowballDamage", GameRules.Category.MISC, GameRuleFactory.createIntRule(0));

		LOGGER.info("\"Hey, catch!\" Minecraft now loaded with 'Better Vanilla Snowballs'");
	}
}
