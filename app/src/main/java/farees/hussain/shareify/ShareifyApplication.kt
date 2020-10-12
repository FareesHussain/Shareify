package farees.hussain.shareify

import android.app.Application
import timber.log.Timber

class ShareifyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}