package dev.hytalemodding.trash;

import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.container.SimpleItemContainer;

import javax.annotation.Nullable;

/**
 * A special trash container that:
 * 1. Stores exactly one item (uses parent's items map)
 * 2. Allows taking item out (recovery)
 * 3. Destroys previous item when new one is placed (whether via click, shift+click, or swap)
 *
 * Key implementation detail: We use SimpleItemContainer's internal 'items' map
 * for storage to ensure proper synchronization with all container operations.
 * When a new item is placed while the slot is occupied, the previous item is destroyed.
 */
public class TrashItemContainer extends SimpleItemContainer {

    private final Inventory playerInventory;

    public TrashItemContainer(Inventory playerInventory) {
        super((short) 1);
        this.playerInventory = playerInventory;
    }

    /**
     * Override to destroy previous item when setting a new one.
     * Returns null to prevent the previous item from being swapped back to player.
     */
    @Override
    protected ItemStack internal_setSlot(short slot, @Nullable ItemStack itemStack) {
        if (ItemStack.isEmpty(itemStack)) {
            // When removing (setting to null/empty), use normal remove
            return super.internal_removeSlot(slot);
        } else {
            // When setting a new item, destroy the previous one by returning null
            // First, actually store the new item in the map
            this.items.put(slot, itemStack);
            // Return null - this "destroys" the previous item (not returned to player on swap)
            return null;
        }
    }

    /**
     * Always allow adding to slot 0 - we handle destruction in internal_setSlot.
     */
    @Override
    protected boolean cantAddToSlot(short slot, ItemStack itemStack, ItemStack slotItemStack) {
        // Always allow adding - existing item will be destroyed (replaced)
        return false;
    }

    /**
     * Always allow removing from slot 0 - player can recover item.
     */
    @Override
    protected boolean cantRemoveFromSlot(short slot) {
        return false;
    }

    /**
     * Always allow dropping from slot 0.
     */
    @Override
    protected boolean cantDropFromSlot(short slot) {
        return false;
    }

    /**
     * Allow moving to this container, but first destroy any existing item.
     * This is called BEFORE the move operation checks the slot contents,
     * so clearing here prevents swap behavior.
     */
    @Override
    protected boolean cantMoveToSlot(ItemContainer fromContainer, short slotFrom) {
        // Destroy existing item by clearing the slot before the move
        this.items.remove((short) 0);
        return false;  // Allow the move
    }
}
