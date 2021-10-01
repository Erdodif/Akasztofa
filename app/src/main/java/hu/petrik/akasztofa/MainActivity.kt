package hu.petrik.akasztofa

import hu.petrik.akasztofa.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlin.random.Random
import android.widget.Toast
import java.io.IOException
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var list: List<String>
    private var index = 0
    private val betuk: String = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz".uppercase()
    private var allas = 0
    private var aktualisSzo = ""
    private var tippeltBetuk = ""
    private var tippelhetoBetuk:CharArray = betuk.toCharArray()

    private fun getAktualisSzoUres(): String {
        val hatar = aktualisSzo.length - 2
        var kiad = ""
        for (i in 0..hatar) {
            kiad += "_" + " "
        }
        kiad += "_"
        return kiad
    }

    private fun init() {
        index = 0
        allas = 0
        aktualisSzo = ""
        tippeltBetuk = ""
        tippelhetoBetuk = betuk.toCharArray()
        try {
            list = assets.open("szavak.txt").bufferedReader(Charsets.UTF_8).readLines()
            aktualisSzo = szoRandom().uppercase()
            bind.textViewSzo.text = getAktualisSzoUres()
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
            13 -> {
                var alert = AlertDialog.Builder(this)
                alert.setTitle("Vereség")
                alert.setMessage("Veszített, szeretné újból megpróbálni?")
                alert.setPositiveButton("igen"){_,_->
                    init()
                }
                alert.setNegativeButton("nem"){_,_->
                    exitProcess(0)
                }
            }
        }
        bind.imageViewKep.setImageResource(melyik)
    }

    private fun szoRandom(): String {
        return list.get(Random.nextInt(list.size - 1))
    }

    private fun betuNovel() {
        index++
        if (index == tippelhetoBetuk.size) {
            index = 0
        }
        betuallit()
    }

    private fun betuCsokkent() {
        index--
        if (index < 0) {
            index = tippelhetoBetuk.size - 1
        }
        betuallit()
    }

    private fun betuallit() {
        bind.textViewBetu.text = tippeltBetuk.get(index).toString()
    }

    fun getBetu(): Char {
        return tippelhetoBetuk[index];
    }

    fun betuKeres(betu: Char): Boolean {
        var talalat = false
        var i = 0
        while (i < aktualisSzo.length && aktualisSzo.get(i) == betu) {
            i++
        }
        if (i < aktualisSzo.length) {
            talalat = true
        }
        return talalat
    }

    fun betuElvesz(betu: Char) {
        tippelhetoBetuk = tippelhetoBetuk.toString()
            .replace(betu.toString(),"").toCharArray()
    }

    private fun tippel() {
        if(betuKeres(getBetu())){
            Toast.makeText(this, "'${getBetu()}' volt benne", Toast.LENGTH_SHORT).show()
            betuElvesz(getBetu())
        }
        else{
            allas--
            allasKovetes()
            Toast.makeText(this, "'${getBetu()}' nem volt benne", Toast.LENGTH_SHORT).show()
            betuElvesz(getBetu())
        }
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
        bind.buttonTippel.setOnClickListener() {
            tippel()
        }
    }
}