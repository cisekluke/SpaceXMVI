package co.untitledkingdom.spacexmvi.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.untitledkingdom.spacexmvi.R
import co.untitledkingdom.spacexmvi.base.BaseMviFragment
import co.untitledkingdom.spacexmvi.main.MainActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.simple_fragment.button
import kotlinx.android.synthetic.main.simple_fragment.input

class SimpleFragment : BaseMviFragment<MainActivity, SimpleView, SimpleViewModel>(
    SimpleViewModel::class.java,
    MainActivity()
), SimpleView {

    private val buttonSubject = PublishSubject.create<SimpleIntent>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.simple_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            buttonSubject.onNext(SimpleIntent.ShowOutputStage(input.text.toString()))
        }
    }

    override fun render(viewState: SimpleViewState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emitIntent(): Observable<SimpleIntent> = buttonSubject

    override fun view(): SimpleView = this
}
