/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.statements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Icon;

public interface IStatementParameter {

    /**
     * Every parameter needs a unique tag, it should be in the format of "&lt;modi&gt;:&lt;name&gt;".
     *
     * @return the unique id
     */
    String getUniqueTag();

    @Environment(EnvType.CLIENT)
    Icon getIcon();

    ItemStack getItemStack();

    /**
     * Something that is initially unintuitive: you HAVE to keep your icons as static variables, due to the fact that
     * every IStatementParameter is instantiated upon creation, in opposition to IStatements which are singletons (due
     * to the fact that they, unlike Parameters, store no additional data)
     */
    @Environment(EnvType.CLIENT)
    void registerIcons(IconRegister iconRegister);

    /**
     * Return the parameter description in the UI
     */
    String getDescription();

    void onClick(IStatementContainer source, IStatement stmt, ItemStack stack, StatementMouseClick mouse);

    void readFromNBT(NBTTagCompound compound);

    void writeToNBT(NBTTagCompound compound);

    /**
     * This returns the parameter after a left rotation. Used in particular in blueprints orientation.
     */
    IStatementParameter rotateLeft();
}
