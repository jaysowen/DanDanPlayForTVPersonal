package com.seiko.torrent.ui.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.seiko.common.dialog.setLoadFragment
import com.seiko.common.extensions.checkPermissions
import com.seiko.core.data.Result
import com.seiko.torrent.R
import com.seiko.torrent.service.TorrentTaskService
import com.seiko.torrent.ui.base.BaseFragment
import com.seiko.torrent.vm.AddTorrentViewModel
import kotlinx.android.synthetic.main.torrent_fragment_add_torrent.*
import org.koin.android.viewmodel.ext.android.viewModel

private const val TAG_SPINNER_PROGRESS = "spinner_progress"

private const val PERMISSION_REQUEST = 1

private val PERMISSIONS = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

class AddTorrentFragment : BaseFragment() {

    private val args by navArgs<AddTorrentFragmentArgs>()

    private val viewModel by viewModel<AddTorrentViewModel>()

    private lateinit var navController: NavController

    override fun getLayoutId(): Int {
        return R.layout.torrent_fragment_add_torrent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        bindViewModel()
        checkPermissions()
    }

    private fun setupUI() {
        navController = findNavController()
        // Toolbar
        toolbar.setTitle(R.string.torrent_add_torrent_title)
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)

        // ViewPager2
        add_torrent_viewpager.adapter = AddTorrentPagerAdapter(this)
        TabLayoutMediator(add_torrent_tabs, add_torrent_viewpager) { tab, position ->
            tab.text = when(position) {
                INFO_FRAG_POS -> getString(R.string.torrent_info)
                FILES_FRAG_POS -> getString(R.string.torrent_files)
                else -> ""
            }
        }.attach()

        requireActivity().onBackPressedDispatcher.addCallback(this) { onBackPressed() }
    }

    private fun bindViewModel() {
        viewModel.state.observe(this::getLifecycle) { result ->
            when(result) {
                is Result.Success -> updateStateUI(result.data)
                is Result.Error -> handleException(result.exception)

            }
        }
        viewModel.loadData()
    }

    /**
     * 更新状态
     */
    private fun updateStateUI(state: Int) {
        when(state) {
            State.DECODE_TORRENT_FILE -> {
                setLoadFragment(true, getString(R.string.torrent_decode_torrent_default_message))
            }
            State.FETCHING_HTTP -> {
                setLoadFragment(true, getString(R.string.torrent_decode_torrent_downloading_torrent_message))
            }
            State.DECODE_TORRENT_COMPLETED,
            State.FETCHING_HTTP_COMPLETED -> {
                setLoadFragment(false)
            }
            State.FETCHING_MAGNET -> {
                fetch_magnet_progress.visibility = View.VISIBLE
                SnackbarUtils.with(coordinator_layout)
                    .setMessage(getString(R.string.torrent_decode_torrent_fetch_magnet_message))
                    .show()
            }
            State.FETCHING_MAGNET_COMPLETED -> {
                fetch_magnet_progress.visibility = View.GONE
            }
        }
    }

    /**
     * 处理异常
     */
    private fun handleException(throwable: Throwable?) {
        setLoadFragment(false)
        //
        LogUtils.w(throwable)
        ToastUtils.showShort(throwable?.message)
    }

    /************************************************
     *                   权限请求                    *
     ************************************************/

    private fun checkPermissions() {
        if (viewModel.state.value != null) return

        if (checkPermissions(PERMISSIONS)) {
            viewModel.decodeUri(args.uri)
        } else {
            requestPermissions(
                PERMISSIONS,
                PERMISSION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                viewModel.decodeUri(args.uri)
            }
        }
    }

    /************************************************
     *                  Toolbar按钮                  *
     ************************************************/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.torrent_add_torrent, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.add_torrent_dialog_add_menu -> {
                when (val result = viewModel.buildTorrentTask()) {
                    is Result.Success -> {
                        TorrentTaskService.addTorrent(requireActivity(), result.data)
                        onBackPressed()
                    }
                    is Result.Error -> {
                        ToastUtils.showShort(result.exception.message)
                    }
                }
            }
        }
        return true
    }

    private fun onBackPressed() {
        LogUtils.d("onBackPressed")
        navController.popBackStack()
    }

}

/**
 * 详情、文件
 */
private const val NUM_FRAGMENTS = 2
private const val INFO_FRAG_POS = 0
private const val FILES_FRAG_POS = 1

private class AddTorrentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =
        NUM_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            INFO_FRAG_POS -> AddTorrentInfoFragment.newInstance()
            FILES_FRAG_POS -> AddTorrentFilesFragment.newInstance()
            else -> throw RuntimeException("Can't create fragment with position=$position.")
        }
    }
}

/**
 * 种子文件、磁力、Url解析状态
 */
@Retention(AnnotationRetention.SOURCE)
internal annotation class State {
    companion object {
        const val DECODE_TORRENT_FILE = 0
        const val DECODE_TORRENT_COMPLETED = 1
        const val FETCHING_MAGNET = 2
        const val FETCHING_HTTP = 3
        const val FETCHING_MAGNET_COMPLETED = 4
        const val FETCHING_HTTP_COMPLETED = 5
    }
}