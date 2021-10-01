package hu.petrik.akasztofa

import hu.petrik.akasztofa.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess
import kotlin.random.Random
import android.widget.Toast
import java.io.IOException
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    lateinit var list: List<String>

    private val betuk: String = "aábcdeéfghiíjklmnoóöőpqrstuúüűvwxyz".uppercase()
    private var tippelhetoBetuk = ""
    private var neelenorizz = false
    private var tippeltBetuk = ""
    private var aktualisSzo = ""
    private var index = 0
    private var allas = 0

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
        neelenorizz = true
        index = 0
        allas = 0
        aktualisSzo = ""
        tippelhetoBetuk = betuk
        tippeltBetuk = ""
        betuallit()
        try {
            list = assets.open("szavak.txt").bufferedReader(Charsets.UTF_8).readLines()
            aktualisSzo = szoRandom().uppercase()
            bind.textViewSzo.text = getAktualisSzoUres()
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        bind.imageViewKep.setImageResource(R.drawable.akasztofa00)
        neelenorizz = false
    }

    fun vege(gyozelem: Boolean) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Nem sikerült kitalálni!")
        alert.setMessage("Szeretnél még egyet játszani?")
        if (gyozelem) {
            alert.setTitle("Helyes megfejtés!")
            alert.setMessage("Szeretnél még egyet játszani?")
        }
        alert.setPositiveButton("igen") { _, _ ->
            init()
        }
        alert.setNegativeButton("nem") { _, _ ->
            exitProcess(0)
        }
        alert.setOnDismissListener() {
            init()
        }
        alert.show()
    }

    private fun getAktualisSzoHelyzet(): String {
        var kiad = "_"
        if (tippeltBetuk.indexOf(aktualisSzo[0]) != -1) {
            kiad = aktualisSzo[0].toString()
        }
        for (i in 1..aktualisSzo.length - 1) {
            if (tippeltBetuk.indexOf(aktualisSzo[i]) != -1) {
                kiad += " " + aktualisSzo[i]
            } else {
                kiad += " _"
            }
        }
        return kiad
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
            13 -> vege(false)
        }
        bind.imageViewKep.setImageResource(melyik)
    }

    private fun szoRandom(): String {
        return list.get(Random.nextInt(list.size - 1))
    }

    private fun betuNovel() {
        index++
        if (index == tippelhetoBetuk.length) {
            index = 0
        }
        betuallit()
    }

    private fun betuCsokkent() {
        index--
        if (index < 0) {
            index = tippelhetoBetuk.length - 1
        }
        betuallit()
    }

    private fun betuallit() {
        bind.textViewBetu.text = tippelhetoBetuk[index].toString()
    }

    fun getBetu(): Char {
        return tippelhetoBetuk[index];
    }

    fun betuElvesz(betu: Char) {
        if (neelenorizz) {
            return
        }
        tippelhetoBetuk = tippelhetoBetuk.replace(betu.toString(), "")
        betuCsokkent()
        betuNovel()
        tippeltBetuk += betu
    }

    fun betuKeres(betu: Char): Boolean {
        return aktualisSzo.indexOf(betu) != -1
    }

    fun kitalalva(): Boolean {
        var jo = true
        for (i in 0..aktualisSzo.length - 1) {
            if (tippeltBetuk.indexOf(aktualisSzo[i]) == -1) {
                jo = false
            }
        }
        return jo
    }

    private fun tippel() {
        if (betuKeres(getBetu())) {
            Toast.makeText(this, "'${getBetu()}' volt benne", Toast.LENGTH_SHORT).show()
            betuElvesz(getBetu())
            if (kitalalva()) {
                bind.textViewSzo.text = getAktualisSzoHelyzet()
                vege(true)
            } else {
                bind.textViewSzo.text = getAktualisSzoHelyzet()
            }
        } else {
            allas++
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