package com.crosswaveconsultancy.language_server.util

import org.assertj.core.api.Assertions.assertThat

fun ApiError.assertValidError(path: String?, status: Int?, message: String?) {
    assertValidError(path, status)
    if(message != null) assertThat(this.message).isEqualTo(message)
}

fun ApiError.assertValidError(path: String?, status: Int?) {
    assertValidError(path)
    if(status != null) assertThat(this.status).isEqualTo(status)
}

fun ApiError.assertValidError(path: String?) {
    assertValidError()

    if(path != null) assertThat(this.path).isEqualTo(path)
}

fun ApiError.assertValidError() {
    assertThat(ok).isFalse
    assertThat(this.status).isNotNull
    assertThat(this.message).isNotNull
    assertThat(this.path).isNotNull
}