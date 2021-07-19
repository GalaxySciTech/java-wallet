package com.wallet.biz.domain

/** 
 * Created by pie on 2020/7/11 08: 11. 
 */
class  PageEntity<T>(var entity: T) {
    var page:Int=0
    var size=Int.MAX_VALUE
}