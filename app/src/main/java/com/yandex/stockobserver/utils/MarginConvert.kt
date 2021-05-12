package com.yandex.stockobserver.utils



fun marginToString(currentPrice: Double, prevClosePrice: Double): String {
    val margin:Double = (Math.round((currentPrice - prevClosePrice)*100).toDouble()/100)
    val percent:Double = (Math.round((margin/prevClosePrice)*100*100).toDouble()/100)
    if (margin>0){
      return "+$margin ($percent%)"
    }else{
      return "$margin ($percent%)"
    }
}