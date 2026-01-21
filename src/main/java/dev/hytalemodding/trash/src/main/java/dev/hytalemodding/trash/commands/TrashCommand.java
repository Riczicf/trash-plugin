package dev.hytalemodding.trash.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.windows.ContainerWindow;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.trash.TrashItemContainer;

import javax.annotation.Nonnull;

public class TrashCommand extends AbstractPlayerCommand {

    private static final Message MESSAGE_TRASH_OPENED = Message.raw("Trash bin opened. Items placed here will be destroyed when replaced.");

    public TrashCommand() {
        super("trash", "Opens a trash bin to destroy items");
    }

    @Override
    protected void execute(
        @Nonnull CommandContext context,
        @Nonnull Store<EntityStore> store,
        @Nonnull Ref<EntityStore> ref,
        @Nonnull PlayerRef playerRef,
        @Nonnull World world
    ) {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());

        if (playerComponent == null) {
            return;
        }

        Inventory playerInventory = playerComponent.getInventory();
        TrashItemContainer trashContainer = new TrashItemContainer(playerInventory);

        playerComponent.getPageManager().setPageWithWindows(
            ref,
            store,
            Page.Bench,
            true,
            new ContainerWindow(trashContainer)
        );

        context.sendMessage(MESSAGE_TRASH_OPENED);
    }
}
