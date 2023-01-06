import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.Timer

object Renderer : JPanel() {

    /**
     * Panel parameters
     */
    const val FRAMES_PER_SEC = 60
    const val FRAME_IN_MSEC = 1000 / FRAMES_PER_SEC
    const val WINDOW_WIDTH = 1280
    const val WINDOW_HEIGHT = 720

    /**
     * Global variables
     */
    var upPressed = false
    var downPressed = false
    var leftPressed = false
    var rightPressed = false
    val frame = JFrame("Game")
    val sprite : BufferedImage = ImageIO.read(File("main/resources/images/map.png"))
    val GOsprite : BufferedImage = ImageIO.read(File("main/resources/images/game_over.png"))
    val hero = Hero

    /**
     * Define Windows settings
     */
    init {
        preferredSize = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        background = Color.white
    }


    fun initGame() {
        SwingUtilities.invokeLater {
            with (frame) {
                defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                title = "Steve Survivor"
                isResizable = false
                add(this@Renderer, BorderLayout.CENTER)
                pack()
                setLocationRelativeTo(null)
                isVisible = true
            }

            val stepTimer = Timer(FRAME_IN_MSEC) { e: ActionEvent? -> stepGame() }
            stepTimer.start()

            // Set up key event handler
            frame.addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent) {
                    when (e.keyCode) {
                        KeyEvent.VK_UP -> upPressed = true
                        KeyEvent.VK_DOWN -> downPressed = true
                        KeyEvent.VK_LEFT -> leftPressed = true
                        KeyEvent.VK_RIGHT -> rightPressed = true
                    }
                }

                override fun keyReleased(e: KeyEvent) {
                    when (e.keyCode) {
                        KeyEvent.VK_UP -> upPressed = false
                        KeyEvent.VK_DOWN -> downPressed = false
                        KeyEvent.VK_LEFT -> leftPressed = false
                        KeyEvent.VK_RIGHT -> rightPressed = false
                    }
                }

                override fun keyTyped(e: KeyEvent) {
                    if(GameManager.state != GameState.SKILL_SELECTION) return
                    when (e.keyChar) {
                        'a' -> GameManager.updgradeSpell1()
                        'z' -> GameManager.updgradeSpell2()
                        'e' -> GameManager.updgradeSpell3()
                        'r' -> GameManager.updgradeSpell4()
                        'h' -> GameManager.updgradeSpell5()
                    }
                }
            })
        }
    }

    private fun stepGame() {
        if(GameManager.state == GameState.GAME) {
            GameManager.stepGame()
        }
        repaint()
    }

    override fun paint(gg: Graphics) {
        super.paintComponent(gg)
        val g = gg as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if(GameManager.state == GameState.GAME_OVER) {

            g.color = Color.black
            g.font = Font("Arial", Font.BOLD, 50)
            g.drawImage(GOsprite,0, 0, null)
            return
        }

        if(GameManager.state == GameState.SKILL_SELECTION) {
            // Choose a skill
            g.color = Color.black
            g.font = Font("Arial", Font.BOLD, 50)
            g.drawString("Choose a skill", WINDOW_WIDTH / 2 - 200, WINDOW_HEIGHT / 2 - 100)

            // Use a, z, e, r, h to choose a skill
            g.font = Font("Arial", Font.BOLD, 30)
            g.drawString("a - CD - Fireball", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2)
            g.drawString("z - CD - Spike", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 + 50)
            g.drawString("e - More SPEED", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 + 100)
            g.drawString("r - CD - Area", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 + 150)
            g.drawString("h - Heal UP", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2 + 200)

            return
        }

        // Draw the background
        g.drawImage(sprite,0 - hero.posX + WINDOW_WIDTH / 2 - sprite.width / 2, 0 - hero.posY + WINDOW_HEIGHT / 2 - sprite.height / 2 , null)

        // Draw the entities
        GameManager.drawables.forEach { it.draw(g) }

        // Draw the GUI
        val killsCount = GameManager.killCount
        val killsText = "Kills: $killsCount"
        g.color = Color.white
        g.drawString(killsText, 10, 20)

        // Draw coordonées
        g.drawString("X : ${hero.posX}, Y : ${hero.posY}", 10, 60)

        val secondsSurvived = GameManager.secondsSurvived
        val secondsSurvivedText = "Seconds survived: $secondsSurvived"
        g.drawString(secondsSurvivedText, 10, 40)

        //================= Draw XP Bar =================//
        val xpNeeded = GameManager.nextLevelKillsNeeded - GameManager.lastLevelKillsNeeded
        val xp = GameManager.killCount - GameManager.lastLevelKillsNeeded
        val xpBarWidth = 1200
        val xpBarHeight = 20
        val xpBarX = WINDOW_WIDTH - xpBarWidth - 33
        val xpBarY = 680
        g.color = Color.DARK_GRAY
        g.drawRect(xpBarX, xpBarY, xpBarWidth, xpBarHeight)
        g.color = Color.ORANGE
        g.fillRect(xpBarX, xpBarY, (xpBarWidth * xp / xpNeeded).toInt(), xpBarHeight)

        g.color = Color.WHITE
        val level = GameManager.level
        val levelText = "Level: $level"
        g.drawString(levelText, WINDOW_WIDTH /2 - 40 , 695)

        manageInputs()
    }

    private fun manageInputs() {
        /**
         * Move the Hero
         */
        if(upPressed && leftPressed) {
            Hero.moveUpLeft()
        }
        else if(upPressed && rightPressed) {
            Hero.moveUpRight()
        }
        else if(downPressed && leftPressed) {
            Hero.moveDownLeft()
        }
        else if(downPressed && rightPressed) {
            Hero.moveDownRight()
        }
        else if(upPressed) {
            Hero.moveUp()
        }
        else if(downPressed) {
            Hero.moveDown()
        }
        else if(leftPressed) {
            Hero.moveLeft()
        }
        else if(rightPressed) {
            Hero.moveRight()
        }
        else {
            /**
             * Invalid Move
             */
        }
    }
}
