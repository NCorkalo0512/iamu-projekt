package hr.algebra.imdbmovies

import android.content.Intent
import android.graphics.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import hr.algebra.imdbmovies.databinding.ActivitySplashScreenBinding
import hr.algebra.imdbmovies.framework.*



private const val DELAY=3000L
const val DATA_IMPORTED="hr.algebra.imdbmovies.data_imported"
class SplashScreenActivity : AppCompatActivity() {
    private val CHANNEL_ID = "AlarmChannel"
    private val NOTIFICATION_ID = 123
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        startAnimations()

        redirect()
    }


   private fun redirect() {
      if(getBooleanPreference(DATA_IMPORTED)){
          callDelayed(DELAY){startActivity<HostActivity>()}
      }else{
          if(isOnline()){
              MoviesService.enqueue(this)
          }else{
              binding.tvSplash.text=getString(R.string.no_internet)
          }
      }

    }

    private fun startAnimations() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.ivSplash.startAnimation(animation)
    }
}


