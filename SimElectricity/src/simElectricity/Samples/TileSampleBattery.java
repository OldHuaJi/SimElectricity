package simElectricity.Samples;

import simElectricity.API.BaseComponent;
import simElectricity.API.IPowerSource;

public class TileSampleBattery extends BaseComponent implements IPowerSource {

	@Override
	public int getResistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onOverloaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxPowerDissipation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutputVoltage() {
		// TODO Auto-generated method stub
		return 12;
	}

}