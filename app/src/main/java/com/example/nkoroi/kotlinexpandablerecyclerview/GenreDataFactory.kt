package com.example.nkoroi.kotlinexpandablerecyclerview

import java.util.*





/**
 * Created by nkoroi on 16/10/17.
 */

object GenreDataFactory {

    fun makeGroups(): List<Group> {
        return Arrays.asList(
                makeRockGenre(),
                makeJazzGenre(),
                makeClassicGenre(),
                makeSalsaGenre(),
                makeBluegrassGenre())
    }


    fun makeRockGenre(): Group {
        return Group(Genre("Rock", R.drawable.ic_banjo), makeRockArtists(), R.drawable.ic_banjo)
    }


    fun makeRockArtists(): List<Artist> {
        val queen = Artist("Queen", true)
        val styx = Artist("Styx", false)
        val reoSpeedwagon = Artist("REO Speedwagon", false)
        val boston = Artist("Boston", true)

        return Arrays.asList(queen, styx, reoSpeedwagon, boston)
    }

    fun makeJazzGenre(): Group {
        return Group(Genre("Jazz", R.drawable.ic_electric_guitar), makeJazzArtists(), R.mipmap.ic_jazz)
    }



    fun makeJazzArtists(): List<Artist> {
        val milesDavis = Artist("Miles Davis", true)
        val ellaFitzgerald = Artist("Ella Fitzgerald", true)
        val billieHoliday = Artist("Billie Holiday", false)

        return Arrays.asList(milesDavis, ellaFitzgerald, billieHoliday)
    }

    fun makeClassicGenre(): Group {
        return Group(Genre("Classic", R.mipmap.ic_classic), makeClassicArtists(), R.mipmap.ic_classic)
    }



    fun makeClassicArtists(): List<Artist> {
        val beethoven = Artist("Ludwig van Beethoven", false)
        val bach = Artist("Johann Sebastian Bach", true)
        val brahms = Artist("Johannes Brahms", false)
        val puccini = Artist("Giacomo Puccini", false)

        return Arrays.asList(beethoven, bach, brahms, puccini)
    }

    fun makeSalsaGenre(): Group {
        return Group(Genre("Salsa", R.mipmap.ic_classic), makeSalsaArtists(),R.mipmap.ic_salsa)
    }



    fun makeSalsaArtists(): List<Artist> {
        val hectorLavoe = Artist("Hector Lavoe", true)
        val celiaCruz = Artist("Celia Cruz", false)
        val willieColon = Artist("Willie Colon", false)
        val marcAnthony = Artist("Marc Anthony", false)

        return Arrays.asList(hectorLavoe, celiaCruz, willieColon, marcAnthony)
    }

    fun makeBluegrassGenre(): Group {
        return Group(Genre("Bluegrass",R.mipmap.ic_bluegrass), makeBluegrassArtists(), R.mipmap.ic_bluegrass)
    }



    fun makeBluegrassArtists(): List<Artist> {
        val billMonroe = Artist("Bill Monroe", false)
        val earlScruggs = Artist("Earl Scruggs", false)
        val osborneBrothers = Artist("Osborne Brothers", true)
        val johnHartford = Artist("John Hartford", false)

        return Arrays.asList(billMonroe, earlScruggs, osborneBrothers, johnHartford)
    }

}