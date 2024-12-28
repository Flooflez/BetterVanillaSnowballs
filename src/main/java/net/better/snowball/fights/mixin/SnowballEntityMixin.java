package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityMixin extends ProjectileEntity{

    public SnowballEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void injected(EntityHitResult entityHitResult, CallbackInfo ci, Entity entity, int amount) {
        GameRules gameRules = this.getWorld().getGameRules();

        int moddedDamage= gameRules.getInt(BetterSnowballFights.SNOWBALL_DAMAGE);
        boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.SNOWBALLS_ONLY_DAMAGE_PLAYERS);

        float i;
        if(playersOnly){
            i = entity instanceof PlayerEntity ? moddedDamage : amount;
        }
        else{
            i = moddedDamage == 0 ? amount : moddedDamage;
        }
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), i);

        double moddedKB= gameRules.getInt(BetterSnowballFights.SNOWBALL_KNOCKBACK)/10.0;

        if (moddedKB != 0 && (!playersOnly || entity instanceof PlayerEntity)) {
            Vec3d velocity = this.getVelocity();
            double knockbackFactor = moddedKB / Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z + 0.001);

            entity.addVelocity(
                    velocity.x * knockbackFactor,
                    velocity.y * knockbackFactor,
                    velocity.z * knockbackFactor
            );
            entity.velocityModified = true;
        }

        ci.cancel();
    }
}
