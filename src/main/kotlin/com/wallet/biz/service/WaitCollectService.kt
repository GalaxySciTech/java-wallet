package com.wallet.biz.service

import com.wallet.entity.domain.WaitCollect

interface WaitCollectService {
    fun getById(id:Long): WaitCollect?

    fun save(waitCollect:WaitCollect): WaitCollect
    fun clearByBean(waitCollect: WaitCollect)
    fun getByBean(waitCollect: WaitCollect): List<WaitCollect>
}
