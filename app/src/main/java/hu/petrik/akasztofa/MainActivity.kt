package hu.petrik.akasztofa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.util.Observable
import androidx.databinding.DataBindingUtil
import hu.petrik.akasztofa.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var list: List<String>

    private var index = 0
    private val betuk: CharArray = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz".toCharArray()

    private fun init() {
        try{
            list = File("assets/szavak.txt").inputStream().readBytes()
                .toString(Charsets.UTF_16).split("\n")
            bind.textViewSzo.text = szoRandom()
        }catch (e : IOException){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.d("Hiba",e.message.toString())
        }
    }

    private fun szoRandom():String{
        return list.get(Random.nextInt(list.size-1))
    }

    private fun betuNovel() {
        index++
        if (index >= betuk.size) {
            index = 0
        }
        betuallit()
    }

    private fun betuCsokkent() {
        index--
        if (index < 0) {
            index = betuk.size - 1
        }
        betuallit()
    }

    private fun betuallit() {
        bind.textViewBetu.text = betuk[index].toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        bind.buttonMinusz.setOnClickListener {
            betuCsokkent()
        }
        bind.buttonPlusz.setOnClickListener {
            betuNovel()
        }
        bind.textViewBetu.setOnLongClickListener {
            index = 0
            betuallit()
            true
        }
    }
}