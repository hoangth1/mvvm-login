package com.login.mvvm.login_test

import android.content.res.Resources
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.login.mvvm.login_test.TestUtil.getJsonFromResource
import com.login.mvvm.login_test.data.AuthenApi
import com.login.mvvm.login_test.data.BaseException
import com.login.mvvm.login_test.data.model.Token
import com.login.mvvm.login_test.data.repository.AuthenRepository
import com.login.mvvm.login_test.rx.ScheduleProvider
import com.login.mvvm.login_test.rx.SchedulerProviderImpl
import com.login.mvvm.login_test.screen.ui.fragment.login.LoginViewModel
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class LoginViewModelTest : BaseApiTest() {

    /**
     * This rule make Architecture Component (as LiveData) working with JUnit
     */
    @Rule
    @JvmField
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    lateinit var authenApi: AuthenApi

    @Spy
    lateinit var scheduleProvider: SchedulerProviderImpl

    var resources = mock(Resources::class.java)

    @Mock
    lateinit var authenRepository: AuthenRepository

    lateinit var loginViewModel: LoginViewModel

    override fun setUp() {
        super.setUp()

        // Get an instance of Retrofit
        val retrofit = getTimeoutRetrofitInstance()

        // Get an instance of blogService
        authenApi = retrofit.create(AuthenApi::class.java)

        // init viewmodel
        loginViewModel = LoginViewModel(resources, authenRepository, scheduleProvider)
    }

    /**
     * Test for case connect api and login succeeded
     */
    @Test
    fun getToken_valid_succeededResponse() {

        // define behavior
        `when`(authenRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(
            Single.just(Token("tiendat@gmail.com", "123456789"))
        )


    }

    /**
     * Test for case connect api and login failed
     */
    @Test
    fun getToken_invalid_succeededResponse() {

    }

    /**
     * Test for case connect server failed (400 response)
     */
    @Test
    fun getToken_failedResponse() {
        val errorResponse = BaseException(BaseException.HTTP,
            HttpException(Response.error<String>(404, ResponseBody.create(null, " Some Error"))).response())

        // define behavior
        `when`(authenRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(
            Single.error(errorResponse)
        )

        loginViewModel.login("dat@gmail.com", "123456")

        // viewModel test
        verify(authenRepository).login("dat@gmail.com", "123456")

        assert(loginViewModel.token == null)

        Assert.assertEquals("Response.error()", loginViewModel.error.value)

    }

    /**
     * Test for case connect with valid time out ( 4s < Server Time out = 5s )
     */
    @Test
    fun getToken_succeededResponse_timeout() {

        val testObserver = TestObserver<Token>()

        // Mock a response with status 200
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBodyDelay(4, TimeUnit.SECONDS)
            .throttleBody(1024, 1, TimeUnit.SECONDS)
            .setBody(getJsonFromResource("login.json"))

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        // define behavior
        `when`(authenRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            authenApi.login("dat@gmail.com", "123456").toObservable().subscribe(testObserver)
            testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
            Single.just(testObserver.values()[0])
        }

        loginViewModel.login("dat@gmail.com", "123456")

        // equal http request
        val request = mockWebServer.takeRequest()
        Assert.assertEquals("/login?email=dat%40gmail.com&password=123456", request.path)

        // equal token response
        testObserver.assertValue { response ->
            response.accessToken == "accessToken_response" &&
                    response.refreshToken == "refreshToken_response"
        }

        // viewModel test
        verify(authenRepository).login("dat@gmail.com", "123456")

        assert(
            loginViewModel.token!!.accessToken == "accessToken_response" &&
                    loginViewModel.token!!.refreshToken == "refreshToken_response"
        )
    }

    /**
     * Test for case connect with invalid time out (6s)
     *
     */
    @Test
    fun getToken_exceed_timeout() {

        // define error for response, but in fact, it is not returned in `when` because connect failed
        val errorResponse = Throwable(" Connect Timeout")

        val testObserver = TestObserver<Token>()

        // Mock a response with status 200
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBodyDelay(6, TimeUnit.SECONDS)
            .throttleBody(1024, 1, TimeUnit.SECONDS)
            .setBody(getJsonFromResource("login.json"))

        // Enqueue request
        mockWebServer.enqueue(mockResponse)

        // define behavior
        `when`(authenRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenAnswer {
            authenApi.login("dat@gmail.com", "123456").toObservable().subscribe(testObserver)
            testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)
            Single.error<Throwable>(errorResponse)
        }

        loginViewModel.login("dat@gmail.com", "123456")

        // equal http request
        val request = mockWebServer.takeRequest()
        Assert.assertEquals("/login?email=dat%40gmail.com&password=123456", request.path)

        // equal token response
        testObserver.apply {
            assertNoValues()
            assertNotComplete()
        }

        // viewModel test
        verify(authenRepository).login("dat@gmail.com", "123456")

        assert(loginViewModel.token == null)
    }
}
