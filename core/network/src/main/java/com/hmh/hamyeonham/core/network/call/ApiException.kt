package com.hmh.hamyeonham.core.network.call

import com.hmh.hamyeonham.core.network.model.ErrorResponse

class ApiException(
    val errorResponse: ErrorResponse,
    //throwable: Throwable
    val errorCode: Int
): Exception()