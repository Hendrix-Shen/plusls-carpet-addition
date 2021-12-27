package com.plusls.carpet.mixin.rule.forceRestock;

import com.plusls.carpet.PcaSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpectralArrowEntity.class)
public abstract class MixinSpectralArrowEntity extends ProjectileEntity {

    protected MixinSpectralArrowEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onHit", at = @At(value = "RETURN"))
    private void forceRestock(LivingEntity target, CallbackInfo ci) {
        if (PcaSettings.forceRestock && !target.world.isClient && target instanceof AbstractTraderEntity) {
            AbstractTraderEntity merchantEntity = (AbstractTraderEntity) target;
            for (TradeOffer tradeOffer : merchantEntity.getOffers()) {
                tradeOffer.resetUses();
            }
            // make villager happy ~
            world.sendEntityStatus(merchantEntity, (byte) 14);
        }
    }
}
