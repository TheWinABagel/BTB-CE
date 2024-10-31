package buildcraft.core.network;

import buildcraft.core.gui.BuildCraftContainer;
import buildcraft.core.gui.slots.SlotPhantom;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSetPhantomSlot extends BuildCraftPacket {
    private EntityPlayerMP playerMP;
    public ItemStack stack;
    public int slotId;
    public int windowId;

    public PacketSetPhantomSlot(EntityPlayerMP playerMP) {
        this.playerMP = playerMP;
    }

    public PacketSetPhantomSlot(ItemStack stack, int slotId, int windowId) {
        this.stack = stack;
        this.slotId = slotId;
        this.windowId = windowId;
    }

    @Override
    public int getID() {
        return PacketIds.GUI_PHANTOM_EMI;
    }

    @Override
    public void readData(DataInputStream data) throws IOException {
        this.stack = Packet.readItemStack(data);
        this.slotId = data.readInt();
        this.windowId = data.readInt();


        if (this.playerMP.openContainer.windowId == windowId && this.playerMP.openContainer.isPlayerNotUsingContainer(this.playerMP)) {
            if (this.playerMP.openContainer instanceof BuildCraftContainer bcContainer && this.playerMP.openContainer.getSlot(slotId) instanceof SlotPhantom phantom) {
                bcContainer.slotClickPhantom(phantom, 0, 0, playerMP, stack);
                this.playerMP.sendSlotContents(this.playerMP.openContainer, this.slotId, (ItemStack)this.playerMP.openContainer.inventoryItemStacks.get(this.slotId));
            }
        }
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException {
        Packet.writeItemStack(stack, data);
        data.writeInt(slotId);
        data.writeInt(windowId);
    }
}
