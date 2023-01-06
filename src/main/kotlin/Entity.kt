
enum class Direction {
    NORTH, SOUTH, WEST, EAST,
    NORTH_WEST, NORTH_EAST,
    SOUTH_WEST, SOUTH_EAST
}

abstract class Entity(var posX: Int, var posY: Int, var size: Int, var speed: Int = 10) : Drawable {
    var lastMove: Direction = Direction.SOUTH
    var lastMoveTimestamp: Long = 0

    fun distanceTo(other: Entity): Double {
        val dx = other.posX - posX
        val dy = other.posY - posY
        return Math.sqrt((dx * dx + dy * dy).toDouble())
    }

    fun isColliding(e: Entity): Boolean {
        // Computes the distance between the hero and the enemy
        val distance = Math.sqrt(Math.pow((posX - e.posX).toDouble(), 2.0) + Math.pow((posY - e.posY).toDouble(), 2.0))
        // If the distance is less than the sum of the radius, the hero is colliding with the enemy
        return distance < (size / 2 + e.size / 2)
    }

    fun getRotationFromLastMove(): Double {
        return when(lastMove) {
            Direction.NORTH -> Math.toRadians(180.0)
            Direction.SOUTH -> Math.toRadians(0.0)
            Direction.WEST -> Math.toRadians(90.0)
            Direction.EAST -> Math.toRadians(-90.0)
            Direction.NORTH_WEST -> Math.toRadians(180 - 45.0)
            Direction.NORTH_EAST -> Math.toRadians(45.0 + 180)
            Direction.SOUTH_WEST -> Math.toRadians(45.0)
            Direction.SOUTH_EAST -> Math.toRadians(-45.0)
            else -> 0.0
        }
    }

    fun moveUp() {
        if (posY > -2970){
            posY -= speed
            lastMove = Direction.NORTH
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveDown() {
        if (posY < 2970){
            posY += speed
            lastMove = Direction.SOUTH
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveLeft() {
        if (posX > -4340){
            posX -= speed
            lastMove = Direction.WEST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveRight() {
        if (posX < 1635 || posY in (-650..-600)){
            posX += speed
            lastMove = Direction.EAST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveUpLeft() {
        val speedX = speed * Math.cos(Math.PI / 4)
        val speedY = speed * Math.sin(Math.PI / 4)
        if (posX > -4340 && posY > -2960) {
            posX -= speedX.toInt()
            posY -= speedY.toInt()
            lastMove = Direction.NORTH_WEST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveUpRight() {
        val speedX = speed * Math.cos(Math.PI / 4)
        val speedY = speed * Math.sin(Math.PI / 4)
        if (posX < 1635 && posY > -2970) {
            posX += speedX.toInt()
            posY -= speedY.toInt()
            lastMove = Direction.NORTH_EAST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveDownLeft() {
        val speedX = speed * Math.cos(Math.PI / 4)
        val speedY = speed * Math.sin(Math.PI / 4)
        if (posY < 2970 && posX > -4340) {
            posX -= speedX.toInt()
            posY += speedY.toInt()
            lastMove = Direction.SOUTH_WEST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
    fun moveDownRight() {
        val speedX = speed * Math.cos(Math.PI / 4)
        val speedY = speed * Math.sin(Math.PI / 4)
        if (posY < 2970 && posX < 1635) {
            posX += speedX.toInt()
            posY += speedY.toInt()
            lastMove = Direction.SOUTH_EAST
            lastMoveTimestamp = System.currentTimeMillis()
        }
    }
}