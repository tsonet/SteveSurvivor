package sprite

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO


object Sprite {
    private var zombieSpriteSheet: BufferedImage? = null
    private var creeperSpriteSheet: BufferedImage? = null
    private var endermanSpriteSheet: BufferedImage? = null
    private var dragonSpriteSheet: BufferedImage? = null
    const val TILE_SIZE = 32

    fun loadSprite(file: String): BufferedImage? {
        var sprite: BufferedImage? = null
        try {
            sprite = ImageIO.read(javaClass.getResource("/images/$file.png"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sprite
    }

    fun getZombieSprite(xGrid: Int, yGrid: Int): BufferedImage {
        if (zombieSpriteSheet == null) {
            zombieSpriteSheet = loadSprite("zombie_spritesheet")
        }
        return zombieSpriteSheet!!.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE)
    }

    fun getCreeperSprite(xGrid: Int, yGrid: Int): BufferedImage {
        if (creeperSpriteSheet == null) {
            creeperSpriteSheet = loadSprite("creeper_spritesheet")
        }
        return creeperSpriteSheet!!.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE)
    }

    fun getEndermanSprite(xGrid: Int, yGrid: Int): BufferedImage {
        if (endermanSpriteSheet == null) {
            endermanSpriteSheet = loadSprite("enderman_spritesheet")
        }
        return endermanSpriteSheet!!.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE)
    }

    fun getDragonSprite(xGrid: Int, yGrid: Int): BufferedImage {
        if (dragonSpriteSheet == null) {
            dragonSpriteSheet = loadSprite("dragon_spritesheet")
        }
        return dragonSpriteSheet!!.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE)
    }
}