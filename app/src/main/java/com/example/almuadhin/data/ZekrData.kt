package com.example.almuadhin.data

data class Zekr(val name: String, val audio: String = "")

object ZekrData {
    val zekrList = listOf(
        Zekr("سبحان الله"),
        Zekr("الحمد لله"),
        Zekr("لا إله إلا الله"),
        Zekr("الله أكبر"),
        Zekr("سبحان الله وبحمده"),
        Zekr("سبحان الله العظيم"),
        Zekr("أستغفر الله"),
        Zekr("لا حول ولا قوة إلا بالله"),
        Zekr("اللهم صل على محمد"),
        Zekr("سبحان الله وبحمده سبحان الله العظيم")
    )

    val sobhanallah_wabehemden = "سبحان الله وبحمده"
}
