package jimenezli.ZombieVeterinarian.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class Advancements {
    public static final CuredZombiePigTrigger CURED_ZOMBIE_PIG = CriteriaTriggers.register(new CuredZombiePigTrigger());

    public static void init() {}
}
