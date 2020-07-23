package com.appamedix.makula.worker.speech

/**
 * The listener who wants to be informed about the progress of the speech synthesizer.
 */
interface SpeechSynthesizerListener {
    /**
     * The speech for an element has been started.
     *
     * @param data: The data element currently in speech.
     */
    fun speechStarted(data: SpeechSynthesizer.SpeechData)

    /**
     * The speech for an element has been ended.
     *
     * @param data: The data element just finished.
     */
    fun speechEnded(data: SpeechSynthesizer.SpeechData)

    /**
     * The speech's queue has been ended, no more speech to apply.
     */
    fun speechFinished()
}