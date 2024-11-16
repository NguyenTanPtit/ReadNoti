package com.vt.readnoti

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class MoneySpeaker(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val soundQueue = mutableListOf<String>()

    fun speakAmount(amount: Int) {
        soundQueue.clear()
        soundQueue.add("intro")

        val parts = parseAmountToParts(amount)
        soundQueue.addAll(parts)

        soundQueue.add("dong")
        playNextSound()
    }

    private fun parseAmountToParts(amount: Int): List<String> {
        val parts = mutableListOf<String>()

        var remaining = amount
        if (remaining >= 1_000_000_000) {
            val billions = remaining / 1_000_000_000
            parts.add("number_$billions")
            parts.add("billion")
            remaining %= 1_000_000_000
        }
        if (remaining >= 1_000_000) {
            val millions = remaining / 1_000_000
            parts.add("number_$millions")
            parts.add("million")
            remaining %= 1_000_000
        }
        if (remaining >= 1_000) {
            val thousands = remaining / 1_000
            parts.add("number_$thousands")
            parts.add("thousand")
            remaining %= 1_000
        }
        if (remaining >= 100) {
            val hundreds = remaining / 100
            parts.add("number_$hundreds")
            parts.add("hundred")
            remaining %= 100
        }
        if (remaining >= 10) {
            val tens = remaining / 10
            parts.add("number_$tens")
            parts.add("ten")
            remaining %= 10
        }
        if (remaining > 0) {
            parts.add("number_$remaining")
        }

        return parts
    }

    private fun playNextSound() {
        if (soundQueue.isNotEmpty()) {
            val fileName = soundQueue.removeAt(0)
            val resId = context.resources.getIdentifier(fileName, "raw", context.packageName)
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(context, resId)
                mediaPlayer?.setOnCompletionListener {
                    it.release()
                    playNextSound()
                }
                mediaPlayer?.setVolume(1.0f, 1.0f) // Set the volume to maximum
                mediaPlayer?.start()
            } else {
                Log.e("MoneySpeaker", "Sound file not found: $fileName")
                playNextSound()
            }
        }
    }
}