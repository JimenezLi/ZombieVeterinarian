package jimenezli.ZombieVeterinarian.client;

import jimenezli.ZombieVeterinarian.ZombieVeterinarianMod;
import jimenezli.ZombieVeterinarian.handler.EntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ZombieVeterinarianMod.ID)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        EntityRegistry.registerEntityRenderer();
    }
}
