import java.awt.Graphics2D

interface Drawable {
    fun draw(g: Graphics2D)
    fun step()
}