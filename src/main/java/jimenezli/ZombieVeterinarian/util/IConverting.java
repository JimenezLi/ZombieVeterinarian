package jimenezli.ZombieVeterinarian.util;

import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.UUID;

public interface IConverting {
    Random ConversionRandom = new Random();
    default int getTotalConversionTime() {
        return ConversionRandom.nextInt(2401) + 3600;
    }
    default int getConversionProgress() {
        return 1;
    }
    void finishConversion(ServerWorld world);
    void setConversionStarter(UUID playerUUID);
}
