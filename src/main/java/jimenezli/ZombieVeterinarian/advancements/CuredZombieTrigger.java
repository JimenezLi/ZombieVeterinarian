package jimenezli.ZombieVeterinarian.advancements;

import com.google.gson.JsonObject;
import jimenezli.ZombieVeterinarian.ZombieVeterinarianMod;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class CuredZombieTrigger extends AbstractCriterionTrigger<CuredZombieTrigger.Instance> {
    private static final ResourceLocation ID = ZombieVeterinarianMod.prefix("cured_zombie");
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CuredZombieTrigger.Instance createInstance(JsonObject p_230241_1_, EntityPredicate.AndPredicate p_230241_2_, ConditionArrayParser p_230241_3_) {
        EntityPredicate.AndPredicate entitypredicate$andpredicate = EntityPredicate.AndPredicate.fromJson(p_230241_1_, "zombie", p_230241_3_);
        EntityPredicate.AndPredicate entitypredicate$andpredicate1 = EntityPredicate.AndPredicate.fromJson(p_230241_1_, "villager", p_230241_3_);
        return new CuredZombieTrigger.Instance(p_230241_2_, entitypredicate$andpredicate, entitypredicate$andpredicate1);
    }
    public void trigger(ServerPlayerEntity p_192183_1_) {
        this.trigger(p_192183_1_, m -> true);
    }

    public static class Instance extends CriterionInstance {
        private final EntityPredicate.AndPredicate zombiepig;
        private final EntityPredicate.AndPredicate pig;

        public Instance(EntityPredicate.AndPredicate p_i231535_1_, EntityPredicate.AndPredicate p_i231535_2_, EntityPredicate.AndPredicate p_i231535_3_) {
            super(CuredZombieTrigger.ID, p_i231535_1_);
            this.zombiepig = p_i231535_2_;
            this.pig = p_i231535_3_;
        }

        public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
            JsonObject jsonobject = super.serializeToJson(p_230240_1_);
            jsonobject.add("zombie", this.zombiepig.toJson(p_230240_1_));
            jsonobject.add("villager", this.pig.toJson(p_230240_1_));
            return jsonobject;
        }
    }
}
