package jimenezli.ZombieVeterinarian.handler;

import jimenezli.ZombieVeterinarian.entity.ConvertingZoglinEntity;
import jimenezli.ZombieVeterinarian.entity.ConvertingZombieEntity;
import jimenezli.ZombieVeterinarian.entity.ConvertingZombifiedPiglinEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventHandler {
    static Random random = new Random();

    @SubscribeEvent
    public static void onZombiePigGivenCure(PlayerInteractEvent.EntityInteract evt) {
        boolean actionSuccessful = false;
        ItemStack itemStack = evt.getItemStack();

        if (itemStack.getItem() == Items.GOLDEN_APPLE) {
            Entity target = evt.getTarget();
            World world = evt.getWorld();
            if (!world.isClientSide()) {
                if (target.getClass() == ZombifiedPiglinEntity.class) {
                    if (((ZombifiedPiglinEntity) target).hasEffect(Effects.WEAKNESS)) {
                        ConvertingZombifiedPiglinEntity convertingZombifiedPiglin = ((ZombifiedPiglinEntity) target).convertTo(EntityRegistry.convertingZombifiedPiglin, true);
                        if (convertingZombifiedPiglin != null) {
                            convertingZombifiedPiglin.setConversionStarter(evt.getPlayer().getUUID());
                            ConvertMob((MobEntity) target, convertingZombifiedPiglin, (ServerWorld) world);
                            actionSuccessful = true;
                        }
                    }
                } else if (target.getClass() == ZoglinEntity.class) {
                    if (((ZoglinEntity) target).hasEffect(Effects.WEAKNESS)) {
                        ConvertingZoglinEntity convertingZoglin = ((ZoglinEntity) target).convertTo(EntityRegistry.convertingZoglin, true);
                        if (convertingZoglin != null) {
                            convertingZoglin.setConversionStarter(evt.getPlayer().getUUID());
                            ConvertMob((MobEntity) target, convertingZoglin, (ServerWorld) world);
                            actionSuccessful = true;
                        }
                    }
                }
            }
        } else if (itemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
            Entity target = evt.getTarget();
            World world = evt.getWorld();
            if (!world.isClientSide()) {
                if (target.getClass() == ZombieEntity.class) {
                    if (((ZombieEntity) target).hasEffect(Effects.WEAKNESS)) {
                        ConvertingZombieEntity convertingZombie = ((ZombieEntity) target).convertTo(EntityRegistry.convertingZombie, true);
                        if (convertingZombie != null) {
                            convertingZombie.setConversionStarter(evt.getPlayer().getUUID());
                            ConvertMob((MobEntity) target, convertingZombie, (ServerWorld) world);
                            actionSuccessful = true;
                        }
                    }
                }
            }
        }
        if (actionSuccessful && !evt.getPlayer().isCreative()) {
            itemStack.shrink(1);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent evt) {
        Entity entity = evt.getEntity();
        if (entity.getClass() == ConvertingZombifiedPiglinEntity.class
            || entity.getClass() == ConvertingZoglinEntity.class
            || entity.getClass() == ConvertingZombieEntity.class) {
            entity.level.playSound(null, entity.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, entity.getSoundSource(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
        }
    }

    private static void ConvertMob(MobEntity before, @Nonnull MobEntity after, ServerWorld world) {
        int conversionTime = 6000;
        after.finalizeSpawn(world, world.getCurrentDifficultyAt(after.blockPosition()), SpawnReason.CONVERSION, null, null);
        after.removeEffect(Effects.WEAKNESS);
        after.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, conversionTime, Math.min(world.getDifficulty().getId() - 1, 0)));
        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(before, after);
    }
}
