package com.example.digitallearningapp.network

object TextNormalizer {
    
    fun normalize(text: String): String {
        if (text.isEmpty()) return ""
        return text.lowercase()
            .replace("ال", "")
            .replace(" ", "")
            .replace("ة", "ه")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .replace("ى", "ي")
            .trim()
    }

    fun getSubjectKeywords(subject: String): List<String> {
        val s = normalize(subject)
        return when {
            s.contains("اسلام") || s.contains("دين") || s.contains("تربيه") -> listOf("اسلاميه", "تربيه", "دين")
            s.contains("عرب") || s.contains("لغه") -> listOf("عربيه", "لغه", "عربي")
            s.contains("رياض") -> listOf("رياضيات", "رياضي")
            else -> listOf(s)
        }
    }

    fun getYearKeywords(year: String): List<String> {
        val y = normalize(year)
        return when {
            y.contains("اول") || y.contains("1") -> listOf("اولى", "1")
            y.contains("ثاني") || y.contains("2") -> listOf("ثانيه", "2")
            y.contains("ثالث") || y.contains("3") -> listOf("ثالثه", "3")
            y.contains("رابع") || y.contains("4") -> listOf("رابعه", "4")
            y.contains("خامس") || y.contains("5") -> listOf("خامسه", "5")
            else -> listOf(y)
        }
    }
}
