package com.glacier.androidslide.util

import kotlin.random.Random


class UtilManager {

    companion object {
        fun generateID(length: Int): String {
            val charset = "abcdefghijklmnopqrstuvwsyz0123456789"
            val randomString = (1..length)
                .map { charset[Random.nextInt(charset.length)] }
                .joinToString("")

            // chunk -> 리스트 하나를 주어진 size의 리스트로 나누는 함수
            return randomString.chunked(3).joinToString("-")
        }

        fun getRandomColor(): List<Int> {
            val r = Random.nextInt(0, 256)
            val g = Random.nextInt(0, 256)
            val b = Random.nextInt(0, 256)
            return listOf(r, g, b)
        }

        fun rgbToHex(r: Int, g: Int, b: Int): String {
            val hexR = Integer.toHexString(r).uppercase()
            val hexG = Integer.toHexString(g).uppercase()
            val hexB = Integer.toHexString(b).uppercase()

            return "0x" + hexR.padStart(2, '0') + hexG.padStart(2, '0') + hexB.padStart(2, '0')
        }

        fun getAlphaMode(mode: Int): Int {
            return if (mode == 10) {
                255
            } else {
                (255 / 10) * mode
            }

        }

    }

}