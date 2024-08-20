package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
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
        Entity entity = entityHitResult.getEntity();

        GameRules gameRules = this.getWorld().getGameRules();
        Entity owner = this.getOwner();

        int moddedDamage= gameRules.getInt(BetterSnowballFights.BOBBER_DAMAGE);
        boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.BOBBERS_ONLY_DAMAGE_PLAYERS);

        float i = (!playersOnly || entity instanceof PlayerEntity) ? moddedDamage : 0;
        if(i != 0){
            entity.damage(this.getDamageSources().thrown(this, owner), i);
        }

        double moddedKB= gameRules.getInt(BetterSnowballFights.BOBBER_KNOCKBACK)/10.0;

        if(moddedKB != 0 && (!playersOnly || entity instanceof PlayerEntity)){
            double x = entity.getX() - owner.getX();
            double z = entity.getZ() - owner.getZ();
            double y = entity.getY() - owner.getY();
            double f = Math.max(x * x + z * z, 0.001);
            double y2 = Math.max(y/(f + y*y) * moddedKB, 0.01);
            entity.addVelocity(x / f * moddedKB, y2 , z / f * moddedKB);
        }
    }

    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(D)Lnet/minecraft/util/math/Vec3d;"), index = 0)
    private double pullMultiplier(double value) {
        GameRules gameRules = this.getWorld().getGameRules();
        return gameRules.getInt(BetterSnowballFights.FISHING_PULL_MULTIPLIER)/10.0;
    }
}
