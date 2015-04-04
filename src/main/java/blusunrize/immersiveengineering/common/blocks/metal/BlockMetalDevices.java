package blusunrize.immersiveengineering.common.blocks.metal;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import blusunrize.immersiveengineering.api.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.ImmersiveNetHandler;
import blusunrize.immersiveengineering.client.render.BlockRenderMetalDevices;
import blusunrize.immersiveengineering.common.ItemNBTHelper;
import blusunrize.immersiveengineering.common.Utils;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWoodenPost;

public class BlockMetalDevices extends BlockIEBase
{
	public IIcon[][] icon_capacitorTop = new IIcon[3][3];
	public IIcon[][] icon_capacitorBot = new IIcon[3][3];
	public IIcon[][] icon_capacitorSide = new IIcon[3][3];

	public static int META_connectorLV=0;
	public static int META_capacitorLV=1;
	public static int META_connectorMV=2;
	public static int META_capacitorMV=3;
	public static int META_transformer=4;
	public static int META_relayHV=5;
	public static int META_connectorHV=6;
	public static int META_capacitorHV=7;
	public static int META_transformerHV=8;
	public static int META_dynamo=9;
	public static int META_thermoelectricGen=10;
	public BlockMetalDevices()
	{
		super("metalDevice", Material.iron, 4, ItemBlockMetalDevices.class,
				"connectorLV","capacitorLV","connectorMV","capacitorMV","transformer","relayHV","connectorHV","capacitorHV","transformerHV", "dynamo","thermoelectricGen");
		setHardness(3.0F);
		setResistance(15.0F);
	}


