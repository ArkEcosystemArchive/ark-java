package io.ark.core


import com.google.common.io.BaseEncoding
import java.text.SimpleDateFormat

class Slot {

  static Date beginEpoch

  static {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    beginEpoch = dateFormat.parse("2017-03-21 13:00:00")
  }

  static int getTime(Date date){
    if(!date)
      date = new Date()

    (date.time - beginEpoch.time)/1000

  }

}
