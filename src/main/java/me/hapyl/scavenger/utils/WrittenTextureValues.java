package me.hapyl.scavenger.utils;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

import static org.bukkit.entity.EntityType.*;

public class WrittenTextureValues {

    public static final Map<EntityType, String> entityTexture = Maps.newHashMap();
    public static final Map<EntityDamageEvent.DamageCause, Material> damageCauseTexture = Maps.newHashMap();

    static {
        entityTexture.put(ELDER_GUARDIAN, "e92089618435a0ef63e95ee95a92b83073f8c33fa77dc5365199bad33b6256");
        entityTexture.put(WITHER_SKELETON, "f5ec964645a8efac76be2f160d7c9956362f32b6517390c59c3085034f050cff");
        entityTexture.put(STRAY, "78ddf76e555dd5c4aa8a0a5fc584520cd63d489c253de969f7f22f85a9a2d56");
        entityTexture.put(HUSK, "d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121");
        entityTexture.put(ZOMBIE_VILLAGER, "2a6e186a2b3d5d427172b74dbd4dacaf287e7e8ffd9bbd1a47ac4ece506ad2ff");
        entityTexture.put(SKELETON_HORSE, "47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a");
        entityTexture.put(ZOMBIE_HORSE, "f896a1dbde1a19540ce7336c6c94f59652aa98bb1068b2ef8c8fa6ef85804f57"); // not spawning
        entityTexture.put(ARMOR_STAND, "993993674424e62f0a461c6268f8854541e024c7d3b416a250b2b4d11b50179d");
        entityTexture.put(DONKEY, "63a976c047f412ebc5cb197131ebef30c004c0faf49d8dd4105fca1207edaff3");
        entityTexture.put(MULE, "a0486a742e7dda0bae61ce2f55fa13527f1c3b334c57c034bb4cf132fb5f5f");
        entityTexture.put(EVOKER, "d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6");
        entityTexture.put(VEX, "c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd");
        entityTexture.put(VINDICATOR, "6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173");
        entityTexture.put(CREEPER, "f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952");
        entityTexture.put(SKELETON, "d6ea4e7464484fdfed5d50f3a50839323ffb2fb3305d4bd27f3b293e879f985b");
        entityTexture.put(SPIDER, "a3ead81326ed5d5aa97b53b0f540b825b79cbf4855ce9b90fe73ea2311eb3a");
        entityTexture.put(ZOMBIE, "7fc7c9f1789443019f6ce7b02a37217838e7e5745e205d36d6fe1d124025ea2c");
        entityTexture.put(SLIME, "895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c");
        entityTexture.put(GHAST, "de8a38e9afbd3da10d19b577c55c7bfd6b4f2e407e44d4017b23be9167abff02");
        entityTexture.put(ZOMBIFIED_PIGLIN, "7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c");
        entityTexture.put(ENDERMAN, "120baf2ed7f2326803165ad801fc056d002243be8ccf2d87ea26b9c76dc3fa6e");
        entityTexture.put(CAVE_SPIDER, "41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224");
        entityTexture.put(SILVERFISH, "da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540");
        entityTexture.put(BLAZE, "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0");
        entityTexture.put(MAGMA_CUBE, "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429");
        entityTexture.put(BAT, "6f47935fee11e5c0e5d26e3df058cbf0634b4301f20edd06a8da56975639a66");
        entityTexture.put(WITCH, "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa");
        entityTexture.put(ENDERMITE, "5a1a0831aa03afb4212adcbb24e5dfaa7f476a1173fce259ef75a85855");
        entityTexture.put(GUARDIAN, "495290e090c238832bd7860fc033948c4d031353533ac8f67098823b7f667f1c");
        entityTexture.put(PIG, "7fe9725c950472e469b9fccae32f61bcefebdb5ea9ce9c92d58171ffb7a336fe");
        entityTexture.put(SHEEP, "32b6adb3b6e83c074e7d0ebaff14d6acbbabeb06694d6ee38cc8dd1bf6630bc6");
        entityTexture.put(COW, "d6551840955f524367580f11b35228938b6786397a8f2e8c8cc6b0eb01b5db3d");
        entityTexture.put(CHICKEN, "ef5ff5451c03600fda0d632f69a354b96743eed0f45802a02e39ced99fa6a04f");
        entityTexture.put(SQUID, "464bdc6f600656511bef596c1a16aab1d3f5dbaae8bee19d5c04de0db21ce92c");
        entityTexture.put(WOLF, "28d408842e76a5a454dc1c7e9ac5c1a8ac3f4ad34d6973b5275491dff8c5c251");
        entityTexture.put(MUSHROOM_COW, "d3ebf38b4a708eb00745d1fb87a53cb81a7af6cc178d9a2c1116c2cbcff94fea");
        entityTexture.put(SNOWMAN, "5777d3da4329bdf60e9d2678ab790834de9096f9d86a0e99a1ab8a43aa5ec1e4");
        entityTexture.put(OCELOT, "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1");
        entityTexture.put(IRON_GOLEM, "a82d7115d01c2c0a4110053677c3bf095546daa6d8e12ccf148010182f3b0ed1");
        entityTexture.put(HORSE, "85ce194a54315acc3bf9db7edf6e7da29f49524b1b8af0ef9e4ac3df2280b0d8");
        entityTexture.put(RABBIT, "ef98337b8242229d95da23090755789778b18bf5d07d61f620cdbdbbd29fa615");
        entityTexture.put(POLAR_BEAR, "c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90");
        entityTexture.put(LLAMA, "7f832466dcc7d5e7702cdee4cd555dbd39637d20adf9367fb03cfd6888baaae7");
        entityTexture.put(PARROT, "c38796f62db5f93949ae26a2f7a3c5f797a31d2694bce4c48ee843ee85f7");
        entityTexture.put(VILLAGER, "4ca8ef2458a2b10260b8756558f7679bcb7ef691d41f534efea2ba75107315cc");
        entityTexture.put(TURTLE, "212b58c841b394863dbcc54de1c2ad2648af8f03e648988c1f9cef0bc20ee23c");
        entityTexture.put(PHANTOM, "7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982");
        entityTexture.put(COD, "7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0");
        entityTexture.put(SALMON, "23bf8fca2af3592c5574b13e3bcf61e2fae829788535f0ddeaa7a2e45b6ba4");
        entityTexture.put(PUFFERFISH, "9e22a4e39537a049eaed8472ff7500d4efa4fd44ea8cc6402415992e2e75dd1");
        entityTexture.put(TROPICAL_FISH, "d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281");
        entityTexture.put(DROWNED, "c3f7ccf61dbc3f9fe9a6333cde0c0e14399eb2eea71d34cf223b3ace22051");
        entityTexture.put(DOLPHIN, "8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3");
        entityTexture.put(CAT, "af916bbe9846aed30e93880593b630c4ffc635518f0fb7ec11e8c0ecfc7a91fc");
        entityTexture.put(PANDA, "426f2fa8b5b4b4d70c4132a71336d8f4da5419258dfcad18cca8459cf5623903");
        entityTexture.put(PILLAGER, "138b41ee17dca75c325d69c5be1daafcae5ae48dbcf9e5fc2c26635bd4e0362d");
        entityTexture.put(RAVAGER, "1cb9f139f9489d86e410a06d8cbc670c8028137508e3e4bef612fe32edd60193");
        entityTexture.put(TRADER_LLAMA, "8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d");
        entityTexture.put(WANDERING_TRADER, "5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932");
        entityTexture.put(FOX, "d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a");
        entityTexture.put(BEE, "e9bf5e927449bfe8b0de578b7b1d582595d9fce2354b5284b9882c56ebcc933a");
        entityTexture.put(HOGLIN, "9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75");
        entityTexture.put(PIGLIN, "d71b3aee182b9a99ed26cbf5ecb47ae90c2c3adc0927dde102c7b30fdf7f4545");
        entityTexture.put(STRIDER, "65ccbb547820b667cc9d3bc9fff1e3d65da2375655a3427b30e1d009eeb272ce");
        entityTexture.put(ZOGLIN, "e67e18602e03035ad68967ce090235d8996663fb9ea47578d3a7ebbc42a5ccf9");
        entityTexture.put(PIGLIN_BRUTE, "3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf");
        entityTexture.put(AXOLOTL, "d704254139a0b1a926e7552482dd67679c6ae0dc8335c980dbd1c0d99634a708");
        entityTexture.put(GLOW_SQUID, "2ecd0b5eb6b384db076d8446065202959dddff0161e0d723b3df0cc586d16bbd");
        entityTexture.put(GOAT, "a662336d8ae092407e58f7cc80d20f20e7650357a454ce16e3307619a0110648");
        entityTexture.put(ALLAY, "40e1c7064af7dee68677efaa95f6e6e01430b006dd91638ea2a61849254488ec");
        entityTexture.put(FROG, "dac2f6846848dcaf51e6fd2998090acd64dfdf3d926da78b97c9b116bb37af33");
        entityTexture.put(TADPOLE, "6cc9b9740bd3adeba52e0ce0a77b3dfdef8d3a40555a4e8bb67d200cd62770d0");
        entityTexture.put(WARDEN, "c6f74361fb00490a0a98eeb814544ecdd775cb55633dbb114e60d27004cb1020");

        damageCauseTexture.put(EntityDamageEvent.DamageCause.ENTITY_ATTACK, Material.DIAMOND_SWORD);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.PROJECTILE, Material.ARROW);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.SUFFOCATION, Material.GRAVEL);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.FALL, Material.FEATHER);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.FIRE, Material.FIRE_CHARGE);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.FIRE_TICK, Material.FIRE_CHARGE);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.LAVA, Material.LAVA_BUCKET);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.DROWNING, Material.WATER_BUCKET);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, Material.TNT);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, Material.CREEPER_HEAD);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.LIGHTNING, Material.LIGHTNING_ROD);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.STARVATION, Material.ROTTEN_FLESH);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.POISON, Material.POISONOUS_POTATO);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.MAGIC, Material.PURPLE_DYE);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.WITHER, Material.WITHER_ROSE);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.FALLING_BLOCK, Material.ANVIL);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.HOT_FLOOR, Material.MAGMA_BLOCK);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.CRAMMING, Material.MINECART);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.DRYOUT, Material.ICE);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.FREEZE, Material.SNOWBALL);
        damageCauseTexture.put(EntityDamageEvent.DamageCause.SONIC_BOOM, Material.ECHO_SHARD);

    }

}
