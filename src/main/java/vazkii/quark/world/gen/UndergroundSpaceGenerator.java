package vazkii.quark.world.gen;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.common.util.Constants;
import vazkii.quark.base.world.config.ClusterSizeConfig;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.base.world.generator.multichunk.ClusterBasedGenerator;
import vazkii.quark.world.module.MegaCavesModule;

public class UndergroundSpaceGenerator extends ClusterBasedGenerator {

	public UndergroundSpaceGenerator(DimensionConfig dimConfig, ClusterSizeConfig sizeConfig, long seedXor) {
		super(dimConfig, sizeConfig, seedXor);
	}

	@Override
	public IGenerationContext createContext(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world) {
		return pos -> {
			BlockState state = world.getBlockState(pos);
			if(state.getBlockHardness(world, pos) > -1) {
				if(pos.getY() < 6)
					world.setBlockState(pos, Blocks.LAVA.getDefaultState(), 1);
				else world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
		};
	}

	@Override
	public BlockPos[] getSourcesInChunk(IWorld world, Random random, ChunkGenerator<? extends GenerationSettings> generator, BlockPos chunkLeft) {
		if(!(generator instanceof FlatChunkGenerator)) {
			int rarity = shapeProvider.getRarity();
			if(rarity > 0 && random.nextInt(rarity) == 0) {
				BlockPos pos = chunkLeft.add(random.nextInt(16), shapeProvider.getRandomYLevel(random), random.nextInt(16));
				if(shapeProvider.getBiomeTypes().canSpawn(getBiome(world, pos)))
					return new BlockPos[] { pos };
			}
		}
		
		return new BlockPos[0];
	}

}