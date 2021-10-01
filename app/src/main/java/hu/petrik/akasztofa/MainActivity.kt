package hu.petrik.akasztofa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.util.Observable
import androidx.databinding.DataBindingUtil
import hu.petrik.akasztofa.databinding.ActivityMainBinding
import java.io.File
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var list: List<String>

    private var index = 0
    private val betuk: CharArray = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz".toCharArray()

    private fun init() {
        list = File("szavak.txt").inputStream().readBytes()
                .toString(Charsets.UTF_16).split("\n")
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