package net.azarquiel.tablapokemons.adapter

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.recycler_row.view.*
import net.azarquiel.tablapokemons.R
import net.azarquiel.tablapokemons.activities.MainActivity
import net.azarquiel.tablapokemons.activities.PokemonActivity
import net.azarquiel.tablapokemons.db.RealmDB
import net.azarquiel.tablapokemons.model.PokemonRealm
import net.azarquiel.tablapokemons.model.TypeRealm

/**
 * Created by alberto on 04/12/2017.
 */
class RecyclerAdapter (val context: Context,
                       val layout : Int,
                       val dataList : List<PokemonRealm>,
                       val realm : RealmDB) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context) //Quiza de fallos, solucion (parent.context)
        val inflater = layoutInflater.inflate(layout, parent, false)
        return ViewHolder(inflater, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }




    inner class ViewHolder(viewLayout : View, context: Context) : RecyclerView.ViewHolder(viewLayout) {
        fun bind(dataItem : PokemonRealm){
            itemView.ivPokemonRow.setImageResource(context.resources.getIdentifier("p${dataItem.pokemon_id}","drawable",context.packageName))
            itemView.tvNamePokemonRow.text = dataItem.name
            val types = realm.getTypesByPokemon(dataItem.pokemon_id)
            itemView.layoutTypes.removeAllViews()
            for (t in types){
                val textView = TextView(context)
                val params = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
                textView.setBackgroundResource(android.R.color.black)
                textView.text = t.name
                textView.textSize = 15F
                textView.setPadding(10,10,10,10)
                textView.setTextColor(context.resources.getColor(android.R.color.white))
                params.gravity = Gravity.RIGHT
                params.setMargins(20,0,0,10)
                textView.layoutParams = params
                itemView.layoutTypes.addView(textView)
            }

            itemView.setOnClickListener( {intentPokemon(dataItem)})
        }

        private fun intentPokemon(dataItem: PokemonRealm) {
            val intent = Intent(context, PokemonActivity::class.java)
            intent.putExtra("id", dataItem.pokemon_id)
            (context as MainActivity).startActivityForResult(intent,1)
        }
    }
}