package vazkii.quark.world.tile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.arl.block.tile.TileMod;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.world.feature.MonsterBoxes;

import java.util.HashMap;
import java.util.Map;

public class TileMonsterBox extends TileMod implements ITickable {
//	public static void main(String[] args) {
//		Pool pool = new Pool("0.5 5-8: 0.1 minecraft:witch; 0.2 minecraft:cave_spider; 0.7 minecraft:zombie");
//		System.out.println(pool.chance);
//		System.out.println(pool.minCount);
//		System.out.println(pool.maxCount);
//	}

	private int breakProgress;

	@Override
	public void update() {
		if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
			return;

		BlockPos pos = getPos();

		if (world.isRemote) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			world.spawnParticle(breakProgress == 0 ? EnumParticleTypes.FLAME : EnumParticleTypes.SMOKE_LARGE, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0);
		}

		boolean doBreak = breakProgress > 0;
		if (!doBreak)
			doBreak = isPlayerInRange(pos);

		if (doBreak) {
			if (breakProgress == 0)
				world.playSound(null, pos, QuarkSounds.BLOCK_MONSTER_BOX_GROWL, SoundCategory.BLOCKS, 1F, 1F);

			breakProgress++;
			if (breakProgress > MonsterBoxes.activationTime) {
				world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
				world.setBlockToAir(pos);
				spawnMobs();
			}
		}
	}

	private boolean isPlayerInRange(BlockPos pos) {
		AxisAlignedBB area = new AxisAlignedBB(pos).grow(MonsterBoxes.activationRadius);
		for (EntityPlayer player : world.playerEntities)
			if (!player.isSpectator() && !player.isCreative() && player.getEntityBoundingBox().intersects(area))
				return true;
		return false;
	}

	private void spawnMobs() {
		if (world.isRemote)
			return;

		BlockPos pos = getPos();

		float r = world.rand.nextFloat();
		double min = 0.0;
		Pool pool = null;
		for (int i = 0; i < MonsterBoxes.pools.length; i++) {
			Pool currentPool = new Pool(MonsterBoxes.pools[i]);

			double max = min + currentPool.chance;
			if (r < max) {
				pool = currentPool;
				break;
			}
			min = max;
		}

		assert pool != null;
		int mobCount = pool.minCount + world.rand.nextInt(Math.max(pool.maxCount - pool.minCount + 1, 1));
		for (int i = 0; i < mobCount; i++) {
			Entity e = null;

			float r_ = world.rand.nextFloat();
//			if (r_ < 0.1)
//				e = new EntityWitch(world);
//			else if (r_ < 0.3)
//				e = new EntityCaveSpider(world);
//			else e = new EntityZombie(world);
			double min_ = 0.0;
			for (Map.Entry<EntityEntry, Double> entry : pool.entities.entrySet()) {
				EntityEntry entity = entry.getKey();
				Double chance = entry.getValue();

				double max = min_ + chance;
				if (r_ < max) {
					e = entity.newInstance(world);
					break;
				}
				min_ = max;
			}

			double motionMultiplier = 0.4;
			assert e != null;
			e.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			e.motionX = (world.rand.nextFloat() - 0.5) * motionMultiplier;
			e.motionY = (world.rand.nextFloat() - 0.5) * motionMultiplier;
			e.motionZ = (world.rand.nextFloat() - 0.5) * motionMultiplier;

			world.spawnEntity(e);
		}
	}

	private static class Pool {
		public double chance;
		public int minCount, maxCount;
		public Map<EntityEntry, Double> entities = new HashMap<>();

		public Pool(String source) {
			int start = 0;

//			Quark.LOG.info("Parsing chance...");
			for (int i = start; i < source.length(); i++) {
				if (source.charAt(i) == ' ') {
					chance = Double.parseDouble(source.substring(start, i));
					start = i + 1;
					break;
				}
			}
//			Quark.LOG.info("chance: " + chance);

//			Quark.LOG.info("Parsing minCount...");
			for (int i = start; i < source.length(); i++) {
				if (source.charAt(i) == '-') {
					minCount = Integer.parseInt(source.substring(start, i));
					start = i + 1;
					break;
				}
			}
//			Quark.LOG.info("minCount: " + minCount);

//			Quark.LOG.info("Parsing maxCount...");
			for (int i = start; i < source.length(); i++) {
				if (source.charAt(i) == ':') {
					maxCount = Integer.parseInt(source.substring(start, i));
					start = i + 2;
					break;
				}
			}
//			Quark.LOG.info("maxCount: " + maxCount);

			while (start < source.length()) {
				double mobChance = 0;
				String mobId = null;

//				Quark.LOG.info("Parsing mobChance...");
				for (int i = start; i < source.length(); i++) {
					if (source.charAt(i) == ' ') {
						mobChance = Double.parseDouble(source.substring(start, i));
						start = i + 1;
						break;
					}
				}
//				Quark.LOG.info("mobChance: " + mobChance);

//				Quark.LOG.info("Parsing mobId...");
				for (int i = start; i < source.length(); i++) {
					if (source.charAt(i) == ';') {
						mobId = source.substring(start, i);
						start = i + 2;
						break;
					}
				}
//				Quark.LOG.info("mobId: " + mobId);

				assert mobId != null;
				entities.put(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(mobId)), mobChance);
			}
		}
	}
}
