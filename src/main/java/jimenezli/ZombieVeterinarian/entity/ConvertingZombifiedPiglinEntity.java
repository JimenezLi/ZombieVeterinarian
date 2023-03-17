package jimenezli.ZombieVeterinarian.entity;

import jimenezli.ZombieVeterinarian.advancements.Advancements;
import jimenezli.ZombieVeterinarian.util.IConverting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class ConvertingZombifiedPiglinEntity extends ZombifiedPiglinEntity implements IConverting {
    private int conversionTime;
    private UUID conversionStarter;

    @Override
    public EntityType<?> getType() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("ConversionTime", this.conversionTime);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if (compoundNBT.contains("ConversionTime")) {
            this.conversionTime = compoundNBT.getInt("ConversionTime");
            this.conversionStarter = (compoundNBT.hasUUID("ConversionPlayer") ? compoundNBT.getUUID("ConversionPlayer") : null);
        }
    }

    public void setConversionStarter(UUID playerUUID) {
        this.conversionStarter = playerUUID;
    }

    public ConvertingZombifiedPiglinEntity(EntityType<? extends ZombifiedPiglinEntity> entityType, World world) {
        super(entityType, world);
        conversionTime = getTotalConversionTime();
        conversionStarter = null;
    }

    public void tick() {
        if (!this.level.isClientSide && this.isAlive()) {
            int i = this.getConversionProgress();
            this.conversionTime -= i;
            if (this.conversionTime <= 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, EntityType.VILLAGER, (timer) -> this.conversionTime = timer)) {
                this.finishConversion((ServerWorld)this.level);
            }
        }

        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte b) {
        if (b == 16) {
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, this.getSoundSource(), 1.0F + this.random.nextFloat(), this.random.nextFloat() * 0.7F + 0.3F, false);
            }

        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public void finishConversion(ServerWorld world) {
        PiglinEntity piglinEntity = this.convertTo(EntityType.PIGLIN, true);
        if (piglinEntity != null) {
            piglinEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(piglinEntity.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
            piglinEntity.addEffect(new EffectInstance(Effects.CONFUSION, 200, 0));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, piglinEntity);
        }

        if (this.conversionStarter != null) {
            PlayerEntity playerEntity = world.getPlayerByUUID(this.conversionStarter);
            if (playerEntity instanceof ServerPlayerEntity) {
                Advancements.CURED_ZOMBIE_PIG.trigger((ServerPlayerEntity) playerEntity);
            }
        }
    }
}
