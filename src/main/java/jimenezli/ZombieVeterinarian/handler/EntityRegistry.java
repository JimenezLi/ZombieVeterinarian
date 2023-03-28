package jimenezli.ZombieVeterinarian.handler;

import jimenezli.ZombieVeterinarian.ZombieVeterinarianMod;
import jimenezli.ZombieVeterinarian.entity.ConvertingZoglinEntity;
import jimenezli.ZombieVeterinarian.entity.ConvertingZombieEntity;
import jimenezli.ZombieVeterinarian.entity.ConvertingZombifiedPiglinEntity;
import jimenezli.ZombieVeterinarian.util.EntityNames;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.client.renderer.entity.ZoglinRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ZombieVeterinarianMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    private static final List<EntityType<?>> ALL = new ArrayList<>();
    public static final EntityType<ConvertingZombifiedPiglinEntity> convertingZombifiedPiglin = build(EntityNames.CONVERTING_ZOMBIFIED_PIGLIN, makeCastedBuilder(ConvertingZombifiedPiglinEntity.class, ConvertingZombifiedPiglinEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(8));
    public static final EntityType<ConvertingZoglinEntity> convertingZoglin = build(EntityNames.CONVERTING_ZOGLIN, makeCastedBuilder(ConvertingZoglinEntity.class, ConvertingZoglinEntity::new, EntityClassification.MONSTER).fireImmune().sized(1.3964844F, 1.4F).clientTrackingRange(8));
    public static final EntityType<ConvertingZombieEntity> convertingZombie = build(EntityNames.CONVERTING_ZOMBIE, makeCastedBuilder(ConvertingZombieEntity.class, ConvertingZombieEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));

    @SuppressWarnings("unchecked")
    private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
        EntityType<E> ret = (EntityType<E>) builder.build(id.toString()).setRegistryName(id);
        ALL.add(ret);
        return ret;
    }

    private static <E extends Entity> EntityType.Builder<E> makeCastedBuilder(@SuppressWarnings("unused") Class<E> cast, EntityType.IFactory<E> factory, EntityClassification classification) {
        return makeBuilder(factory, classification);
    }

    private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification) {
        return EntityType.Builder.of(factory, classification).
                sized(0.6F, 1.8F).
                setUpdateInterval(3).
                setShouldReceiveVelocityUpdates(true);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        evt.getRegistry().registerAll(ALL.toArray(new EntityType<?>[0]));
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(convertingZombifiedPiglin, ConvertingZombifiedPiglinEntity.createAttributes().build());
        event.put(convertingZoglin, ConvertingZoglinEntity.createAttributes().build());
        event.put(convertingZombie, ConvertingZombieEntity.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerEntityRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(convertingZombifiedPiglin, m -> new PiglinRenderer(m, true));
        RenderingRegistry.registerEntityRenderingHandler(convertingZoglin, ZoglinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(convertingZombie, ZombieRenderer::new);
    }
}
