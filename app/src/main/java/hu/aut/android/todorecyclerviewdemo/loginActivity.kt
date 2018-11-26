package hu.aut.android.todorecyclerviewdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            when {
                password.text.isEmpty() -> password.error = getString(R.string.enterPassword)
                password.text.toString() == getString(R.string.correctPassword) -> {
                    var intentMain = Intent()
                    intentMain.setClass(this, SplashActivity::class.java)
                    startActivity(intentMain)
                }
                else -> password.error = getString(R.string.incorrectPassword)
            }
        }

    }
}
