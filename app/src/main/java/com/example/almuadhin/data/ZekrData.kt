package com.example.almuadhin.data

data class Zekr(val name: String, val audio: String = "")

object ZekrData {
    val zekrList = listOf(
        Zekr("سبحان الله", "sobhanallah_wabehamdeh"),
        Zekr("الحمد لله", "alhamdo_lelah"),
        Zekr("لا إله إلا الله", ""),
        Zekr("الله اكبر", ""),
        Zekr("سبحان الله وبحمده", "sobhanallah_wabehamdeh"),
        Zekr("سبحان الله العظيم", ""),
        Zekr("استغفر الله", ""),
        Zekr("لا حول ولا قوة إلا بالله", "lahawla_wlaqowat"),
        Zekr("اللهم صل على محمد", "nozaker_salt_ala_habib"),
        Zekr("سبحان الله وبحمده سبحان الله العظيم", "sobhanallah_wabehamdeh")
    )

    val sobhanallah_wabehemden = "سبحان الله وبحمده"
}
