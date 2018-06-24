package akhmedoff.usman.videoforvk.ui.profile

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.data.utils.getAlbumRepository
import akhmedoff.usman.data.utils.getUserRepository
import akhmedoff.usman.data.utils.getVideoRepository
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.Router
import akhmedoff.usman.videoforvk.ui.album.AlbumFragment
import akhmedoff.usman.videoforvk.ui.video.VideoFragment
import akhmedoff.usman.videoforvk.ui.view.MarginItemDecorator
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

private const val USER_ID = "user_id"
private const val IS_USER = "is_user"

class ProfileFragment : Fragment(), ProfileContract.View {

    companion object {
        const val FRAGMENT_TAG = "profile_fragment_tag"
        const val RETAINED_KEY = "retained"

        fun createFragment(userId: String?) = ProfileFragment().apply {
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)
            bundle.putBoolean(IS_USER, false)
            arguments = bundle
        }
    }

    override lateinit var presenter: ProfileContract.Presenter

    private val recyclerAdapter: ProfileRecyclerAdapter by lazy {
        ProfileRecyclerAdapter({ video, view -> showVideo(video, view) },
                { album, view -> showAlbum(album, view) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProfilePresenter(this,
                getUserRepository(context!!),
                getVideoRepository(context!!),
                getAlbumRepository(context!!))
        presenter.view = this

        if (savedInstanceState == null || !savedInstanceState.containsKey(RETAINED_KEY))
            presenter.onCreated()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.onViewCreated()

        profile_recycler.adapter = recyclerAdapter
        profile_recycler.itemAnimator = DefaultItemAnimator()
        profile_recycler.addItemDecoration(MarginItemDecorator(1,
                resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)))
    }

    override fun onStart() {
        super.onStart()
        presenter.view = this
    }

    override fun showUserName(name: String) {
        collapsingToolbar.title = name
        toolbar.title = name
    }

    override fun showUserPhoto(photoUrl: String) {
        Picasso.get().load(photoUrl).into(user_avatar)

    }

    override fun setIsUser(isUser: Boolean) {

    }

    override fun getUserId() = arguments?.getString(USER_ID)

    override fun showLoadingError() {
    }

    override fun showLoading(isLoading: Boolean) {
        swipe_update.isRefreshing = isLoading
    }

    override fun showUserStatus(status: String) {
        toolbar.subtitle = status
    }

    override fun showAlbums(albums: PagedList<Album>) {
        recyclerAdapter.albums = albums
    }

    override fun showVideos(videos: PagedList<Video>) {
        recyclerAdapter.submitList(videos)
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyed()
    }

    private fun showVideo(video: Video, view: View) {
        val fragment = VideoFragment.getInstance(video, view.transitionName)

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoFragment.FRAGMENT_TAG,
                    view
            )
        }
    }

    private fun showAlbum(album: Album, view: View) {
        val fragment = AlbumFragment.getFragment(album, view.transitionName)

        activity?.supportFragmentManager?.let { fragmentManager ->
            Router.replaceFragment(
                    fragmentManager,
                    this,
                    fragment,
                    true,
                    VideoFragment.FRAGMENT_TAG,
                    view
            )
        }
    }

    override fun getIsUser() = arguments?.getBoolean(IS_USER) ?: true
}