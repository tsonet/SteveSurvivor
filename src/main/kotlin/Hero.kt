import Renderer.WINDOW_HEIGHT
import Renderer.WINDOW_WIDTH
import spells.Spell
import sprite.Animation
import sprite.Sprite.TILE_SIZE
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Hero : Entity(0, 0, TILE_SIZE) {
    val sprite : BufferedImage = ImageIO.read(File("main/resources/images/steve.png"))
    val spells: MutableList<Spell> = mutableListOf()
    val initialHealth = 100
    var health = initialHealth

    fun isAlive() = health > 0

    override fun draw(g: Graphics2D) {
        val centerX = WINDOW_WIDTH / 2
        val centerY = WINDOW_HEIGHT / 2

        // Draw the hero always in the center of the screen
        g.drawImage(sprite, centerX - 42, centerY - 42, null)

        // Draw the health bar
        if(health <= initialHealth) {
            g.fillRect(centerX - 29, centerY - 24, 35, 5)
            g.color = Color.green
            g.fillRect(centerX - 29, centerY - 24, (health * 35) / initialHealth, 5)
        }

    }

    override fun step() {
        spells.forEach { it.step() }
        // Check if dead
        if (!isAlive()) {
            GameManager.endGame()
        }
    }
}