package net.better.vanilla.snowballs.mixin;

import net.better.vanilla.snowballs.BetterVanillaSnowballs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityMixin extends Entity {
    public SnowballEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float modifySnowballDamage(float damage){
        GameRules gameRules = this.world.getGameRules();

        if (gameRules.getInt(BetterVanillaSnowballs.SNOWBALL_DAMAGE) <= 0) {
            return damage;
        }
        return gameRules.getInt(BetterVanillaSnowballs.SNOWBALL_DAMAGE);
    }
}
