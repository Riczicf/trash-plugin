package dev.hytalemodding.trash;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.hytalemodding.trash.commands.TrashCommand;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class TrashPlugin extends JavaPlugin {

    public TrashPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new TrashCommand());
    }

    @Override
    protected void start() {
        getLogger().at(Level.INFO).log("TrashPlugin has been enabled! Use /trash to open trash bin.");
    }

    @Override
    protected void shutdown() {
        getLogger().at(Level.INFO).log("TrashPlugin has been disabled!");
    }
}
