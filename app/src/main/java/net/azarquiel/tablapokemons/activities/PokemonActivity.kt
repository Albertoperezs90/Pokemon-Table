package net.azarquiel.tablapokemons.activities

import android.app.ActionBar
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import net.azarquiel.tablapokemons.R

import kotlinx.android.synthetic.main.activity_pokemon.*
import kotlinx.android.synthetic.main.content_pokemon.*
import net.azarquiel.tablapokemons.db.RealmDB
import net.azarquiel.tablapokemons.model.AbilityRealm
import net.azarquiel.tablapokemons.model.PokemonRealm
import net.azarquiel.tablapokemons.model.TypeRealm

class PokemonActivity : AppCompatActivity() {

    private lateinit var realm : RealmDB
    private lateinit var preferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var fav : Boolean = false
    private var changed : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)
        setSupportActionBar(toolbar)

        val id : Long= intent.getLongExtra("id", -1)

        realm = RealmDB(this)

        val pokemon = realm.getPokemonById(id)
        val types = realm.getTypesByPokemon(id)
        val abilities = realm.getAbilitiesByPokemon(id)

        preferences = getSharedPreferences("pokemon_favoritos", Context.MODE_PRIVATE)
        editor = preferences.edit()

        checkIfFav(id)

        ivPokemon.setImageResource(resources.getIdentifier("p${pokemon.pokemon_id}","drawable",packageName))
        tvNamePokemon.text = pokemon.name
        for (t in types){
            linearLayoutType.addView(createTextViewType(t))
        }

        for (a in abilities){
            linearLayoutAbilities.addView(createTextViewAbilite(a))
        }

        fabFav.setOnClickListener { addFavorites(pokemon) }
    }

    private fun checkIfFav(id: Long) {
        val pokemons = preferences.all

        for (p in pokemons.entries){
            if (p.key.toString().toLong() == id){
                fabFav.setImageResource(android.R.drawable.btn_star_big_on)
                fav = true
            }
        }
    }

    private fun createTextViewAbilite(a: AbilityRealm): TextView {
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        textView.text = a.name
        textView.textSize = 15F
        params.gravity = Gravity.CENTER
        params.setMargins(20,0,0,0)
        textView.layoutParams = params
        return textView
    }

    private fun createTextViewType(t: TypeRealm): TextView {
        val textView = TextView(this)
        val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
        textView.setBackgroundResource(android.R.color.black)
        textView.text = t.name
        textView.textSize = 15F
        textView.setPadding(10,10,10,10)
        textView.setTextColor(resources.getColor(android.R.color.white))
        params.gravity = Gravity.CENTER
        params.setMargins(20,0,0,0)
        textView.layoutParams = params
        return textView
    }

    override fun onBackPressed() {
        intent.putExtra("FavActualizado",1)
        if (changed){
            setResult(1,intent)
        }else {
            setResult(2,intent)
        }
        finish()
    }

    private fun addFavorites(pokemon: PokemonRealm) {
        if (fav){
            editor.remove("${pokemon.pokemon_id}")
            editor.commit()
            Toast.makeText(this, "Pokemon eliminado de favoritos", Toast.LENGTH_LONG).show()
            fabFav.setImageResource(android.R.drawable.btn_star_big_off)
            fav = false
        }else {
            editor.putString("${pokemon.pokemon_id}", pokemon.name)
            editor.commit()
            Toast.makeText(this, "Pokemon añadido a favoritos", Toast.LENGTH_LONG).show()
            fabFav.setImageResource(android.R.drawable.btn_star_big_on)
            fav = true
        }

        changed = true
        intent.putExtra("FavActualizado",1)
        setResult(1,intent)
    }

}
