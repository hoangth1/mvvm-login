package com.login.mvvm.login_test.rx


import com.google.gson.Gson
import com.login.mvvm.login_test.data.BaseException
import com.login.mvvm.login_test.data.BaseErrorResponse
import io.reactivex.*
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    companion object {
        fun create() = RxErrorHandlingCallAdapterFactory()
    }

    private val instance = RxJava2CallAdapterFactory.create()
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(retrofit, instance.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>)
    }
}

class RxCallAdapterWrapper<R>(
    private val retrofit: Retrofit,
    private val wrapped: CallAdapter<R, Any>
) : CallAdapter<R, Any> {
    override fun adapt(call: Call<R>): Any {
        val result = wrapped.adapt(call)
        return when (result) {
            is Single<*> -> result.onErrorResumeNext(Function<Throwable, SingleSource<Nothing>> {
                Single.error(convertToBaseException(it))
            })
            is Observable<*> -> result.onErrorResumeNext(Function<Throwable, ObservableSource<Nothing>>() {
                Observable.error<Nothing>(convertToBaseException(it))
            })
            is Completable -> result.onErrorResumeNext {
                Completable.error(convertToBaseException(it))
            }
            else -> result
        }
    }

    override fun responseType(): Type = wrapped.responseType()

    private fun convertToBaseException(throwable: Throwable): BaseException {
        try {
            when (throwable) {
                is BaseException -> return throwable
                is HttpException -> {
                    if (throwable.response().errorBody() == null) {
                        return BaseException.toHttpError(throwable.response())
                    }
                    val errorMessage = throwable.response().errorBody()!!.string()
                    val errorResponse = Gson().fromJson(errorMessage, BaseErrorResponse::class.java)
                    return BaseException.toServerError(errorResponse)
                }
                else -> return BaseException.toUnexpectedError(throwable)
            }
        } catch (e: IOException) {
            return BaseException.toUnexpectedError(throwable)
        }
    }
}