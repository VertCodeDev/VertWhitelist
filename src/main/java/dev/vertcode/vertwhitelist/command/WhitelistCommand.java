package dev.vertcode.vertwhitelist.command;

import dev.vertcode.vertlibrary.base.VertCommandBase;
import dev.vertcode.vertlibrary.standalone.config.GenericLang;
import dev.vertcode.vertwhitelist.command.admin.*;
import dev.vertcode.vertwhitelist.command.structure.VertWhitelistCommand;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhitelistCommand extends VertCommandBase {

    @Getter
    private final List<VertWhitelistCommand> whitelistCommands = new ArrayList<>();
    private final VertWhitelistCommand helpCommand;

    public WhitelistCommand() {
        super("whitelist", "vertwhitelist.command.whitelist", false);

        this.whitelistCommands.add(new AddPlayerCommand());
        this.whitelistCommands.add(new RemovePlayerCommand());
        this.whitelistCommands.add(new EnableCommand());
        this.whitelistCommands.add(new DisableCommand());
        this.whitelistCommands.add(new ClearCommand());
        this.whitelistCommands.add(new ListCommand());

        this.whitelistCommands.add(this.helpCommand = new HelpCommand());
    }

    @Override
    protected void execute(CommandSender sender, Command cmd, String[] args) {
        if (args.length < 1) {
            helpCommand.execute(sender, args);
            return;
        }

        Optional<VertWhitelistCommand> optional = getCommandByString(args[0].toLowerCase());
        if (!optional.isPresent()) {
            helpCommand.execute(sender, args);
            return;
        }

        VertWhitelistCommand command = optional.get();
        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            sender.sendMessage(GenericLang.NO_PERMISSIONS.getString());
            return;
        }

        command.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        String lastArg = args[args.length - 1].toLowerCase();

        if (args.length < 2)
            return this.whitelistCommands.stream().filter(whitelistCommand -> whitelistCommand.getPermission() != null &&
                    sender.hasPermission(whitelistCommand.getPermission())).map(VertWhitelistCommand::getCommand)
                    .filter(m -> m.toLowerCase().startsWith(lastArg)).collect(Collectors.toList());

        Optional<VertWhitelistCommand> optional = getCommandByString(args[0].toLowerCase());
        if (!optional.isPresent()) return new ArrayList<>();

        VertWhitelistCommand command = optional.get();
        List<String> tabComplete = command.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        if (tabComplete == null) return new ArrayList<>();

        return tabComplete.stream().filter(m -> m.toLowerCase().startsWith(lastArg)).collect(Collectors.toList());
    }

    private Optional<VertWhitelistCommand> getCommandByString(String command) {
        return this.whitelistCommands.stream().filter(whitelistCommand ->
                command.equalsIgnoreCase(whitelistCommand.getCommand()) ||
                        Arrays.stream(whitelistCommand.getAliases()).anyMatch(cmd -> cmd.equalsIgnoreCase(command))).findFirst();
    }
}
