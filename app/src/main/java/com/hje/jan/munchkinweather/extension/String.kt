package com.hje.jan.munchkinweather.extension

/**
 * 检查用户输入是否为手机号码格式
 * */
fun String.isPhoneNumber(): Boolean {
    return this.matches(Regex("1[358][0-9]{9}"))
}

/**
 * 检查用户输入是否为验证码格式
 * */
fun String.isVerificationCode(): Boolean {
    return this.matches(Regex("[0-9]{6}"))
}

/**
 * 检查用户输入是否为规范的用户名格式
 * */
fun String.isValidUsername(): Boolean {
    return this.matches(Regex("^[a-zA-Z][a-zA-Z0-9_]{4,15}\$"))
}

/**
 * 检查用户输入是否为正确密码格式
 * */
fun String.isValidPassword(): Boolean {
    return this.matches(Regex("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,10}\$"))
}

/**
 * 隐式显示电话号码
 * */
fun String.toPhoneText(): String {
    val sb = StringBuilder()
    for (c in this.indices) {
        if (this.length >= 7) {
            if (c <= 2 || c >= 7)
                sb.append(this[c])
            else sb.append('*')
        } else {
            sb.append('*')
        }
    }
    return sb.toString()
}


/**
 * 隐式显示密码
 * */
fun String.toPasswordText(): String {
    val sb = StringBuilder()
    for (c in this.indices) {
        if (c == 0 || c == this.length - 1)
            sb.append(this[c])
        else sb.append('*')
    }
    return sb.toString()
}