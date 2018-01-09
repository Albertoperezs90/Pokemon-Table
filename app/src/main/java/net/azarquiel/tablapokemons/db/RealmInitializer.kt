package net.azarquiel.recetasrealm.RealmInitializer

import android.app.Application
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by alberto on 26/11/2017.
 */
class RealmInitializer : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        //REALM CONFIGURATION
        val config = RealmConfiguration.Builder()
                .assetFile("pokemon.realm")
                .name("pokemon.realm")
                .schemaVersion(1)
                .build()

        Realm.setDefaultConfiguration(config)

        //STETHO INITIALIZER
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        )
    }
}