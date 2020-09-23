package jp.ac.gifu_nct.miekaji

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.os.Debug
import android.util.Log
import android.view.KeyEvent
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.auth.LoginActivity
import jp.ac.gifu_nct.miekaji.auth.SignupActivity
import jp.ac.gifu_nct.miekaji.ui.flower.FlowerFragment
import jp.ac.gifu_nct.miekaji.utils.AuthUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if(AuthUtil.token == null) {
            if(AuthUtil.isDebug) {
                Thread() {
                    AuthUtil.fetchToken(this)
                    if(AuthUtil.token == null) {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                    }
                }.start()
            } else {
                val sp = getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE)
                val id = sp.getString("miekaji-id", null)
                val pass = sp.getString("miekaji-pass", null)
                if(id == null || pass == null) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                } else {
                    Thread() {
                        AuthUtil.fetchToken(this)
                        if(AuthUtil.token == null) {
                            val editor = sp.edit()
                            editor.remove("miekaji-id")
                            editor.remove("miekaji-pass")
                            editor.commit()

                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }.start()
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_housework,
                R.id.navigation_flower,
                R.id.navigation_friend
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}