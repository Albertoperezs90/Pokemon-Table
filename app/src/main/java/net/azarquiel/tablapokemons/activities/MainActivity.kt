package net.azarquiel.tablapokemons.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.tablapokemons.R
import net.azarquiel.tablapokemons.adapter.RecyclerAdapter
import net.azarquiel.tablapokemons.db.RealmDB

class MainActivity : AppCompatActivity() {

    private lateinit var realm : RealmDB
    private lateinit var adapter : RecyclerAdapter
    private var fav : Boolean = false
    private lateinit var preferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        realm = RealmDB(this@MainActivity)
        val list = realm.getAll()
        adapter = RecyclerAdapter(this, R.layout.recycler_row,list,realm)
        rvRow.layoutManager = LinearLayoutManager(this)
        rvRow.adapter = adapter
        preferences = getSharedPreferences("pokemon_favoritos", Context.MODE_PRIVATE)
        editor = preferences.edit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.favPokemon -> favoritesPokemon(item)
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun favoritesPokemon(item: MenuItem): Boolean {

        if (fav){
            item.icon = AppCompatResources.getDrawable(this,android.R.drawable.star_big_off)
            fav = false
        }else {
            item.icon = AppCompatResources.getDrawable(this, android.R.drawable.star_big_on)
            fav = true
        }

        updateRecyclerView()

        return true
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (fav && resultCode == 1){
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        if (fav){
            val idPokemons = mutableListOf<Long>()
            for (p in preferences.all.entries){
                val id = p.key.toString().toLong()
                idPokemons.add(id)
            }

            val favPokemons = realm.getFavPokemons(idPokemons.toList())

            adapter = RecyclerAdapter(this,R.layout.recycler_row,favPokemons,realm)
            rvRow.layoutManager = LinearLayoutManager(this)
            rvRow.adapter = adapter
        }else {
            adapter = RecyclerAdapter(this,R.layout.recycler_row,realm.getAll(),realm)
            rvRow.layoutManager = LinearLayoutManager(this)
            rvRow.adapter = adapter
        }

    }


}
