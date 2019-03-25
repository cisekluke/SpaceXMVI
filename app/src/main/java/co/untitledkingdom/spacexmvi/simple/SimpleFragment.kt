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
import kotlinx.android.synthetic.main.fragment_simple.button
import kotlinx.android.synthetic.main.fragment_simple.input
import kotlinx.android.synthetic.main.fragment_simple.text

class SimpleFragment : BaseMviFragment<SimpleViewState, SimpleView, SimplePresenter>(), SimpleView {
    override fun getPresenter(): SimplePresenter = SimplePresenter()

    override fun view(): SimpleView = this

    private val buttonSubject = PublishSubject.create<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            buttonSubject.onNext(input.text.toString())
        }
    }

    override fun render(viewState: SimpleViewState) {
        with(viewState) {
            text.text = output
        }
    }

    override fun emitUpdateButtonClick(): Observable<String> = buttonSubject
}