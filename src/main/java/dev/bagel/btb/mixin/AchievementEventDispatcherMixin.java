package dev.bagel.btb.mixin;

import btw.achievement.event.AchievementEventDispatcher;
import buildcraft.core.utils.FakePlayer;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AchievementEventDispatcher.class)
public class AchievementEventDispatcherMixin {
    @Inject(method = "handleEvent", at = @At("HEAD"), cancellable = true)
    private static <T> void buildcraft$noFakePlayerAchievement(AchievementEventDispatcher.AchievementEvent<T> type, EntityPlayer player, T data, CallbackInfo ci) {
        if (data instanceof FakePlayer) {
            ci.cancel();
        }
    }
}
