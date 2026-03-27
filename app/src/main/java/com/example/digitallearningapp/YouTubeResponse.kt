// YouTubeResponse.kt
package com.example.digitallearningapp;

data class YouTubeResponse(
        val items: List<YouTubeItem>
)

data class YouTubeItem(
        val id: Id,
        val snippet: Snippet
)

data class Id(
        val videoId: String
)

// يستخدم فقط عند جلب الفيديوهات من Playlist
data class ResourceId(
        val videoId: String?
)

data class Snippet(
        val title: String,
        val thumbnails: Thumbnails,
        val resourceId: ResourceId? = null // هذا السطر الجديد
)

data class Thumbnails(
        val medium: Thumbnail
)

data class Thumbnail(
        val url: String
)