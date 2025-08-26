package buildcraft.transport;


import buildcraft.api.transport.IPipeTile;
import buildcraft.core.ItemBuildCraft;
import buildcraft.core.inventory.InvUtils;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;

public class ItemGateCopier extends ItemBuildCraft {
    public Icon[] icons;
    public ItemGateCopier(int id) {
        super(id);
        setMaxStackSize(1);
        setUnlocalizedName("gateCopier");
    }

    @Override
    public Icon getIconFromDamage(int meta) {
        if (itemIcon != null) { // NBT lookup workaround?
            return itemIcon;
        }
        if (icons != null && icons.length > 0) {
            return icons[meta % icons.length];
        } else {
            return null;
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister) {
        String[] names = new String[] { "buildcraft:gateCopier/empty", "buildcraft:gateCopier/full" };
        icons = new Icon[names.length];

        for (int i = 0; i < names.length; i++) {
            icons[i] = par1IconRegister.registerIcon(names[i]);
        }
    }

    @Override
    public Icon getIconIndex(ItemStack i) {
        NBTTagCompound cpt = InvUtils.getItemData(i);
        this.itemIcon = cpt.hasKey("logic") ? icons[1] : icons[0];
        return this.itemIcon;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        boolean isCopying = !player.isSneaking();
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        NBTTagCompound data = InvUtils.getItemData(stack);
//        PipePluggable pluggable = null;
        Gate gate = null;

        if (!(tile instanceof IPipeTile)) {
            isCopying = true;
        } else {
            Block block = Block.blocksList[world.getBlockId(x, y, z)];

            if (tile instanceof TileGenericPipe pipe && block instanceof BlockGenericPipe) {

                BlockGenericPipe.RaytraceResult rayTraceResult = ((BlockGenericPipe) block).doRayTrace(world, x, y, z, player);

                if (rayTraceResult != null && rayTraceResult.boundingBox != null
                        && rayTraceResult.hitPart == BlockGenericPipe.Part.Gate) {
                    gate = pipe.pipe.gate;
//                    pluggable = ((TileGenericPipe) tile).getPipePluggable(rayTraceResult.sideHit);
                }
            } else {
                System.err.println("Tile is not a pipe somehow");
            }
        }

//        if (pluggable instanceof GatePluggable) {
//            gate = ((GatePluggable) pluggable).realGate;
//        }

        if (isCopying) {
            if (gate == null) {
                stack.setTagCompound(new NBTTagCompound());
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.clear"));
                return true;
            }

            data = new NBTTagCompound();
            stack.setTagCompound(data);

            gate.writeToNBT(data);
            data.setByte("material", (byte) gate.material.ordinal());
            data.setByte("logic", (byte) gate.logic.ordinal());
            player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.gateCopied"));
        } else {
            if (!data.hasKey("logic")) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.noInformation"));
                return true;
            } else if (gate == null) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.noGate"));
                return true;
            }

            GateMaterial dataMaterial = GateMaterial.fromOrdinal(data.getByte("material"));
            GateMaterial gateMaterial = gate.material;

            if (gateMaterial.numSlots < dataMaterial.numSlots) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.warning.slots"));
            }
            /*if (gateMaterial.numActionParameters < dataMaterial.numActionParameters) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.warning.actionParameters"));
            }
            if (gateMaterial.numTriggerParameters < dataMaterial.numTriggerParameters) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.warning.triggerParameters"));
            }*/
            if (data.getByte("logic") != gate.logic.ordinal()) {
                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.warning.logic"));
            }

            gate.readFromNBT(data);
//            if (!gate.verifyGateStatements()) {
//                player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.warning.load"));
//            }

            if (tile instanceof TileGenericPipe) {
                ((TileGenericPipe) tile).sendUpdateToClient();
            }
            player.addChatMessage(StatCollector.translateToLocal("chat.gateCopier.gatePasted"));
            return true;
        }

        return true;
    }
}
