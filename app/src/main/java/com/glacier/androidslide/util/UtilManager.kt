package com.glacier.androidslidesofteer.util

import java.util.Random

class UtilManager {

    companion object {
        fun generateID(length: Int): String {
            val charset = "abcdefghijklmnopqrstuvwsyz0123456789"
            val randomString = (1..length)
                .map { charset[Random().nextInt(charset.length)] }
                .joinToString("")

            // chunk -> 리스트 하나를 주어진 size의 리스트로 나누는 함수
            return randomString.chunked(3).joinToString("-")
        }
    }

}