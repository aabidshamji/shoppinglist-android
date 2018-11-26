package hu.aut.android.todorecyclerviewdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim = AnimationUtils.loadAnimation(this, R.anim.push_anim)

        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                val intentMain = Intent()
                intentMain.setClass(this@SplashActivity,
                        ScrollingActivity::class.java)
                startActivity(intentMain)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        imageView.startAnimation(anim)

    }
}
