package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.server.world.ServerWorld;
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

@Mixin(EggEntity.class)
public abstract class EggEntityMixin extends ProjectileEntity{
    public EggEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;serverDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"), cancellable = true)
    private void injected(EntityHitResult entityHitResult, CallbackInfo ci) {
        if(this.getWorld() instanceof ServerWorld) {
            GameRules gameRules = this.getWorld().getServer().getGameRules();
            Entity entity = entityHitResult.getEntity();


            int moddedDamage= gameRules.getInt(BetterSnowballFights.EGG_DAMAGE);
            boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.EGGS_ONLY_DAMAGE_PLAYERS);

            float i = (!playersOnly || entity instanceof PlayerEntity) ? moddedDamage : 0;

            entity.serverDamage(this.getDamageSources().thrown(this, this.getOwner()), i);

            double moddedKB= gameRules.getInt(BetterSnowballFights.EGG_KNOCKBACK)/10.0;

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
}
