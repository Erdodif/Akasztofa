package hu.petrik.akasztofa

import hu.petrik.akasztofa.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlin.random.Random
import android.widget.Toast
import java.io.IOException
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var list: List<String>
    private var index = 0
    private val betuk: CharArray = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz".toCharArray()
    private var allas = 0


    private fun szoUres(szo: String): String {
        val hatar = szo.length - 2
        var kiad = ""
        for (i in 0..hatar) {
            kiad += "_" + " "
        }
        kiad += "_"
        return kiad
    }

    private fun init() {
        try {
            list = assets.open("szavak.txt").bufferedReader(Charsets.UTF_8).readLines()
            bind.textViewSzo.text = szoUres(szoRandom())
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.d("Hiba", e.message.toString())
        }
    }

    private fun allasKovetes() {
        var melyik = R.drawable.akasztofa13
        when (allas) {
            0 -> melyik = R.drawable.akasztofa00
            1 -> melyik = R.drawable.akasztofa01
            2 -> melyik = R.drawable.akasztofa02
            3 -> melyik = R.drawable.akasztofa03
            4 -> melyik = R.drawable.akasztofa04
            5 -> melyik = R.drawable.akasztofa05
            6 -> melyik = R.drawable.akasztofa06
            7 -> melyik = R.drawable.akasztofa07
            8 -> melyik = R.drawable.akasztofa08
            9 -> melyik = R.drawable.akasztofa09
            10 -> melyik = R.drawable.akasztofa10
            11 -> melyik = R.drawable.akasztofa11
            12 -> melyik = R.drawable.akasztofa12
        }
        bind.imageViewKep.setImageResource(melyik)
    }


    private fun szoRandom(): String {
        return list.get(Random.nextInt(list.size - 1))
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