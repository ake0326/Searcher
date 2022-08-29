package com.example.searcher.network

import com.example.searcher.utils.logE
import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

object Retrofit {
    private const val BASE_URL: String = "http://webservice.recruit.co.jp"

    val instance: Apis by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val errorInterceptor = Interceptor { chain ->
            try {
                chain.proceed(chain.request())
            }catch (e: ConnectException){
                logE("RETROFIT", "Error Connect :: $e")
                createResponse(chain, e)
            }catch (e: SSLHandshakeException){
                logE("RETROFIT", "Error SSLHandshake :: $e")
                createResponse(chain, e)
            }catch (e: SSLException){
                logE("RETROFIT", "Error SSL :: $e")
                createResponse(chain, e)
            }catch (e: SocketException){
                logE("RETROFIT", "Error Socket :: $e")
                createResponse(chain, e)
            }catch (e: SocketTimeoutException){
                logE("RETROFIT", "Error SocketTimeout :: $e")
                createResponse(chain, e)
            }catch (e: IOException){
                logE("RETROFIT", "Error thread :: $e")
                createResponse(chain, e)
            }catch (e:Exception){
                logE("RETROFIT", "Error thread :: $e")
                createResponse(chain, e)
            }
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(5,5, TimeUnit.SECONDS))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(errorInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        retrofit.create(Apis::class.java)
    }

    private fun createResponse(chain: Interceptor.Chain, e: Any) : Response {
        return Response.Builder()
            .code(418)
            .request(chain.request())
            .body(object: ResponseBody() {
                override fun contentLength() = 0L
                override fun contentType(): MediaType? = null
                override fun source(): BufferedSource = okio.Buffer()
            })
            .message(e.toString() + " : " + chain.request().toString())
            .protocol(Protocol.HTTP_1_1)
            .build()
    }
}