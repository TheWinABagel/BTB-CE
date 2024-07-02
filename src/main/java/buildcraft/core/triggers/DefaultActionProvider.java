package buildcraft.core.triggers;

import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionProvider;
import buildcraft.core.IMachine;
import buildcraft.core.utils.BCLog;
import java.util.LinkedList;
import java.util.logging.Level;
import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public class DefaultActionProvider implements IActionProvider {

	@Override
	public LinkedList<IAction> getNeighborActions(Block block, TileEntity tile) {
		LinkedList<IAction> res = new LinkedList<>();

		res.add(BuildCraftCore.actionRedstone);

		try {
			if (tile instanceof IMachine machine) {
                if (machine.allowAction(BuildCraftCore.actionOn))
					res.add(BuildCraftCore.actionOn);
				if (machine.allowAction(BuildCraftCore.actionOff))
					res.add(BuildCraftCore.actionOff);
				if (machine.allowAction(BuildCraftCore.actionLoop))
					res.add(BuildCraftCore.actionLoop);
			}
		} catch (Throwable error) {
			BCLog.logger.log(Level.SEVERE, "Outdated BC API detected, please update your mods!");
		}

		return res;
	}
}
