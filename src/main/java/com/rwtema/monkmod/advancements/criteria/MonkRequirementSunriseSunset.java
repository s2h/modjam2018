package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MonkRequirementSunriseSunset extends MonkRequirement {
	private final int stareTime;

	public MonkRequirementSunriseSunset(int level, int stareTime) {
		super(level);
		this.stareTime = stareTime;
	}

	@SubscribeEvent
	public void walk(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}

		if (event.player instanceof EntityPlayerMP) {
			MonkData monkData = MonkManager.get(event.player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				EntityPlayerMP player = (EntityPlayerMP) event.player;
				double celestialAngle = player.world.getCelestialAngle(0) * Math.PI * 2;
				double sunHeight = Math.cos(celestialAngle);
				if (Math.abs(sunHeight) < 0.1) {
					Vec3d vec3d1 = player.getLook(1.0F);
					Vec3d sunDir = new Vec3d(-Math.sin(celestialAngle), sunHeight, 0);

					if (sunDir.dotProduct(vec3d1) > 0.99) {
						monkData.increaseProgress(1);
						if(monkData.getProgress() > stareTime){
							grantLevel(player);
						}
					}
				} else {
					monkData.setProgress(0);
				}
			}
		}
	}
}