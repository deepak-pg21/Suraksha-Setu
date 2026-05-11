package com.mindmatrix.surakshasetu.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var audioFile: File? = null

    fun startRecording() {

        audioFile = File(
            context.externalCacheDir,
            "emergency_audio.mp3"
        )

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        recorder?.apply {

            setAudioSource(MediaRecorder.AudioSource.MIC)

            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)

            setOutputFile(audioFile?.absolutePath)

            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            prepare()

            start()
        }
    }

    fun stopRecording() {

        recorder?.apply {

            stop()

            release()
        }

        recorder = null
    }

    fun getAudioPath(): String? {
        return audioFile?.absolutePath
    }
}