package spells

import Enemy
import Entity
import GameManager
import Renderer.FRAMES_PER_SEC
import Renderer.WINDOW_HEIGHT
import Renderer.WINDOW_WIDTH
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.PI

class Area(posX: Int, posY: Int, size: Int, val damage: Int) : Entity(posX, posY, size) {
    override fun draw(g: Graphics2D) {
        // Draw the area
        g.color = Color.cyan
        g.fillOval(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2, size, size)
        g.color = Color.black
    }

    override fun step() {
        // Check if the spike collides
        GameManager.getEnemies().forEach {
            if(it.isColliding(this)) {
                it.health -= damage
            }
        }
        GameManager.drawables -= this
    }
}

class AreaSpell : Spell(FRAMES_PER_SEC / 5) {
    override fun cast() {
        GameManager.addDrawable(Area(Hero.posX - 15, Hero.posY, 250, 25))
    }
}