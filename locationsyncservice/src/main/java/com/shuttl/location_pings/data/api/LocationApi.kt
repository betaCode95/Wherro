package com.shuttl.location_pings.data.api

import com.shuttl.location_pings.data.model.request.SendLocationRequestBody
import com.shuttl.location_pings.data.model.response.BaseResponse
import retrofit2.http.*

interface LocationApi {

    @POST("")
    suspend fun syncLocation(@Url fullUrl: String,
                             @Header("x-api-key") apiKey: String,
                             @Header("Content-Type") contentType: String,
                             @Body body: SendLocationRequestBody<Any>): BaseResponse

}