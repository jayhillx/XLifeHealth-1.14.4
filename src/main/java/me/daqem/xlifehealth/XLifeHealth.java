package me.daqem.xlifehealth;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(XLifeHealth.MOD_ID)
public class XLifeHealth {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "xlifehealth";

    public XLifeHealth() {
        DistExecutor.runForDist(
                () -> SideProxy.Client::new,
                () -> SideProxy.Server::new
        );
    }

}