	@Override
	public boolean allowHammerHarvest(int meta)
	{
		return true;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
	{
		if(!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityCapacitorLV)
		{
			ItemStack stack = new ItemStack(this,1,meta);
			if(((TileEntityCapacitorLV)world.getTileEntity(x,y,z)).energyStorage.getEnergyStored()>0)
				ItemNBTHelper.setInt(stack, "energyStorage", ((TileEntityCapacitorLV)world.getTileEntity(x,y,z)).energyStorage.getEnergyStored());
			int[] sides = ((TileEntityCapacitorLV)world.getTileEntity(x,y,z)).sideConfig;
			if(sides[0]!=-1 || sides[1]!=0||sides[2]!=0||sides[3]!=0||sides[4]!=0||sides[5]!=0)
				ItemNBTHelper.setIntArray(stack, "sideConfig", sides);
			world.spawnEntityInWorld(new EntityItem(world,x+.5,y+.5,z+.5,stack));
		}
	}
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		if(metadata==1||metadata==7)
			return new ArrayList();
		ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
		return ret;
	}


	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		//1 capacitorLV
		icons[1][0] = iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_bottom_none");
		icons[1][1] = iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_top_none");
		icons[1][2] = iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_side_none");
		icons[1][3] = iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_side_none");
		for(int i=0;i<3;i++)
		{
			String s = i==0?"none":i==1?"in":"out";
			icon_capacitorBot[0][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_bottom_"+s);
			icon_capacitorTop[0][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_top_"+s);
			icon_capacitorSide[0][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorLV_side_"+s);
		}
		//3 capacitorMV
		icons[3][0] = iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_bottom_none");
		icons[3][1] = iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_top_none");
		icons[3][2] = iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_side_none");
		icons[3][3] = iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_side_none");
		for(int i=0;i<3;i++)
		{
			String s = i==0?"none":i==1?"in":"out";
			icon_capacitorBot[1][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_bottom_"+s);
			icon_capacitorTop[1][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_top_"+s);
			icon_capacitorSide[1][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorMV_side_"+s);
		}
		//7 capacitorHV
		icons[7][0] = iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_bottom_none");
		icons[7][1] = iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_top_none");
		icons[7][2] = iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_side_none");
		icons[7][3] = iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_side_none");
		for(int i=0;i<3;i++)
		{
			String s = i==0?"none":i==1?"in":"out";
			icon_capacitorBot[2][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_bottom_"+s);
			icon_capacitorTop[2][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_top_"+s);
			icon_capacitorSide[2][i]= iconRegister.registerIcon("immersiveengineering:metal_capacitorHV_side_"+s);
		}
		//9 dynamo
		icons[9][0] = iconRegister.registerIcon("immersiveengineering:metal_dynamo_bottom");
		icons[9][1] = iconRegister.registerIcon("immersiveengineering:metal_dynamo_top");
		icons[9][2] = iconRegister.registerIcon("immersiveengineering:metal_dynamo_front");
		icons[9][3] = iconRegister.registerIcon("immersiveengineering:metal_dynamo_side");
		//10 thermoelectricGen
		icons[10][0] = iconRegister.registerIcon("immersiveengineering:metal_thermogen_bottom");
		icons[10][1] = iconRegister.registerIcon("immersiveengineering:metal_thermogen_top");
		icons[10][2] = iconRegister.registerIcon("immersiveengineering:metal_thermogen_side");
		icons[10][3] = iconRegister.registerIcon("immersiveengineering:metal_thermogen_side");

//		//Lantern
//		icons[2][0] = iconRegister.registerIcon("immersiveengineering:metal_lantern_bottom");
//		icons[2][1] = iconRegister.registerIcon("immersiveengineering:metal_lantern_top");
//		icons[2][2] = iconRegister.registerIcon("immersiveengineering:metal_lantern_side");
//		icons[2][3] = iconRegister.registerIcon("immersiveengineering:metal_lantern_side");


		//0 connectorLV
		//2 connectorMV
		//4 transformer
		//5 relayHV
		//6 connectorHV
		//8 transformerHV
		for(int i=0;i<4;i++)
		{
			icons[0][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
			icons[2][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
			icons[4][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
			icons[5][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
			icons[6][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
			icons[8][i] = iconRegister.registerIcon("immersiveengineering:storage_steel");
		}
	}
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityCapacitorLV)
		{
			TileEntityCapacitorLV cap = (TileEntityCapacitorLV)world.getTileEntity(x, y, z);
			int t = cap instanceof TileEntityCapacitorHV?2: cap instanceof TileEntityCapacitorMV?1: 0;
			if(side==0)
				return icon_capacitorBot[t][cap.sideConfig[side]+1];
			else if(side==1)
				return icon_capacitorTop[t][cap.sideConfig[side]+1];
			else
				return icon_capacitorSide[t][cap.sideConfig[side]+1];
		}
		if(world.getTileEntity(x, y, z) instanceof TileEntityDynamo && ((TileEntityDynamo)world.getTileEntity(x,y,z)).facing>3 && side>1)
		{
			return icons[META_dynamo][side<4?3:2];
		}
		return super.getIcon(world, x, y, z, side);
	}
	@Override
	public int getRenderType()
	{
		return BlockRenderMetalDevices.renderID;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		if(world.getBlockMetadata(x, y, z)==2)
		{
			return 15;
		}
		return 0;
	}
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityCapacitorLV && Utils.isHammer(player.getCurrentEquippedItem()))
		{
			((TileEntityCapacitorLV)world.getTileEntity(x, y, z)).toggleSide(side);
			return true;
		}
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityConnectorLV)
		{
			float length = world.getTileEntity(x, y, z) instanceof TileEntityRelayHV?.875f: world.getTileEntity(x, y, z) instanceof TileEntityConnectorHV?.75f: .5f;

			switch(((TileEntityConnectorLV)world.getTileEntity(x, y, z)).facing )
			{
			case 0://UP
				this.setBlockBounds(.3125f,0,.3125f,  .6875f,length,.6875f);
				break;
			case 1://DOWN
				this.setBlockBounds(.3125f,1-length,.3125f,  .6875f,1,.6875f);
				break;
			case 2://SOUTH
				this.setBlockBounds(.3125f,.3125f,0,  .6875f,.6875f,length);
				break;
			case 3://NORTH
				this.setBlockBounds(.3125f,.3125f,1-length,  .6875f,.6875f,1);
				break;
			case 4://EAST
				this.setBlockBounds(0,.3125f,.3125f,  length,.6875f,.6875f);
				break;
			case 5://WEST
				this.setBlockBounds(1-length,.3125f,.3125f,  1,.6875f,.6875f);
				break;
			}
		}
		else if(world.getBlockMetadata(x, y, z)==2)
			this.setBlockBounds(.25f,0,.25f, .75f,.8125f,.75f);
		else if(world.getTileEntity(x, y, z) instanceof TileEntityTransformer)
		{
			TileEntityTransformer transf = (TileEntityTransformer)world.getTileEntity(x, y, z);
			if(transf.postAttached>0)
			{
				switch(transf.postAttached)
				{
				case 2://SOUTH
					this.setBlockBounds(.25f,0,.6875f,  .75f,1,1.3125f);
					break;
				case 3://NORTH
					this.setBlockBounds(.25f,0,-.3125f,  .75f,1,.3125f);
					break;
				case 4://EAST
					this.setBlockBounds(.6875f,0,.25f,  1.3125f,1,.75f);
					break;
				case 5://WEST
					this.setBlockBounds(-.3125f,0,.25f,  .3125f,1,.75f);
				}
			}
			else
				this.setBlockBounds(0,0,0,1,1,1);
		}
		else
			this.setBlockBounds(0,0,0,1,1,1);
	}
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
	{
		//		int playerViewQuarter = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		//		int f = playerViewQuarter==0 ? 2:playerViewQuarter==1 ? 5:playerViewQuarter==2 ? 3: 4;
		//		if(world.getTileEntity(x, y, z) instanceof TileEntityTransformer)
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		switch(meta)
		{
		case 0://0 connectorLV
			return new TileEntityConnectorLV();
		case 1://1 capacitorLV
			return new TileEntityCapacitorLV();
		case 2://2 connectorMV
			return new TileEntityConnectorMV();
		case 3://3 capacitorMV
			return new TileEntityCapacitorMV();
		case 4://4 transformer
			return new TileEntityTransformer();
		case 5://5 relayHV
			return new TileEntityRelayHV();
		case 6://6 connectorHV
			return new TileEntityConnectorHV();
		case 7://7 capacitorHV
			return new TileEntityCapacitorHV();
		case 8://8 transformerHV
			return new TileEntityTransformerHV();
		case 9://9 dynamo
			return new TileEntityDynamo();
		case 10://10 thermoelectricGen
			return new TileEntityThermoelectricGen();
		}
		return null;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		if(world.getTileEntity(x, y, z) instanceof IImmersiveConnectable)
			ImmersiveNetHandler.clearAllConnectionsFor(new ChunkCoordinates(x, y, z),world);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block nbid)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityConnectorLV)
		{
			TileEntityConnectorLV relay = (TileEntityConnectorLV)world.getTileEntity(x, y, z);
			ForgeDirection fd = ForgeDirection.getOrientation(relay.facing);
			if(world.isAirBlock(x+fd.offsetX, y+fd.offsetY, z+fd.offsetZ))
			{
				dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
				world.setBlockToAir(x, y, z);
			}
		}
		if(world.getTileEntity(x, y, z) instanceof TileEntityTransformer)
		{
			TileEntityTransformer transf = (TileEntityTransformer)world.getTileEntity(x, y, z);
			if(transf.postAttached>0 && !(world.getTileEntity(x+(transf.postAttached==4?1: transf.postAttached==5?-1: 0), y, z+(transf.postAttached==2?1: transf.postAttached==3?-1: 0)) instanceof TileEntityWoodenPost ))
			{
				this.dropBlockAsItem(world, x, y, z, new ItemStack(this,1,world.getBlockMetadata(x, y, z)));
				world.setBlockToAir(x, y, z);
			}
			else if(transf.postAttached<=0 && ((transf.dummy && world.isAirBlock(x,y+1,z))|| (!transf.dummy && world.isAirBlock(x,y-1,z))))
				world.setBlockToAir(x, y, z);
		}
	}
}