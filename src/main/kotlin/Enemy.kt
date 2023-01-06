import Renderer.WINDOW_HEIGHT
import Renderer.WINDOW_WIDTH
import sprite.Animation
import sprite.Sprite
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

enum class EnemyType(val health: Int, val speed: Int, val damage: Int, val color: Color, val size: Int, val sprite: Animation) {

    NORMAL(50, 1, 5, Color.green, 15,
        Animation(arrayOf(
            Sprite.getZombieSprite(1, 0),
            Sprite.getZombieSprite(1, 1),
            Sprite.getZombieSprite(0, 1),
            Sprite.getZombieSprite(1, 2)), 5)
    ),
    FAST(70, 5, 10, Color.yellow, 30,
        Animation(arrayOf(
            Sprite.getCreeperSprite(1, 0),
            Sprite.getCreeperSprite(1, 1),
            Sprite.getCreeperSprite(0, 1),
            Sprite.getCreeperSprite(1, 2)), 5)
    ),
    STRONG(200, 1, 20, Color.red, 30,
        Animation(arrayOf(
            Sprite.getEndermanSprite(1, 0),
            Sprite.getEndermanSprite(1, 1),
            Sprite.getEndermanSprite(0, 1),
            Sprite.getEndermanSprite(1, 2)), 5)
    ),
    STRONG2(500, 1, 20, Color.blue, 30,
        Animation(arrayOf(
            Sprite.getDragonSprite(1, 0),
            Sprite.getDragonSprite(1, 1),
            Sprite.getDragonSprite(0, 1),
            Sprite.getDragonSprite(1, 2)), 5)
    )
}

object EnemyFactory {
    fun createEnemy(type: EnemyType): Enemy {
        // Get random pos at a radius of 1000 from the hero
        val heroX = Hero.posX
        val heroY = Hero.posY
        val randomAngle = Math.random() * 2 * Math.PI
        val randomRadius = 500
        val randomX = (heroX + randomRadius * Math.cos(randomAngle)).toInt()
        val randomY = (heroY + randomRadius * Math.sin(randomAngle)).toInt()
        return Enemy(randomX, randomY, type.size, type.color, type.health, type.speed, type.damage, type.sprite)
    }

    fun getRandomType(): EnemyType {
        val random = Math.random()
        return when {
            random < 0.7 -> EnemyType.NORMAL
            random < 0.95 -> EnemyType.FAST
            random < 0.99 -> EnemyType.STRONG
            else -> EnemyType.STRONG2
        }
    }
}

class Enemy(posX: Int, posY: Int, size: Int, val color: Color, val initialHealth: Int, val initialSpeed: Int, val initialDamage: Int, val currentAnimation: Animation) : Entity(posX, posY, size, initialSpeed) {

    var damage = initialDamage
    var health = initialHealth
    var dying = false
    var dyingCounter = 0

    fun isAlive() = health > 0

    override fun draw(g: Graphics2D) {
        // If the enemy is dying, draw a red cross
        if(dying) {
            g.color = Color.red
            g.drawLine(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2, posX - Hero.posX + WINDOW_WIDTH / 2 + size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 + size / 2)
            g.drawLine(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 + size / 2, posX - Hero.posX + WINDOW_WIDTH / 2 + size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2)
        }
        else {
            // Draw the enemy
            g.color = color
            //g.fillOval(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2, size, size)
            g.color = Color.black

            val image = currentAnimation.sprite
            val at = AffineTransform.getTranslateInstance((posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2).toDouble(), (posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2).toDouble())
            at.rotate(getRotationFromLastMove(), size / 2.0, size / 2.0)
            g.drawImage(image, at, null)

            // Draw health bar
            if(health < initialHealth) {
                g.fillRect(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2 - 10, size, 5)
                g.color = Color.red
                g.fillRect(posX - Hero.posX + WINDOW_WIDTH / 2 - size / 2, posY - Hero.posY + WINDOW_HEIGHT / 2 - size / 2 - 10, (health * size) / initialHealth, 5)
            }
        }
    }

    override fun step() {
        currentAnimation.update()
        // Check if dead
        if (!isAlive()) {
            if(!dying) {
                dying = true
                GameManager.enemyKilled()
            }

            dyingCounter++
            if(dyingCounter > 100) {
                GameManager.drawables -= this
            }
        } else {
            // Move towards the hero
            val heroX = Hero.posX
            val heroY = Hero.posY
            val angle = Math.atan2((heroY - posY).toDouble(), (heroX - posX).toDouble())
            val velocity = 5
            val velocityX = (velocity * Math.cos(angle)).toInt()
            val velocityY = (velocity * Math.sin(angle)).toInt()

            val enemies = GameManager.getEnemies()
            // Add repulsion from other enemies

            // Repulsion from other enemies
            val repulsion = 100
            val repulsionX = enemies.filter { it != this && this.distanceTo(it) < size}.map { repulsion * (posX - it.posX) / Math.pow(distanceTo(it), 2.0) }.sum().toInt()
            val repulsionY = enemies.filter { it != this && this.distanceTo(it) < size}.map { repulsion * (posY - it.posY) / Math.pow(distanceTo(it), 2.0) }.sum().toInt()

            posX += velocityX + repulsionX
            posY += velocityY + repulsionY

            // Determine Direction from posX and posY
            val direction = when {
                velocityX > 0 && velocityY > 0 -> Direction.SOUTH_EAST
                velocityX > 0 && velocityY < 0 -> Direction.NORTH_EAST
                velocityX < 0 && velocityY > 0 -> Direction.SOUTH_WEST
                velocityX < 0 && velocityY < 0 -> Direction.NORTH_WEST
                velocityX > 0 -> Direction.EAST
                velocityX < 0 -> Direction.WEST
                velocityY > 0 -> Direction.SOUTH
                velocityY < 0 -> Direction.NORTH
                else -> Direction.NORTH
            }
            lastMove = direction
            lastMoveTimestamp = System.currentTimeMillis()

            // Check if the enemy is colliding with the hero
            if (distanceTo(Hero) < Hero.size / 2 + size / 2) {
                Hero.health -= damage
                health = 0 // Kill enemy on collision
            }

        }
    }
}