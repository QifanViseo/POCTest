package com.practice.qifan.testtexttospeech

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.android.synthetic.main.activity_edit_text.*
import java.util.*

class EditTextActivity : AppCompatActivity() , TextToSpeech.OnInitListener{

    private var mTts: TextToSpeech? = null
    private val REQ_TTS_STATUS_CHECK = 0
    private val TAG = "EditTextActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        val checkIntent = Intent()
        checkIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK)
        btn_input_speech.setOnClickListener {
            mTts?.speak(tie_text.text.toString(), TextToSpeech.QUEUE_ADD, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = mTts?.setLanguage(Locale.FRENCH)
            //设置发音语言
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            //判断语言是否可用
            {
                Log.v(TAG, "Language is not available")
                btn_input_speech.isEnabled = false
            } else {
                mTts?.speak("This is an example of speech synthesis.", TextToSpeech.QUEUE_ADD, null)
                btn_input_speech.isEnabled = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_TTS_STATUS_CHECK) {
            when (resultCode) {
                TextToSpeech.Engine.CHECK_VOICE_DATA_PASS ->
                    //这个返回结果表明TTS Engine可以用
                {
                    mTts = TextToSpeech(this, this)
                    Log.v(TAG, "TTS Engine is installed!")

                }
                TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL -> Log.v(TAG, "Got a failure. TTS apparently not available")
            //检查失败
                else -> Log.v(TAG, "Got a failure. TTS apparently not available")
            }
        } else {
            //其他Intent返回的结果
        }
    }


    override fun onPause() {
        super.onPause()
        if (mTts != null)
        //activity暂停时也停止TTS
        {
            mTts?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTts?.shutdown()
    }

}
