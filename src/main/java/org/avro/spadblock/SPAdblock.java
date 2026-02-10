package org.avro.spadblock;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SPAdblock implements ModInitializer {
    public static final String MOD_ID = "spadblock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("SP Adblock initializing...");
    }
}
