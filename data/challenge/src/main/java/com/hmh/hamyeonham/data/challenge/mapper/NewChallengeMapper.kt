package com.hmh.hamyeonham.data.challenge.mapper

import com.hmh.hamyeonham.challenge.model.NewChallenge
import com.hmh.hamyeonham.core.network.challenge.model.NewChallengeRequest

fun NewChallengeRequest.toNewChallenge(): NewChallenge {
    return NewChallenge(
        period = period,
        goalTime = goalTime
    )
}

fun NewChallenge.toNewChallengeRequest(): NewChallengeRequest {
    return NewChallengeRequest(
        period = period,
        goalTime = goalTime
    )
}

