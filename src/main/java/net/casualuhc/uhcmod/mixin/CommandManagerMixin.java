package net.casualuhc.uhcmod.mixin;

import com.mojang.brigadier.CommandDispatcher;
import net.casualuhc.uhcmod.command.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.minecraft.server.command.CommandManager.RegistrationEnvironment;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Shadow
    @Final
    private CommandDispatcher<ServerCommandSource> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void register(RegistrationEnvironment env, CallbackInfo ci){
        CameramanCommand.register(this.dispatcher);
        PosCommand.register(this.dispatcher);
        SpectCommand.register(this.dispatcher);
        CoordsCommand.register(this.dispatcher);
        UHCCommand.register(this.dispatcher);
        ReadyCommand.register(this.dispatcher);
        TeamGlowingCommand.register(this.dispatcher);
    }
}
