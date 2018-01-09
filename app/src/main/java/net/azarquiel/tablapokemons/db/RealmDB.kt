package net.azarquiel.tablapokemons.db

import android.content.Context
import io.realm.Realm
import net.azarquiel.tablapokemons.model.*

/**
 * Created by alberto on 04/12/2017.
 */
class RealmDB (val context: Context) {

    private lateinit var realm : Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    fun getAll() : List<PokemonRealm> {
        val pokemon = realm.where(PokemonRealm::class.java).findAll()
        return pokemon.toList()
    }

    fun getPokemonById (id : Long) : PokemonRealm{
        val pokemon = realm.where(PokemonRealm::class.java).equalTo("pokemon_id",id).findFirst()
        return pokemon
    }

    fun getAbilitiesByPokemon (pokemonId : Long) : List<AbilityRealm>{
        val idAbilities = realm.where(PokemonAbilitiesRealm::class.java).equalTo("pokemon_id",pokemonId).findAll()
        val listAbilities : MutableList<AbilityRealm> = mutableListOf()
        for (a in idAbilities){
            val ability = realm.where(AbilityRealm::class.java).equalTo("ability_id",a.ability_id).findFirst()
            listAbilities.add(ability)
        }
        return listAbilities.toList()
    }

    fun getTypesByPokemon (pokemonId : Long) : List<TypeRealm> {
        val type = realm.where(PokemonTypesRealm::class.java).equalTo("pokemon_id", pokemonId).findAll()
        val listTypes : MutableList<TypeRealm> = mutableListOf()
        for (t in type){
            val type = t.type_id
            val types = realm.where(TypeRealm::class.java).equalTo("type_id",type).findFirst()
            listTypes.add(types)
        }
        return listTypes.toList()
    }

    fun getFavPokemons (pokemonsId : List<Long>) : List<PokemonRealm>{
        val favPokemons = mutableListOf<PokemonRealm>()

        for (id in pokemonsId){
            val pokemon = realm.where(PokemonRealm::class.java).equalTo("pokemon_id",id).findFirst()
            favPokemons.add(pokemon)
        }

        return favPokemons.toList()
    }
}