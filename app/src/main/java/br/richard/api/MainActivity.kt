package br.richard.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import java.net.URL
import android.util.Log
import android.widget.*
import coil.api.load
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    val BASE_URL = "https://store.steampowered.com/api/appdetails?appids="
    var API_URL = "https://store.steampowered.com/api/appdetails?appids="
    var COMPRA_URL = "https://store.steampowered.com/app/"
    private var TAG = "Richard";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //57690
        val listaJogos = findViewById<ListView>(R.id.listaJogos)
        var jogosAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1)
        jogosAdapter.add("1151340")
        jogosAdapter.add("22380")
        jogosAdapter.add("377160")
        jogosAdapter.add("1850570")
        listaJogos.adapter = jogosAdapter
        listaJogos.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position,id ->
            val item = parent.getItemAtPosition(position)
            val saidaTituloJogo = findViewById<TextView>(R.id.tituloJogo)
            saidaTituloJogo.text = item.toString()
            buscaCotacao(item.toString())
        }

    }

    fun buscaCotacao(codigo : String){
        GlobalScope.async{
            API_URL = BASE_URL
            API_URL += codigo
            val resposta = URL(API_URL).readText()

            var tituloJogo = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getString("name")
            var precoFinalJogo = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getJSONObject("price_overview").getString("final_formatted")
            var precoInicioJogo = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getJSONObject("price_overview").getString("initial_formatted")
            var moeda = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getJSONObject("price_overview").getString("currency")
            var desconto = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getJSONObject("price_overview").getInt("discount_percent")
            var saidaImagem = JSONObject(resposta).getJSONObject(codigo).getJSONObject("data").getString("header_image")

            Log.d("TESTE",saidaImagem.toString())

            val saidaTituloJogo = findViewById<TextView>(R.id.tituloJogo)
            val saidaImgJogo = findViewById<ImageView>(R.id.imgJogo)
            saidaImgJogo.load(saidaImagem.toString())
            val saidaPrecoInicialJogo = findViewById<TextView>(R.id.precoInicial)
            val saidaPrecoFinalJogo = findViewById<TextView>(R.id.precoFinal)
            val saidaDescontoJogo = findViewById<TextView>(R.id.desconto)
            val saidaComprarJogo = findViewById<TextView>(R.id.comprarJogo)
            saidaTituloJogo.setText(tituloJogo)
            saidaPrecoInicialJogo.setText(precoInicioJogo)
            saidaPrecoFinalJogo.setText(precoFinalJogo)
            saidaDescontoJogo.setText("${desconto.toString()}%")
            saidaComprarJogo.setText("${COMPRA_URL}${codigo}")


        }


    }


}