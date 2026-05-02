package com.example.digitallearningapp.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.digitallearningapp.data.repository.YouTubeRepository
import com.example.digitallearningapp.model.Subject
import com.example.digitallearningapp.model.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit
@Serializable
data class VideoInsert(
    @SerialName("video_id") val videoId: String,
    val title: String,
    val thumbnail: String,
    @SerialName("playlist_id") val playlistId: String,
    @SerialName("subject_id") val subjectId: Int
)
class VideoSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    init {
        Log.d("SYNC", "=== VideoSyncWorker CREATED ===")
    }

    override suspend fun doWork(): Result {
        Log.d("SYNC", "=== doWork CALLED ===")

        return try {
            val apiKey = "AIzaSyC3VzbxUXNJHp_B3xjuSFUpjr3FzWFLSBg"
            val repository = YouTubeRepository()

            Log.d("SYNC", "Connecting to Supabase...")

            val subjects = SupabaseClient.client.postgrest["subjects"]
                .select()
                .decodeList<Subject>()

            Log.d("SYNC", "Got ${subjects.size} subjects")

            val youtubeSubjects = subjects.filter {
                !it.playlistId.startsWith("http") && it.playlistId.isNotEmpty()
            }

            Log.d("SYNC", "YouTube playlists: ${youtubeSubjects.size}")
            coroutineScope {
                youtubeSubjects.map { subject ->
                    async(Dispatchers.IO) {
                        try {
                            Log.d("SYNC", "Fetching: ${subject.name}")
                            val videos = repository.getVideosFromPlaylist(subject.playlistId, apiKey)
                            Log.d("SYNC", "Got ${videos.size} videos for ${subject.name}")

                            if (videos.isNotEmpty()) {
                                val videoInserts = videos.map { video ->
                                    VideoInsert(
                                        videoId = video.videoId,
                                        title = video.title,
                                        thumbnail = video.thumbnail ?: "",
                                        playlistId = subject.playlistId,
                                        subjectId = subject.id
                                    )
                                }
                                SupabaseClient.client.postgrest["videos"].upsert(videoInserts)
                                Log.d("SYNC", "Uploaded ${videos.size} videos ✅")
                            }
                        } catch (e: Exception) {
                            Log.e("SYNC", "Error: ${e.message}")
                        }
                    }
                }.awaitAll()
            }
            Log.d("SYNC", "=== ALL DONE ===")
            Result.success()

        } catch (e: Exception) {
            Log.e("SYNC", "FAILED: ${e.message}")
            Result.retry()
        }
    }

    companion object {
        fun schedule(context: Context) {
            Log.d("SYNC", "Scheduling worker...")

            val immediateRequest = OneTimeWorkRequestBuilder<VideoSyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<VideoSyncWorker>(
                15, TimeUnit.MINUTES
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).apply {
                cancelAllWork()
                enqueue(immediateRequest)
                enqueueUniquePeriodicWork(
                    "video_sync",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodicRequest
                )
            }

            Log.d("SYNC", "Worker scheduled ✅")
        }
    }
}