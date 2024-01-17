package dev.louis.transfer;

import com.google.common.net.InternetDomainName;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.class_9151;
import net.minecraft.class_9152;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class TransferCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandManager.RegistrationEnvironment environment, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                CommandManager.literal("transfer")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.literal("to")
                                        .then(CommandManager.argument("domain", StringArgumentType.word()).
                                                executes(context -> {
                                                   var player = EntityArgumentType.getPlayer(context, "player");
                                                   var target = StringArgumentType.getString(context, "domain");
                                                   connect(player, target, 25565);
                                                   return 1;

                                                }).then(CommandManager.argument("port", IntegerArgumentType.integer(0, 65535)).executes(context -> {
                                                    var player = EntityArgumentType.getPlayer(context, "player");
                                                    var target = StringArgumentType.getString(context, "domain");
                                                    var ip = IntegerArgumentType.getInteger(context, "port");
                                                    connect(player, target, ip);
                                                    return 1;
                                                })))

                                )));
    }

    private static void connect(ServerPlayerEntity player, String target, int ip) {
        var transferPacket = new class_9151(target, ip);
        player.networkHandler.sendPacket(transferPacket);
    }
}
