package co.untitledkingdom.spacexmvi

import co.untitledkingdom.spacexmvi.models.Rocket
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class MainViewModgelTest {

    private val mainInteractor: MainInteractor = mock()

    init {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testListFetchingSuccess() {
        val fetchedRocketList = listOf(Rocket("testRocketName", "url/to/pic"))
        whenever(mainInteractor.fetchRocketList()).thenReturn(
                Observable.just(PartialMainViewState.ListFetchedState(fetchedRocketList))
        )

        val mainViewModel = MainMviPresenter(mainInteractor)
        val mainViewRobot = MainViewRobot(mainViewModel)

        mainViewRobot.startView()
        mainViewRobot.emitButtonClick()
        mainViewRobot.stopView()
        mainViewRobot.startView()

        mainViewRobot.assertViewStates(
                MainViewState(),
                MainViewState(progress = true),
                MainViewState(rocketList = fetchedRocketList),
                MainViewState(rocketList = fetchedRocketList)
        )
    }

    @Test
    fun testListFetchingError() {
        whenever(mainInteractor.fetchRocketList()).thenReturn(
                Observable.just(PartialMainViewState.ErrorState)
        )

        val mainViewModel = MainMviPresenter(mainInteractor)
        val mainViewRobot = MainViewRobot(mainViewModel)

        mainViewRobot.startView()
        mainViewRobot.emitButtonClick()
        mainViewRobot.stopView()
        mainViewRobot.startView()

        mainViewRobot.assertViewStates(
                MainViewState(),
                MainViewState(progress = true),
                MainViewState(error = true),
                MainViewState(error = true)
        )
    }

    @Test
    fun `fetch rockets list and clear after that`() {

        /** mock necessary classes */
        val fetchedRocketList = listOf(Rocket("testRocketName", "url/to/pic"))
        whenever(mainInteractor.fetchRocketList()).thenReturn(
                Observable.just(PartialMainViewState.ListFetchedState(fetchedRocketList))
        )

        /** initialize fromView model */
        val mainViewModel = MainMviPresenter(mainInteractor)

        /** initialize fromView robot using fromView model just created */
        val mainViewRobot = MainViewRobot(mainViewModel)

        /** attach the fromView and run bind() method */
        mainViewRobot.startView()

        /** classic action emitters */
        mainViewRobot.emitButtonClick()
        mainViewRobot.clearButtonClick()

        /** simulate lifecycle change to check if state will be restored correctly */
        mainViewRobot.stopView()
        mainViewRobot.startView()

        /** check if fromView states has been rendered properly */
        mainViewRobot.assertViewStates(
                MainViewState(),
                MainViewState(progress = true),
                MainViewState(rocketList = fetchedRocketList),
                MainViewState(),
                MainViewState() /** check the same state for the second time due to lifecycle changes */
        )
    }
}