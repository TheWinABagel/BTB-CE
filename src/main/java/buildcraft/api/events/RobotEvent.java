/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution.
 */
package buildcraft.api.events;

import buildcraft.api.robots.EntityRobotBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public abstract class RobotEvent /*extends Event*/ {

    public final EntityRobotBase robot;

    public RobotEvent(EntityRobotBase robot) {
        this.robot = robot;
    }

//    @Cancelable
    public static class Place extends RobotEvent {

        public final EntityPlayer player;

        public Place(EntityRobotBase robot, EntityPlayer player) {
            super(robot);
            this.player = player;
        }
    }

//    @Cancelable
    public static class Interact extends RobotEvent {

        public final EntityPlayer player;
        public final ItemStack item;

        public Interact(EntityRobotBase robot, EntityPlayer player, ItemStack item) {
            super(robot);
            this.player = player;
            this.item = item;
        }
    }

//    @Cancelable
    public static class Dismantle extends RobotEvent {

        public final EntityPlayer player;

        public Dismantle(EntityRobotBase robot, EntityPlayer player) {
            super(robot);
            this.player = player;
        }
    }
}
