package spells

abstract class Spell(var cooldown: Int) {
    var currentCooldown = 0

    fun step() {
        if (currentCooldown > 0) {
            currentCooldown--
        }
        else if(currentCooldown == 0) {
            cast()
            currentCooldown = cooldown
        }
    }

    abstract fun cast()
}