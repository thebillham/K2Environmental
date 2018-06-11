//package nz.co.k2.k2e.ui.splash;
//
//import java.util.logging.Handler;
//
//import io.reactivex.Completable;
//import io.reactivex.CompletableObserver;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Action;
//import nz.co.k2.k2e.data.DataManager;
//import nz.co.k2.k2e.ui.base.BaseViewModel;
//import nz.co.k2.k2e.utils.rx.SchedulerProvider;
//
//public class SplashViewModel extends BaseViewModel<SplashNavigator> {
//
//    public SplashViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
//        super(dataManager, schedulerProvider);
//    }
//
//    // If logged in, start downloading MyJobs, not WFM jobs yet
//    public void startSeeding() {
//        // MOCK Function Below
//        // TODO Replace later
//        Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Exception {
//                wait(3000);
//            }
//        }).subscribeOn(getSchedulerProvider().io())
//        .observeOn(getSchedulerProvider().ui())
//        .subscribe(new CompletableObserver() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                getNavigator().openLoginActivity();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
////    // if logged in, go straight to NavDrawer, otherwise go to GoogleLogin activity
////    private void decideNextActivity() {
////        if (getDataManager().getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
////            getNavigator().openLoginActivity();
////        } else {
////            getNavigator().openMainActivity();
////        }
////    }
//}
