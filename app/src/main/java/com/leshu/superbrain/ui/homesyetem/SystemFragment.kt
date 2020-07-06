package com.leshu.superbrain.ui.homesyetem

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.coder.zzq.smartshow.toast.SmartToast
import com.leshu.superbrain.R
import com.leshu.superbrain.adapter.SystemAdapter
import com.leshu.superbrain.ui.base.BaseVMFragment
import com.leshu.superbrain.view.SpacesItemDecoration
import com.leshu.superbrain.view.loadpage.BasePageViewForStatus
import com.leshu.superbrain.view.loadpage.loadPageViewForStatus
import com.leshu.superbrain.view.loadpage.SimplePageViewForStatus
import com.leshu.superbrain.vm.SystemViewModel
import kotlinx.android.synthetic.main.fragment_recycleview.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 *@创建者wwy
 *@创建时间 2019/9/25 9:46
 *@描述 体系
 */
class SystemFragment : BaseVMFragment<SystemViewModel>() {
    private val systemAdapter = SystemAdapter()
    private val loadPageViewForStatus: BasePageViewForStatus = SimplePageViewForStatus()
    private lateinit var rootView: loadPageViewForStatus
    override fun setLayoutResId(): Int = R.layout.fragment_recycleview
    override fun initVM(): SystemViewModel = getViewModel()

    override fun initData() {
        mViewModel.getSystemType()
    }

    override fun initView() {
        rootView =
            activity?.let { activity -> loadPageViewForStatus.getRootView(activity) } as loadPageViewForStatus
        rootView.apply {
            failTextView().onClick { refresh() }
        }

        ArticleRv.apply {
            //  addItemDecoration(SpacesItemDecoration(dip(8), LinearLayoutManager.VERTICAL))
            adapter = systemAdapter
        }
        systemAdapter.apply {
            isAnimationFirstOnly = true
            setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
            registerListener {
                onTypeTextClick {
                    SmartToast.show(it.toString())
                }
            }
        }
        refreshLayout.setOnRefreshListener { refresh() }
        refreshLayout.setEnableLoadMore(false)
    }

    private fun refresh() {
        mViewModel.getSystemType()
    }

    override fun startObserve() {
        mViewModel.apply {
            systemClassifyListModel.observe(this@SystemFragment, Observer {
                if (!it.isRefresh) refreshLayout.finishRefresh()
                it.loadPageStatus?.value?.let { loadPageStatus ->
                    loadPageViewForStatus.convert(rootView, loadPageStatus)
                    systemAdapter.setEmptyView(rootView)
                }
                it.showSuccess?.let { list ->
                    systemAdapter.setList(list)
                }
                it.showError?.let { }//加载失败
            })
        }
    }
}