package com.example.jetpacknavigation2_worldskills

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.activitynavigation.model.Options
import com.example.jetpacknavigation2_worldskills.Fragments.BoxSelectionFragment
import com.example.jetpacknavigation2_worldskills.Fragments.OptionsFragment
import com.example.jetpacknavigation2_worldskills.databinding.ActivityMainBinding
import com.example.jetpacknavigation2_worldskills.interfaces.*

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var currentFragment: Fragment? =null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUI()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() ||  super.onSupportNavigateUp()
    }

    private fun updateUI(){
        val fragment =  currentFragment

        if (fragment is HasCustomTitle){
            binding.toolbar.title = getString(fragment.getTitleRes())
        }
        else{
            binding.toolbar.title = getString(R.string.sone_title)
        }

        if (fragment is HasCustomAction){
            createCustomTollbarAction(fragment.getCustomAction())
        }
        else{
            binding.toolbar.menu.clear()
        }

        if (navController.currentDestination?.id == navController.graph.startDestination){
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }else{
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun createCustomTollbarAction(action: CustomAction){
        binding.toolbar.menu.clear()

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    private fun launchDestination(destionationId: Int, args: Bundle? =null){
        navController.navigate(destionationId, args)
    }

    override fun showBoxSelectionSreen(options: Options) {
        launchDestination(R.id.boxSelectionFragment, BoxSelectionFragment.createArgs(options))
    }

    override fun showOptionsScreen(options: Options) {
        launchDestination(R.id.optionsFragment, OptionsFragment.createArgs(options))
    }

    override fun showAboutScreen() {
        launchDestination(R.id.aboutFragment)
    }

    override fun showCongratulationsScreen() {
        launchDestination(R.id.boxFragment)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun goToMenu() {
        navController.popBackStack(R.id.menuFragment, false)
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner, {key, bundle ->
            listener(bundle.getParcelable(KEY_RESULT)!!)


        } )
    }

    companion object{
        private val KEY_RESULT = "RESULT"
    }
}