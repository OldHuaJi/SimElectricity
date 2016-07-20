package simElectricity.Common.EnergyNet;

import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import simElectricity.API.EnergyTile.ISEGridNode;
import simElectricity.API.EnergyTile.ISESimulatable;

public class GridNode implements ISEGridNode{
	public EnergyNetDataProvider gridDataProvider;
	
	//0 - terminal 1 - node 2 - transformer primary 3 - transformer secondary
	public byte type;
	public int x;
	public int y;
	public int z;
	
	public HashMap<GridNode, Double> resistances = new HashMap<GridNode, Double>();
	
	public TileEntity associatedTE;
	
	//Only used during loading
	protected int neighborX[];
	protected int neighborY[];
	protected int neighborZ[];
	private double[] resistancesBuf;
		
	public GridNode(EnergyNetDataProvider dataProvider){
		gridDataProvider = dataProvider;
	}
	
	public int buildNeighborConnection(HashMap<String, GridNode> gridNodeMap){
		int numOfNeighbors = neighborX.length;
		
		for (int i = 0; i<numOfNeighbors ; i++){
			String neighborID = getIDString(neighborX[i], neighborY[i], neighborZ[i]);
			GridNode neighbor = gridNodeMap.get(neighborID);
			
			gridDataProvider.addEdge(this, neighbor, resistancesBuf[i]);
		}
		
		return numOfNeighbors;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		type = nbt.getByte("type");
		x = nbt.getInteger("x");
		y = nbt.getInteger("y");
		z = nbt.getInteger("z");
		neighborX = nbt.getIntArray("neigborX");
		neighborY = nbt.getIntArray("neigborY");
		neighborZ = nbt.getIntArray("neigborZ");
		
		
		int numOfNeighbors = neighborX.length;
		resistancesBuf = new double[numOfNeighbors];
		for (int i=0; i<numOfNeighbors; i++){
			resistancesBuf[i] = nbt.getDouble("R"+String.valueOf(i));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt, LinkedList<ISESimulatable> neighbors) {	
		nbt.setByte("type", type);
		nbt.setInteger("x", x);
		nbt.setInteger("y", y);
		nbt.setInteger("z", z);
		
		int length = 0;
		for (ISESimulatable neighbor : neighbors){
			if (neighbor instanceof GridNode)
				length++;
		}
		
		neighborX = new int[length];
		neighborY = new int[length];
		neighborZ = new int[length];
		int i = 0;
		for (ISESimulatable neighbor : neighbors){
			if (neighbor instanceof GridNode){
				GridNode gridNode = (GridNode)neighbor;
				neighborX[i] = gridNode.x;
				neighborY[i] = gridNode.y;
				neighborZ[i] = gridNode.z;
				nbt.setDouble("R"+String.valueOf(i), resistances.get(neighbor));
				i++;
			}
		}
		nbt.setIntArray("neigborX", neighborX);
		nbt.setIntArray("neigborY", neighborY);
		nbt.setIntArray("neigborZ", neighborZ);
	}
	
	public String getIDString(){
		return getIDString(x, y, z);
	}
	
	public static String getIDString(int x, int y, int z){
		return String.valueOf(x) + ":" +String.valueOf(y) + ":" + String.valueOf(z);
	}
	
	public static String getIDStringFromTileEntity(TileEntity te){
		return getIDString(te.xCoord, te.yCoord, te.zCoord);
	}
	
	
	//ISEGridObject -----------------------------
	@Override
	public LinkedList<ISESimulatable> getNeighborList(){
		LinkedList<ISESimulatable> ret = new LinkedList<ISESimulatable>();
		for (ISESimulatable obj : gridDataProvider.getNeighborsOf(this)){
			ret.add(obj);
		}
		return ret;
	}
	
	@Override
	public double getResistance(ISEGridNode neighbor){
		if (resistances.containsKey(neighbor))
			return resistances.get(neighbor);
		else
			return Double.NaN;
	}
	
	@Override
	public int getXCoord(){
		return this.x;
	}
	
	@Override
	public int getYCoord(){
		return this.y;
	}
	
	@Override
	public int getZCoord(){
		return this.z;
	}
}
