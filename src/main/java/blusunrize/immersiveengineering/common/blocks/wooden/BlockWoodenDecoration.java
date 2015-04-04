package blusunrize.immersiveengineering.common.blocks.wooden;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressedPowered;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import blusunrize.immersiveengineering.client.render.BlockRenderWoodenDecoration;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;

public class BlockWoodenDecoration extends BlockIEBase
{
	public BlockWoodenDecoration()
	{
		super("woodenDecoration", Material.wood,1, ItemBlockWoodenDecoration.class, "treatedWood","fence","slab0","slab1","doubleSlab");
	}

	@Override
	public int getRenderType()
	{
		return BlockRenderWoodenDecoration.renderID;
	}

	@Override
	public int damageDropped(int meta)
	{
		if(meta==3||meta==4)
			return 2;
		return super.damageDropped(meta);
	}
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
		if(metadata==4)
			ret.add(new ItemStack(this,1,2));
		return ret;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		int meta = world.getBlockMetadata(x, y, z);

		if(meta==1)
			return side==UP || side==DOWN;
		if(meta==2)
			return side==DOWN;
		if(meta==3)
			return side==UP;

		return true;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y ,int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta==1||meta==2||meta==3)
			return true;
		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for(int i=0; i<subNames.length; i++)
			if(i!=3)
				list.add(new ItemStack(item, 1, i));
	}
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		for(int i=0; i<subNames.length; i++)
			icons[i][0] = iconRegister.registerIcon("immersiveengineering:treatedWood");
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
	{
		return meta;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if(world.getBlockMetadata(x, y, z)==1)
			this.setBlockBounds(canConnectFenceTo(world,x-1,y,z)?0:.375f,0,canConnectFenceTo(world,x,y,z-1)?0:.375f, canConnectFenceTo(world,x+1,y,z)?1:.625f,1,canConnectFenceTo(world,x,y,z+1)?1:.625f);
		else if(world.getBlockMetadata(x, y, z)==2)
			this.setBlockBounds(0,0,0, 1,.5f,1);
		else if(world.getBlockMetadata(x, y, z)==3)
			this.setBlockBounds(0,.5f,0, 1,1,1);
		else
			this.setBlockBounds(0,0,0,1,1,1);
	}
	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		return block != this && block != Blocks.fence_gate ? (block.getMaterial().isOpaque() && block.renderAsNormalBlock() ? block.getMaterial() != Material.gourd : false) : true;
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity ent)
	{
		super.addCollisionBoxesToList(world, x, y, z, aabb, list, ent);
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		if(world.getBlockMetadata(x, y, z)==1)
			this.setBlockBounds(canConnectFenceTo(world,x-1,y,z)?0:.375f,0,canConnectFenceTo(world,x,y,z-1)?0:.375f, canConnectFenceTo(world,x+1,y,z)?1:.625f,1.5f,canConnectFenceTo(world,x,y,z+1)?1:.625f);
		else
			this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		this.setBlockBoundsBasedOnState(world,x,y,z);
		return super.getCollisionBoundingBoxFromPool(world, x, y, z);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return null;
	}
	@Override
	public boolean allowHammerHarvest(int metadata)
	{
		return false;
	}
}