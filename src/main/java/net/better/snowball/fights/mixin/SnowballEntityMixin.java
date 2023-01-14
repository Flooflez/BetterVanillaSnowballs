package net.better.snowball.fights.mixin;

import net.better.snowball.fights.BetterSnowballFights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityMixin extends ProjectileEntity{
    public SnowballEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean injected(Entity entity, DamageSource source, float amount){
        GameRules gameRules = this.world.getGameRules();

        int moddedDamage= gameRules.getInt(BetterSnowballFights.SNOWBALL_DAMAGE);

        if(gameRules.getBoolean(BetterSnowballFights.SNOWBALLS_ONLY_DAMAGE_PLAYERS)){
            float i = entity instanceof PlayerEntity ? moddedDamage : amount;
            return entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), i);
        }
        else{
            float i = moddedDamage == 0 ? amount : moddedDamage;
            return entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), i);
        }


    }
}
