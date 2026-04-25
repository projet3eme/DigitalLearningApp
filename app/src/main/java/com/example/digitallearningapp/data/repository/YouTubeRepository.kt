package com.example.digitallearningapp.data.repository

import android.util.Log
import com.example.digitallearningapp.model.Subject
import com.example.digitallearningapp.model.Video
import com.example.digitallearningapp.network.YouTubeApi
import com.example.digitallearningapp.network.RetrofitInstance
import com.example.digitallearningapp.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YouTubeRepository(private val api: YouTubeApi = RetrofitInstance.api) {

    /**
     * جلب المواد مع ضمان الظهور لكل المستويات والسنوات عبر البحث الجزئي المرن
     */
    suspend fun getSubjects(level: String, year: String): List<Subject> = withContext(Dispatchers.IO) {
        try {
            Log.d("SUPABASE_DEBUG", "Querying: Level='$level', Year='$year'")
            
            // جلب كافة المواد من قاعدة البيانات للتأكد من وجودها
            val result = SupabaseClient.client.postgrest["subjects"]
                .select()
                .decodeList<Subject>()

            // تطبيق "الفلترة الذكية" التي تتجاوز كل عوائق الكتابة العربية
            val filtered = result.filter { subject ->
                val dbLevel = normalizeSmart(subject.level)
                val dbYear = normalizeSmart(subject.year)
                val searchLevel = normalizeSmart(level)
                val searchYear = normalizeSmart(year)

                // البحث المرن: هل المسمى في قاعدة البيانات يحتوي على ما نبحث عنه؟
                (dbLevel.contains(searchLevel) || searchLevel.contains(dbLevel)) &&
                (dbYear.contains(searchYear) || searchYear.contains(dbYear))
            }

            Log.d("SUPABASE_DEBUG", "Match count: ${filtered.size}")
            filtered
        } catch (e: Exception) {
            Log.e("SUPABASE_DEBUG", "Critical error: ${e.message}")
            emptyList()
        }
    }

    /**
     * دالة التطهير "الذهبية" لتوحيد كل أشكال الكتابة العربية
     */
    private fun normalizeSmart(text: String?): String {
        if (text == null) return ""
        return text.trim()
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .replace("ة", "ه")
            .replace("ى", "ي")
            .replace(" ", "")
            .replace("السنة", "") // إزالة كلمة السنة لزيادة مرونة المطابقة
            .lowercase()
    }

    suspend fun getVideosFromPlaylist(playlistId: String, apiKey: String): List<Video> {
        return try {
            val response = api.getVideosFromPlaylist(
                apiKey = apiKey,
                playlistId = playlistId,
                part = "snippet,contentDetails",
                maxResults = 50
            )
            response.items.mapNotNull { item ->
                val vId = item.contentDetails?.videoId ?: item.snippet.resourceId?.videoId
                if (!vId.isNullOrEmpty()) {
                    Video(
                        title = item.snippet.title ?: "No Title",
                        videoId = vId,
                        thumbnail = item.snippet.thumbnails?.medium?.url
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
