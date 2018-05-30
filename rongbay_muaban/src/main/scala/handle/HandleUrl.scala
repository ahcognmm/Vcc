package  handle
import java.text.{Normalizer, SimpleDateFormat}
import java.util.Date

import org.apache.commons.lang3.StringEscapeUtils

import scala.util.parsing.json.JSON

class HandleUrl (val url: String) {

    val handleUrl = handle
    val fieldExtra = getFieldExtra
    val subcat = getSubCat

    protected def handle(): Map[String, Any] = {
        val ad_id: Long = url.substring(url.length - 13, url.length - 5).toLong
        val time1 = System.nanoTime()
        val result = scala.io.Source.fromURL(s"https://rongbay.com/api/store/product_zamba.php?act=list_item&ad_id=${ad_id - 1}&limit=1").mkString
        val time2 = System.nanoTime()
        println(s"time =${(time2 - time1) / 1000000}")
        val jsO = JSON.parseFull(result)
        val data = jsO.get.asInstanceOf[Map[String, Any]].get("data").get.asInstanceOf[List[String]](0).asInstanceOf[Map[String, Any]]
        data
    }

    protected def getFieldExtra(): String = {
        handleUrl.get("field_extra").get.asInstanceOf[String]
    }

    protected def getInfo(id: String): Int = {

        val splitedField = fieldExtra.split(id)
        if (splitedField.size != 1) {
            val tail = splitedField(splitedField.size - 1)
            tail.split("a:1:\\{i:")(1).split(";")(0).toInt
        } else 0

    }

    // subcat
    def getType(): Double = {
        handleUrl.get("ad_id_subcat").get.asInstanceOf[Double]
    }

    def getDate(): String = {
        parseTime(handleUrl.get("ad_date").get.asInstanceOf[Double])
    }

    def getSubCat(): Double = {
        handleUrl.get("ad_id_subcat").get.asInstanceOf[Double]
    }

    def getDescription(): String = {
        Normalizer.normalize(StringEscapeUtils.unescapeHtml3(handleUrl.get("ad_description").get.asInstanceOf[String].toLowerCase()), Normalizer.Form.NFC)
    }


    def getPricePerM2(): Double = {
        val price = getPrice
        val area = getArea()
        if (price != 0 && area != 0) {
            if (subcat == 5) {
                price / area
            } else {
                val t = getTotalFloor()
                if (t != 0) {
                    price / (area * t)
                } else 0
            }

        } else
            0
    }

    def getPrice(): Double = {
        try {
            fieldExtra.split("\\\"giatien\\\";s:")(1).split(";")(0).toDouble
        } catch {
            case e: Exception => {
                try {
                    val price = fieldExtra.split("\\\"giatien\\\";d:")(1).split(";")(0).toDouble
                    if (price < 1000) {
                        price * 1000000000
                    } else
                        price
                } catch {
                    case e: Exception => {
                        try {
                            val des = getDescription()
                            if (des.contains("giá ")) {
                                if (!des.contains("giá thương lượng")) {
                                    val price = des.split("giá ")
                                            .last.split("t")(0)
                                            .split(" ").last
                                            .replaceAll(":", "").replaceAll("-", "").replaceAll(",", ".").toDouble
                                    val x =0
                                    if (price < 100) {
                                        price * 1000000000
                                    } else price * 1000000
                                } else 0
                            } else 0
                        } catch {
                            case e: Exception => 0
                        }
                    }
                }
            }
        }
    }

    def getFullAdd(): String = {
        try {
            Normalizer.normalize(fieldExtra.split("\"all_address\\\";s:")(1).split("\\\"")(1),Normalizer.Form.NFC)
        } catch {
            case e: Exception => {
                "No information"
            }

        }
    }


    def getArea(): Int = {
        try {
            fieldExtra.split("\\\"dientich\\\";s:")(1).split("\\\"")(1).toInt
        } catch {
            case e: Exception => {
                -1
            }
        }
    }

    def parseTime(dateTime: Double): String = {
        val s: Long = dateTime.toLong * 1000
        val df2 = new SimpleDateFormat("dd/MM/YYYY")
        df2.format(new Date(s))
    }

    // chung cư
    def getMainDoorDirection(): Int = {
        if (subcat == 5) {
            getInfo("\\\"73\\\"")
        } else 0
    }

    // chung cu
    def getBalconyDirection(): Int = {
        if (subcat == 5) {
            getInfo("\\\"82\\\"")
        } else 0
    }

    // chung cu
    def getTotalBedRoom(): Int = {
        getInfo("\\\"Số phòng ngủ\\\"")
    }

    //chung cu
    def getPosition(): Int = {
        if (subcat == 5) {
            getInfo("\\\"92\\\"")
        } else 0
    }

    // nhà mặt đất
    def getDirection(): Int = {

        getInfo("\\\"Hướng\\\"")
    }

    // nha mat dat
    def getTotalFloor(): Float = {
        val splitedDes = getDescription().split(" tầng")
        val rs: Float = {
            if (splitedDes.size != 1) {
                var x: Float = 0
                for (i <- 0 to splitedDes.size - 2) {
                    if (x == 0) {
                        try {
                            val arr = splitedDes(i).split(' ')
                            x = arr(arr.size - 1).replaceAll("(^\\h*)|(\\h*$)", "").replaceAll(",", ".").toFloat
                        } catch {
                            case e: Exception =>
                        }
                    }
                }
                x
            } else 0
        }
        rs
    }

    //nha mat dat
    def getFacade(): Int = {
        getInfo("\\\"Mặt tiền\\\"")
    }

    def getPaper(): Int = {
        getInfo("\\\"Giấy tờ pháp lý\\\"")
    }


}

//object tets {
//    def main(args: Array[String]): Unit = {
//        val time1 = System.nanoTime()
//        val handle = new HandleUrl("28344746" +
//                ".html")
//        println("++++++++++++++++++++")
//        println("Cua chinh: " + handle.getMainDoorDirection())
//        println("huong ban cong: " + handle.getBalconyDirection())
//        println("Gia: " + handle.getPrice())
//        println("Dien tich: " + handle.getArea())
//        println("Huong nha: " + handle.getDirection())
//        println("So tang: " + handle.getTotalFloor())
//        println("Gia/m2: " + handle.getPricePerM2)
//        println("Giay to: " + handle.getPaper())
//        println("Dia chi: " + handle.getFullAdd())
//        println("So phong ngu: " + handle.getTotalBedRoom())
//        println("Can goc/ can thuong: " + handle.getPosition())
//        println("Mat tien: "+ handle.getFacade())
//        println("++++++++++++++++++++")
//        val time2 = System.nanoTime()
//        print((time2 - time1) / 1000000)
//    }
//}

//https://rongbay.com/api/store/product_zamba.php?act=list_item&ad_id=28178208&limit=1
// 1 thang = 2592000
