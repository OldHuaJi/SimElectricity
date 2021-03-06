package simelectricity.essential.grid;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rikka.librikka.math.MathAssitant;
import rikka.librikka.math.Vec3f;
import simelectricity.essential.client.grid.PowerPoleRenderHelper;

public class TilePowerPole extends TilePowerPoleBase {
    @SideOnly(Side.CLIENT)
    public boolean isType0() {
        return this.getBlockMetadata() >> 3 == 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public BlockPos getAccessoryPos() {
    	return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected PowerPoleRenderHelper createRenderHelper() {
        PowerPoleRenderHelper helper;
        int rotation = this.getBlockMetadata() & 7;

        if (this.isType0()) {
            helper = new PowerPoleRenderHelper(this.pos, rotation, 2, 3) {
                @Override
                public void onUpdate() {
                    if (this.connectionInfo.size() < 2)
                        return;

                    PowerPoleRenderHelper.ConnectionInfo[] connection1 = this.connectionInfo.getFirst();
                    PowerPoleRenderHelper.ConnectionInfo[] connection2 = connectionInfo.getLast();

                    float x = -3.95F;
                    float z = 0;
                    float cos = MathAssitant.cosAngle(rotation*45);
                    float sin = MathAssitant.sinAngle(rotation*45);
                    int rotation = 0;
                    Vec3f pos = new Vec3f(
                            x*cos + z*sin + 0.5F + this.pos.getX(),
                            this.pos.getY() + 5,
                            -sin*x + cos*z + 0.5F + this.pos.getZ()
                    );

                    this.addExtraWire(connection1[1].fixedFrom, pos, 2.5F);
                    this.addExtraWire(pos, connection2[1].fixedFrom, 2.5F);
                    if (PowerPoleRenderHelper.hasIntersection(
                            connection1[0].fixedFrom, connection2[0].fixedFrom,
                            connection1[2].fixedFrom, connection2[2].fixedFrom)) {
                        this.addExtraWire(connection1[0].fixedFrom, connection2[2].fixedFrom, 2.5F);
                        this.addExtraWire(connection1[2].fixedFrom, connection2[0].fixedFrom, 2.5F);
                    } else {
                        this.addExtraWire(connection1[0].fixedFrom, connection2[0].fixedFrom, 2.5F);
                        this.addExtraWire(connection1[2].fixedFrom, connection2[2].fixedFrom, 2.5F);
                    }
                }
            };
            helper.addInsulatorGroup(0, 5, -0.7F,
                    helper.createInsulator(2, 3, -4.5F, 0, -1),
                    helper.createInsulator(2, 3, 0, 5, -0.7F),
                    helper.createInsulator(2, 3,  4.5F, 0, -1)
            );
            helper.addInsulatorGroup(0, 5, 0.7F,
                    helper.createInsulator(2, 3, -4.5F, 0, 1),
                    helper.createInsulator(2, 3, 0, 5, 0.7F),
                    helper.createInsulator(2, 3, 4.5F, 0, 1)
            );
        } else {
            helper = new PowerPoleRenderHelper(this.pos, rotation, 1, 3);
            helper.addInsulatorGroup(3.95F, 5, 0,
                    helper.createInsulator(0, 3, -4.9F, -2, 0),
                    helper.createInsulator(0, 3, 3.95F, 5, 0),
                    helper.createInsulator(0, 3, 4.9F, -2, 0)
            );
        }

        return helper;
    }
}
