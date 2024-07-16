package me.chaoticwagon.vivarium.worldgen

import me.chaoticwagon.vivarium.util.OpenSimplexNoise

class TerrainGen {

    init {
        // Lower side = oceans, higher side = far inland
        val continentals = OpenSimplexNoise()
        val temperature = OpenSimplexNoise()
        val humidity = OpenSimplexNoise()


    }
}