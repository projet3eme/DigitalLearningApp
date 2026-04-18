package com.example.digitallearningapp.model

data class PlaylistResponse(
    val items: List<PlaylistItem>
)

data class PlaylistItem(
    val id: String,
    val snippet: Snippet
)

data class VideoResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val snippet: Snippet,
    val contentDetails: ContentDetails?
)

data class SearchResponse(
    val items: List<SearchItem>
)

data class SearchItem(
    val id: SearchId,
    val snippet: Snippet
)

data class SearchId(
    val videoId: String?
)

data class ContentDetails(
    val videoId: String?
)

data class Snippet(
    val title: String,
    val thumbnails: Thumbnails?,
    val resourceId: ResourceId? = null
)

data class ResourceId(
    val videoId: String?
)

data class Thumbnails(
    val medium: Thumbnail?
)

data class Thumbnail(
    val url: String
)
