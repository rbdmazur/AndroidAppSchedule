package com.example.schedule

class Utils {
    companion object {
        fun subList(list: List<String>, from: Int): List<String> {
            val result = ArrayList<String>()
            for (i in from until list.size) {
                result.add(list[i])
            }
            return result
        }
    }
}