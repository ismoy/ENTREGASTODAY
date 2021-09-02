package cl.tofcompany.entregastoday.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio
import cl.tofcompany.entregastoday.Controladores.MainActivity
import cl.tofcompany.entregastoday.R
import com.google.firebase.auth.FirebaseAuth

class splash_screen : AppCompatActivity() {
    private val SPLASH_TIME:Long = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val user = FirebaseAuth.getInstance().currentUser
        if (user !=null){
            startActivity(Intent(this,ServicioEnvio::class.java))
        }else {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_TIME)
        }
    }
}