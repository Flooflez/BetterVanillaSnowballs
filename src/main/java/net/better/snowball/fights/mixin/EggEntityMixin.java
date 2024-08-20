package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
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

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), cancellable = true)
    private void injected(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();

        GameRules gameRules = this.getWorld().getGameRules();
        Entity owner = this.getOwner();

        int moddedDamage= gameRules.getInt(BetterSnowballFights.EGG_DAMAGE);
        boolean playersOnly = gameRules.getBoolean(BetterSnowballFights.EGGS_ONLY_DAMAGE_PLAYERS);

        float i = (!playersOnly || entity instanceof PlayerEntity) ? moddedDamage : 0;

        entity.damage(this.getDamageSources().thrown(this, owner), i);

        double moddedKB= gameRules.getInt(BetterSnowballFights.EGG_KNOCKBACK)/10.0;

        if(moddedKB != 0 && (!playersOnly || entity instanceof PlayerEntity)){
            double x = entity.getX() - owner.getX();
            double z = entity.getZ() - owner.getZ();
            double y = entity.getY() - owner.getY();
            double f = Math.max(x * x + z * z, 0.001);
            double y2 = Math.max(y/(f + y*y) * moddedKB, 0.01);
            entity.addVelocity(x / f * moddedKB, y2 , z / f * moddedKB);
        }

        ci.cancel();
    }
}
