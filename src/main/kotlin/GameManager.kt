import spells.AreaSpell
import spells.FireBallSpell
import spells.SpikeSpell

enum class GameState {
    GAME, SKILL_SELECTION, GAME_OVER
}
object GameManager {
    var state = GameState.GAME

    var enemyCooldown = 10
    var enemySpawnCount = 1
    var drawables = listOf<Drawable>()
    var stepCount = 0
    var killCount = 0
    var secondsSurvived = 0
    var level = 1
    var nextLevelKillsNeeded = 10
    var lastLevelKillsNeeded = 0
    var countsp1 : Int = 0 // Count number of amelioration spell 1 get
    var countsp2 : Int = 0 // Count number of amelioration spell 2 get
    var countsp4 : Int = 0 // Count number of amelioration spell 4 get


    fun initGame() {
        createHero()
        Renderer.initGame()
    }

    fun addDrawable(drawable: Drawable) {
        drawables += drawable
    }

    fun enemyKilled() {
        killCount++
        if (killCount >= nextLevelKillsNeeded) {
            level++
            state = GameState.SKILL_SELECTION
            lastLevelKillsNeeded = nextLevelKillsNeeded
            nextLevelKillsNeeded += 10 * level
        }
    }

    fun getEnemies(): List<Enemy> {
        return drawables.filterIsInstance<Enemy>().filter { it.isAlive() }
    }

    private fun createHero() {
        addDrawable(Hero)
        Hero.spells.add(SpikeSpell())
        Hero.spells.add(FireBallSpell())
        Hero.spells.add(AreaSpell())
    }


    fun stepGame() {
        stepCount++
        this.drawables.forEach { it.step() }
        if(stepCount % enemyCooldown == 0) {
            for (i in 0..enemySpawnCount)
                addDrawable(EnemyFactory.createEnemy(EnemyFactory.getRandomType()))
        }

        if(stepCount % 60 == 0) {
            secondsSurvived++
            if(secondsSurvived % 30 == 0) {
                enemySpawnCount++
            }
        }
    }

    fun endGame() {
        state = GameState.GAME_OVER
    }

    fun updgradeSpell1() {
        if (countsp1 < 12){
            println(" ✔ Upgrading spell 1 [FireBall]")
            Hero.spells.filterIsInstance<FireBallSpell>().first().cooldown -= 5
            state = GameState.GAME
            countsp1++
        }
        else{
            println(" ❌ Spell 1 get maximum level [FireBall]")
            state = GameState.GAME
        }
    }
    fun updgradeSpell2() {
        if (countsp2 < 12) {
            println(" ✔ Upgrading spell 2 [Spike]")
            Hero.spells.filterIsInstance<SpikeSpell>().first().cooldown -= 1
            state = GameState.GAME
            countsp2++
        }
        else{
            println(" ❌ Spell 2 get maximum level [Spike]")
            state = GameState.GAME
        }
    }

    fun updgradeSpell3() {
        println(" ✔ Upgrading spell 3 [Speed]")
        Hero.speed += 1
        state = GameState.GAME
    }

    fun updgradeSpell4() {
        if (countsp4 < 12) {
            println(" ✔ Upgrading spell 4 [Area]")
            Hero.spells.filterIsInstance<AreaSpell>().first().cooldown -= 1
            state = GameState.GAME
            countsp4++
        }
        else{
            println(" ❌ Spell 4 get maximum level [Spike]")
            state = GameState.GAME
        }
    }

    fun updgradeSpell5() {
        println(" ✔ Upgrading spell 5 [Health]")
        Hero.health += 15
        state = GameState.GAME
    }

}