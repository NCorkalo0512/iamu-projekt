package hr.algebra.imdbmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hr.algebra.imdbmovies.adapter.ItemAdapter
import hr.algebra.imdbmovies.databinding.ActivityHostBinding
import hr.algebra.imdbmovies.databinding.ActivitySplashScreenBinding


class HostActivity : AppCompatActivity() {
    private val CHANNEL_ID = "AlarmChannel"
    private val NOTIFICATION_ID = 123
    private lateinit var binding: ActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        binding= ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHamburgerMenu()
        initNavigation()

        showNotification()
    }

    private fun initNavigation() {
        val navController= Navigation.findNavController(this,R.id.navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.host_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                toggleDrawer()
                return true
            }
            R.id.menuExit ->{
                exitApp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.exit)
            setMessage("Really exit app?")
            setIcon(R.drawable.exit)
            setCancelable(true)
            setPositiveButton("Ok"){_,_-> finish()}
            setNegativeButton("Cancel", null)
            show()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawers()
        }else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Notifikacija")
            .setContentText("Notifikacija ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}