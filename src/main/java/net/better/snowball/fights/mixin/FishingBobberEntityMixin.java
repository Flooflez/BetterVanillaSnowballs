package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity{
    public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileEntity;onEntityHit(Lnet/minecraft/util/hit/EntityHitResult;)V", shift = At.Shift.AFTER), cancellable = true)
    private void injected(EntityHitResult entityHitResult, CallbackInfo ci) {
        if(this.getWorld() instanceof ServerWorld) {
            GameRules gameRules = this.getWorld().getServer().getGameRules();
            Entity entity = entityHitResult.getEntity();

            Entity owner = this.getOwner();

            int moddedDamage= gameRules.getInt(BetterSnowballFights.BOBBER_DAMAGE);
            boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.BOBBERS_ONLY_DAMAGE_PLAYERS);

            float i = (!playersOnly || entity instanceof PlayerEntity) ? moddedDamage : 0;
            if(i != 0){
                entity.serverDamage(this.getDamageSources().thrown(this, owner), i);
            }

            double moddedKB= gameRules.getInt(BetterSnowballFights.BOBBER_KNOCKBACK)/10.0;

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
        }
    }


    @Unique
    private Entity pulledEntity;
    @Inject(method = "pullHookedEntity", at = @At(value = "HEAD"))
    private void capturePulledEntity(Entity entity, CallbackInfo ci) {
        this.pulledEntity = entity; // Capture the entity being pulled
    }

    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"), index = 0)
    private double pullMultiplier(double value) {
        if(this.getWorld() instanceof ServerWorld) {
            GameRules gameRules = this.getWorld().getServer().getGameRules();
            boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.BOBBERS_ONLY_DAMAGE_PLAYERS);
            if(!playersOnly || pulledEntity instanceof PlayerEntity){
                pulledEntity.velocityModified = true; //force update for players
                return gameRules.getInt(BetterSnowballFights.FISHING_PULL_MULTIPLIER)/10.0;
            }
        }
        return 0.1; //vanilla value

    }
}
