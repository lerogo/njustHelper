package com.njust.helper.library.borrowed

import com.njust.helper.BuildConfig
import com.njust.helper.tools.HttpClients
import com.njust.helper.tools.JsonData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.lang.reflect.Type

interface BorrowedBooksApi {
    @FormUrlEncoded
    @POST("libBorrow.php")
    fun borrowedBooks(
            @Field("stuid") stuid: String,
            @Field("pwd") pwd: String
    ): Observable<JsonData<String>>

    companion object {
        val INSTANCE: BorrowedBooksApi = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(object : Converter.Factory() {
                    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
                        return Converter<ResponseBody, JsonData<String>> { it ->
                            object : JsonData<String>(it.string()) {
                                @Throws(Exception::class)
                                override fun parseData(jsonObject: JSONObject): String {
                                    return jsonObject.getString("content")
                                }
                            }
                        }
                    }
                })
                .client(HttpClients.globalOkHttpClient)
                .build()
                .create(BorrowedBooksApi::class.java)
    }
}
