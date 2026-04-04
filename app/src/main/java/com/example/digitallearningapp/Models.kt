

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
    val items: List<YouTubeVideoItem>
)

data class YouTubeVideoItem(
    val id: String,
    val snippet: YouTubeSnippet
)



data class YouTubeSnippet(
    val title: String,
    val thumbnails: Thumbnails? = null,
    val resourceId: ResourceId? = null
)
data class ResourceId(
    val videoId: String? = null
)