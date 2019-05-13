package com.login.mvvm.login_test.data

import retrofit2.Response


class BaseException : RuntimeException {
    companion object {
        /*
        * An [IOException] occured while communicating to server
        *
        * */
        const val NETWORK = 0

        /*
       * A non-2xx HTTP status code was received from the server
       *
       * */
        const val HTTP = 1

        /*
        * An error server with code & message
        * */
        const val SERVER = 2

        /*
      * An internal error occurred while attempting to execute a request. It is best practice to re-throw this exception
       * so your applications crashes
       *
      * */
        const val UNEXPECTED = 3

        fun toHttpError(response: Response<*>): BaseException = BaseException(HTTP, response)

        fun toNetworkError(cause: Throwable): BaseException = BaseException(NETWORK, cause)

        fun toServerError(errorResponse: BaseErrorResponse): BaseException = BaseException(SERVER, errorResponse)

        fun toUnexpectedError(cause: Throwable): BaseException = BaseException(UNEXPECTED, cause)


    }

    var errorType: Int = NETWORK
    var errorResponse: BaseErrorResponse? = null
    var response: Response<*>? = null
    val serverErrorCode: String?
        get() = errorResponse?.statusCode?.toString()

    constructor(type: Int, cause: Throwable) : super(cause.message, cause) {
        errorType = type
    }

    constructor(type: Int, errorResponse: BaseErrorResponse?) {
        this.errorType = type
        this.errorResponse = errorResponse
    }

    constructor(type: Int, response: Response<*>) {
        val code = response.code()
        this.errorType = type
        this.response = response
    }

    override val message: String?
        get() = when (errorType) {
            SERVER -> errorResponse?.statusMessage
            NETWORK -> cause?.message
            HTTP -> response?.message()
            else -> {
                "Unexpected error has occurred"
            }
        }

}
