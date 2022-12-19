package org.c191239.firechargeutil.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireChargeItem.class)
public class FireChargeMixin extends Item{
    public FireChargeMixin(Item.Settings settings){
        super(settings);
    }
    @Override
    public Rarity getRarity(ItemStack stack){
        return Rarity.EPIC;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            //world.createExplosion(user, user.getX(), user.getY(), user.getZ(), 5.0f, true, Explosion.DestructionType.BREAK);
            double a = user.getX();
            double b = user.getEyeY();
            double c = user.getZ();
            Vec3d vec3d = user.getRotationVector();
            double d = vec3d.getX();
            double e = vec3d.getY();
            double f = vec3d.getZ();
            FireballEntity fireball = new FireballEntity(world, a, b, c, d, e, f);
            fireball.setOwner(user);
            fireball.refreshPositionAndAngles(a, b, c, user.yaw, user.pitch);
            fireball.explosionPower = 3;
            world.spawnEntity(fireball);
            user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            if (!user.abilities.creativeMode) {
                stack.decrement(1);
            }
            user.swingHand(hand, true);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.consume(stack);
    }
}