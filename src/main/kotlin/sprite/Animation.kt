package sprite

import java.awt.image.BufferedImage


class Animation(
    frames: Array<BufferedImage>, // frame delay 1-12 (You will have to play around with this)
    private val frameDelay: Int
) {
    private var frameCount // Counts ticks for change
            : Int
    private var currentFrame // animations current frame
            : Int
    private val animationDirection // animation direction (i.e counting forward or backward)
            : Int
    private val totalFrames // total amount of frames for your animation
            : Int
    private var stopped // has animations stopped
            = true
    private val frames: MutableList<Frame> = ArrayList<Frame>() // Arraylist of frames

    init {
        for (i in frames.indices) {
            addFrame(frames[i], frameDelay)
        }
        frameCount = 0
        currentFrame = 0
        animationDirection = 1
        totalFrames = this.frames.size
        start()
    }

    fun start() {
        if (!stopped) {
            return
        }
        if (frames.size == 0) {
            return
        }
        stopped = false
    }

    fun stop() {
        if (frames.size == 0) {
            return
        }
        stopped = true
    }

    fun restart() {
        if (frames.size == 0) {
            return
        }
        stopped = false
        currentFrame = 0
    }

    fun reset() {
        stopped = true
        frameCount = 0
        currentFrame = 0
    }

    private fun addFrame(frame: BufferedImage, duration: Int) {
        if (duration <= 0) {
            System.err.println("Invalid duration: $duration")
            throw RuntimeException("Invalid duration: $duration")
        }
        frames.add(Frame(frame, duration))
        currentFrame = 0
    }

    val sprite: BufferedImage
        get() = frames[currentFrame].frame

    fun update() {
        if (!stopped) {
            frameCount++
            if (frameCount > frameDelay) {
                frameCount = 0
                currentFrame += animationDirection
                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0
                } else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1
                }
            }
        }
    }
}