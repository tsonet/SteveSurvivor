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

class Spike(posX: Int, posY: Int, size: Int, val damage: Int, val velocityX: Int, val velocityY : Int) : Entity(posX, posY, size) {
    override fun draw(g: Graphics2D) {
        g.color = Color.black
        // Draw the spike
        g.drawLine(posX - Hero.posX + WINDOW_WIDTH / 2, posY - Hero.posY + WINDOW_HEIGHT / 2, posX - Hero.posX + WINDOW_WIDTH / 2 + velocityX, posY - Hero.posY + WINDOW_HEIGHT / 2 + velocityY)

    }

    override fun step() {
        posX += velocityX
        posY += velocityY

        // Check if the spike collides
        GameManager.getEnemies().forEach {
            if(it.isColliding(this)) {
                it.health -= damage
                GameManager.drawables -= this
            }
        }

        // if the spike is too far away, remove it
        if(Math.sqrt((posX - Hero.posX).toDouble() * (posX - Hero.posX).toDouble() + (posY - Hero.posY).toDouble() * (posY - Hero.posY).toDouble()) > 1000) {
            GameManager.drawables -= this
        }
    }
}

class SpikeSpell : Spell(FRAMES_PER_SEC / 5) {
    var currentAngle = 0.0
    override fun cast() {
        for (i in 0..20) {
            castSpike(currentAngle)
            currentAngle += PI / 10
        }
        currentAngle += PI / 15

      }

    fun castSpike(angle: Double) {
        GameManager.addDrawable(Spike(Hero.posX, Hero.posY, 10, 25, (20 * Math.cos(angle)).toInt(), (20 * Math.sin(angle)).toInt()))
    }
}