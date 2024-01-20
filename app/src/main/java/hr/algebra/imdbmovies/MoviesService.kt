package hr.algebra.imdbmovies

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import hr.algebra.imdbmovies.api.MovieFetcher
import hr.algebra.imdbmovies.framework.sendBroadcast

private const val JOB_ID=1
@Suppress("DEPRECATION")
class MoviesService : JobIntentService() {
    override fun onHandleWork(intent: Intent) {
     MovieFetcher(this).fetchItems(100)


    }
    companion object{
        fun enqueue(context: Context){
            enqueueWork(context, MoviesService::class.java, JOB_ID,
                Intent(context, MoviesService::class.java)
            )
        }
    }
}