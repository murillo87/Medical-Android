package com.appamedix.makula.worker.speech

import android.content.Context
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

/**
 * A text to speech synthesizer. Uses the TextToSpeech class targeting API level 28.
 * @property context: The app's context.
 * @param countryCode: The ISO language code, e.g. "de" for german.
 * @param regionCode: The ISO region code, e.g. "DE" for germany.
 */
class SpeechSynthesizer(private val context: Context,
                        countryCode: String = "de",
                        regionCode: String = "DE") : TextToSpeech.OnInitListener {

    // The tag for logging.
    private val logTag = "SPEECH"

    // The speech synthesizer used under the hood.
    private val synthesizer: TextToSpeech = TextToSpeech(context, this)

    // The country code and region code combined to a locale for the speech synthesizer.
    private val locale: Locale = Locale(countryCode, regionCode)

    /**
     * The possible speech states.
     */
    private enum class SpeechState {
        // The synthesizer is not yet initialized.
        UNDEFINED,
        // No speech in progress, but ready to start.
        IDLE,
        // Speech in progress, data is being processed.
        SPEAKING,
        // Speech in progress, but cancelled to end with the next word in queue.
        CANCELLED
    }

    // The current state of the speech synthesizer.
    private var speechState: SpeechState = SpeechState.UNDEFINED

    /**
     * The delegate to inform about speaking states.
     * The given context should implement the SpeechSynthesizerListener and thus will be informed about state changes.
     */
    private val listener: SpeechSynthesizerListener? = context as? SpeechSynthesizerListener

    /**
     * Destroys the speech synthesizer by first stopping any speech in progress and then
     * shutting down the engine to free up the native resources.
     *
     * **Must be called when the scene gets destroyed.**
     */
    fun destroy() {
        synthesizer.stop()
        synthesizer.shutdown()
        speechState = SpeechState.UNDEFINED
    }

    /**
     * A data class holding the text to speak and an optional position, e.g. to point into a table.
     *
     * @param text: The text to speak out.
     * @param position: A position index this data belongs to in a table list.
     */
    data class SpeechData(val text: String, val position: Int?)

    // A list of data which contains the speech texts to speak.
    private var speechData: ArrayList<SpeechData> = arrayListOf()

    // A pointer to the current item in the speechData list which is currently speaking or next to speak.
    private var speechIndex: Int = 0

    /**
     * Applies the speech data which is a list the synthesizer can run through.
     * Stops speaking if currently in progress.
     */
    fun setSpeechData(data: ArrayList<SpeechData>) {
        this.stopSpeaking()
        this.speechData = data
    }

    /**
     * Returns true if the synthesizer is currently speaking a data item, otherwise false.
     */
    fun isSpeaking(): Boolean {
        return speechState == SpeechState.SPEAKING || speechState == SpeechState.CANCELLED
    }

    /**
     * Starts the speech mode of the synthesizer.
     * Does nothing if no speech data is provided or speaking is currently already in progress.
     *
     * This starts reading out the previously provided speech data from the beginning looping through the array.
     * Before calling this the speech data has to be provided via `setSpeechData(data:)`.
     */
    fun startSpeaking() {
        if (speechState == SpeechState.UNDEFINED) { return }
        if (speechState != SpeechState.IDLE) { return }
        if (speechData.isEmpty()) { return }

        speechState = SpeechState.SPEAKING
        speechIndex = 0
        speak(speechData[speechIndex].text)
    }

    /**
     * Stops the speech immediately, does nothing if speaking has not been started or has already finished.
     */
    fun stopSpeaking() {
        if (speechState == SpeechState.UNDEFINED) { return }
        if (speechState == SpeechState.IDLE) { return }

        synthesizer.stop()
    }

    /**
     * Speaks out a given text via Android's TextToSpeech synthesizer.
     *
     * @param text: The text to speak out.
     */
    private fun speak(text: String) {
        synthesizer.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UtteranceID") // Any ID string will do it
    }

    /* TextToSpeech.OnInitListener */

    override fun onInit(status: Int) {
        assert(speechState == SpeechState.UNDEFINED)

        if (status == TextToSpeech.SUCCESS) {
            // Initialization succeeded, now set up the synthesizer with the locale
            val setLanguageResult = synthesizer.setLanguage(locale)
            if (setLanguageResult == TextToSpeech.LANG_AVAILABLE ||
                    setLanguageResult == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    setLanguageResult == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE) {
                val setListenerResult = synthesizer.setOnUtteranceProgressListener(ttsProgressListener)
                if (setListenerResult == TextToSpeech.SUCCESS) {
                    speechState = SpeechState.IDLE
                } else {
                    Log.i(logTag, "Speech listener not set")
                }
            } else {
                Log.i(logTag, "Speech language not supported ($this.countryCode-$this.regionCode")
            }
        } else {
            Log.i(logTag, "Speech initialization failed")
        }
    }

    /* UtteranceProgressListener */

    /**
     * A listener to get notified about the progress of the internal TextToSpeech synthesizer.
     */
    private inner class ProgressListener: UtteranceProgressListener() {
        @Suppress("OverridingDeprecatedMember")
        override fun onError(utteranceId: String?) {
            Log.i(logTag, "TTS error: '$utteranceId'")

            val data = speechData[speechIndex]
            Handler(context.mainLooper).post {
                listener?.speechEnded(data)
                listener?.speechFinished()
            }

            speechState = SpeechState.IDLE
        }

        override fun onError(utteranceId: String, errorCode: Int) {
            Log.i(logTag, "TTS error '$errorCode': '$utteranceId'")

            val data = speechData[speechIndex]
            Handler(context.mainLooper).post {
                listener?.speechEnded(data)
                listener?.speechFinished()
            }

            speechState = SpeechState.IDLE
        }

        override fun onStart(utteranceId: String?) {
            // Inform delegate about speech start.
            val data = speechData[speechIndex]
            Handler(context.mainLooper).post {
                listener?.speechStarted(data)
            }
        }

        override fun onStop(utteranceId: String?, interrupted: Boolean) {
            // Inform delegate about speech end.
            val data = speechData[speechIndex]
            Handler(context.mainLooper).post {
                listener?.speechEnded(data)
                listener?.speechFinished()
            }

            // Clean up state.
            speechState = SpeechState.IDLE
        }

        override fun onDone(utteranceId: String?) {
            // Inform delegate about speech end of current item.
            val data = speechData[speechIndex]
            Handler(context.mainLooper).post {
                listener?.speechEnded(data)
            }

            // Continue with the next element in queue.
            speechIndex += 1
            when {
                // Speech cancelled, just clean up.
                speechState == SpeechState.CANCELLED -> speechState = SpeechState.IDLE
                // Next element exists, start speaking it.
                speechData.size > speechIndex -> speak(speechData[speechIndex].text)
                // Index out of bounds, speech has finished.
                else -> {
                    speechState = SpeechState.IDLE
                    Handler(context.mainLooper).post {
                        listener?.speechFinished()
                    }
                }
            }
        }
    }

    // The listener for getting notified about the text to speech progress.
    private val ttsProgressListener: ProgressListener = ProgressListener()
}