import androidx.databinding.library.baseAdapters.BR
import com.login.mvvm.login_test.R
import com.login.mvvm.login_test.databinding.FragmentMainBinding
import com.login.mvvm.login_test.screen.base.BaseFragment
import com.login.mvvm.login_test.screen.ui.fragment.main.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {
    companion object {
        val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

    override val viewModel: MainViewModel by viewModel()
    override val layoutResource: Int = R.layout.fragment_main
    override val viewModelVariable: Int = BR.viewModel

    override fun initComponent() {


    }
}