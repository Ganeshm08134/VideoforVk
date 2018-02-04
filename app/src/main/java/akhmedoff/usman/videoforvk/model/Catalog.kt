package akhmedoff.usman.videoforvk.model

class Catalog(
    var items: MutableList<VideoCatalog>,
    var name: String?,
    var id: String?,
    var view: String? = null,
    var canHide: Boolean?,
    var type: String?
)