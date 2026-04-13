
package com.example.digitallearningapp

// ===== Playlist Models =====
data class Playlist(
    val id: String,
    val title: String,
    val thumbnails: VideoThumbnail? = null
)

data class PlaylistsResponse(
    val items: List<PlaylistItem>
)

data class PlaylistItem(
    val id: String,
    val snippet: PlaylistSnippet
)

data class PlaylistSnippet(
    val title: String,
    val thumbnails: Thumbnails? = null
)

data class Thumbnails(
    val medium: VideoThumbnail? = null
)

data class VideoThumbnail(
    val url: String,
    val width: Int? = null,
    val height: Int? = null
)

// ===== Video Models =====
data class Video(
    val title: String,
    val videoId: String,
    val thumbnail: String? = null
)

data class YouTubeResponse(
    val items: List<YouTubeVideoItem>,
    val nextPageToken: String? = null
)

data class YouTubeVideoItem(
    val id: VideoId,
    val snippet: YouTubeSnippet,
     val contentDetails: ContentDetails? = null // أضف هذا

)

data class VideoId(
    val videoId: String?
)

data class YouTubeSnippet(
    val title: String,
    val thumbnails: Thumbnails? = null,
    val resourceId: VideoId? = null
)
data class ContentDetails(
    val videoId: String? = null
)