package akhmedoff.usman.videoforvk.main

import akhmedoff.usman.videoforvk.Error
import akhmedoff.usman.videoforvk.base.BasePresenter
import akhmedoff.usman.videoforvk.data.repository.VideoRepository
import akhmedoff.usman.videoforvk.model.Catalog
import akhmedoff.usman.videoforvk.model.CatalogItem
import akhmedoff.usman.videoforvk.model.CatalogItemType
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent

class MainPresenter(private val videoRepository: VideoRepository) :
    BasePresenter<MainContract.View>(), MainContract.Presenter {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() = refresh()

    override fun refresh() {
        loadCatalogs()
    }

    override fun loadCatalogs() {
        view?.showLoading()
        view?.let { view ->
            videoRepository.getCatalog().observe(view, Observer { pagedList ->
                pagedList?.let { catalogs ->
                    view.hideLoading()
                    view.showList(catalogs)
                }
            })
        }
    }

    override fun clickCatalog(catalog: Catalog) {
        view?.showCatalog(catalog)
    }

    override fun clickItem(item: CatalogItem) {
        when (item.type) {
            CatalogItemType.VIDEO -> view?.showVideo(item)

            CatalogItemType.ALBUM -> view?.showAlbum(item)
        }
    }

    override fun error(error: Error, message: String) {

    }
}