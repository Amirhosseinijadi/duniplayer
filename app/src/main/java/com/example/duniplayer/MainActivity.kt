package com.example.duniplayer

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.duniplayer.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer
    var isplaying = true
    var isuserchanging = false
    var ismute = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preppermusic()

        binding.btnPlayPause.setOnClickListener {

            configuremusic()

        }
        binding.btnGoBefore.setOnClickListener { gobeforemusic() }

        binding.btnGoAfter.setOnClickListener { goaftermusic() }

        binding.btnVolumeOnOff.setOnClickListener { configurevolumm() }

        binding.sliderMain.addOnChangeListener { slider, value, fromUser ->

            binding.txtLeft.text = secondTostring(value.toLong())
            isuserchanging = fromUser

        }

        binding.sliderMain.addOnSliderTouchListener(object:Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {

                mediaPlayer.seekTo(slider.value.toInt())

            }

        })

    }

    private fun configurevolumm() {

        val audiomanager = getSystemService(AUDIO_SERVICE) as AudioManager
        if(ismute){

            audiomanager.adjustVolume(AudioManager.ADJUST_UNMUTE,AudioManager.FLAG_SHOW_UI)
            binding.btnVolumeOnOff.setImageResource(R.drawable.ic_volume_on)
            ismute = false


        }else{

            audiomanager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_SHOW_UI)
            binding.btnVolumeOnOff.setImageResource(R.drawable.ic_volume_off)
            ismute = true

        }

    }

    private fun goaftermusic() {

        val newvalue2 = mediaPlayer.currentPosition + 15000
        mediaPlayer.seekTo(newvalue2)

    }

    private fun gobeforemusic() {

        val now = mediaPlayer.currentPosition

        val newvalue = now - 15000
        mediaPlayer.seekTo(newvalue)

    }

    private fun configuremusic() {

        if(isplaying){
            mediaPlayer.pause()
            binding.btnPlayPause.setImageResource(R.drawable.ic_play)
            isplaying = false

        }else{

            mediaPlayer.start()
            binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
            isplaying = true

        }

    }

    private fun preppermusic() {

        mediaPlayer = MediaPlayer.create(this,R.raw.music_file)
        mediaPlayer.start()
        isplaying = true

        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)

        binding.sliderMain.valueTo = mediaPlayer.duration.toFloat()

        binding.txtRight.text = secondTostring(mediaPlayer.duration.toLong())
        val timer = Timer()
        timer.schedule(object:TimerTask(){
            override fun run() {
                runOnUiThread {

                    if(!isuserchanging){

                        binding.sliderMain.value = mediaPlayer.currentPosition.toFloat()

                    }

                }
            }
        },1000,1000)

    }

    private fun secondTostring(duration:Long):String{

        val second = duration/1000 % 60
        val minutes = duration / (1000 * 60) % 60

        return java.lang.String.format(Locale.US,"%02d:%02d",minutes,second)

    }
}