package spells

import Enemy
import Entity
import GameManager
import Renderer.FRAMES_PER_SEC
import Renderer.WINDOW_HEIGHT
import Renderer.WINDOW_WIDTH
import java.awt.Color
import java.awt.Graphics2D

class FireBall(posX: Int, posY: Int, size: Int, val damage: Int, val radius: Int, val target: Enemy) : Entity(posX, posY, size) {
    var explode = false

    override fun draw(g: Graphics2D) {

        if(explode) {
            g.color = Color.orange
            g.fillOval(posX - Hero.posX + WINDOW_WIDTH / 2 - radius / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - radius / 2, radius, radius)
            g.color = Color.black
            // Get all enemies in range
            val enemiesInRange = GameManager.getEnemies().filter { it.distanceTo(this) < radius }
            enemiesInRange.forEach {
                it.health -= damage
            }
            GameManager.drawables -= this
        }
        else {
            g.color = Color.red
            g.fillOval(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2, size, size)
            g.color = Color.black
        }

    }

    override fun step() {
        // If the target is dead, remove the fireball
        if(target.health <= 0) {
            GameManager.drawables -= this
            return
        }

        // Move towards the target
        val dx = target.posX - posX
        val dy = target.posY - posY
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble())
        val velocityX = (dx / distance * speed).toInt()
        val velocityY = (dy / distance * speed).toInt()

        posX += velocityX
        posY += velocityY

        // Check if the spike collides
        GameManager.getEnemies().forEach {
            if(it.isColliding(this)) {
                // Explode and damage in range
                explode = true
            }
        }


    }
}

class FireBallSpell : Spell(FRAMES_PER_SEC / 2) {
    override fun cast() {
        if(GameManager.getEnemies().isNotEmpty()) {
            val targets = GameManager.getEnemies()
            // Get the closest enemy
            val target = targets.minBy { Math.sqrt((it.posX - Hero.posX).toDouble() * (it.posX - Hero.posX).toDouble() + (it.posY - Hero.posY).toDouble() * (it.posY - Hero.posY).toDouble()) }
            GameManager.addDrawable(FireBall(Hero.posX, Hero.posY, 15, 50, 100, target))
        }
    }
}